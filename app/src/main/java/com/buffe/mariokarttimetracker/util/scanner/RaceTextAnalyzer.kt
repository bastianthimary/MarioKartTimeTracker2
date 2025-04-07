package com.buffe.mariokarttimetracker.util.scanner

import android.annotation.SuppressLint
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.*
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.min

class RaceTextAnalyzer(private val listener: TextResultListener) : ImageAnalysis.Analyzer {
    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    // TimeCache als vereinfachte Version beibehalten - hilft bei Stabilisierung der Ergebnisse
    // zwischen Frames, falls in einem Frame keine Zeit erkannt wird
    private val lastDetectedTimes = mutableListOf<String>()
    private val maxCacheSize = 3 // Reduziert auf 3 (war vorher 5)

    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private val processingInProgress = AtomicBoolean(false)

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        // Wenn bereits eine Verarbeitung läuft, dieses Bild überspringen
        if (processingInProgress.getAndSet(true)) {
            imageProxy.close()
            return
        }

        val bitmap = imageProxy.toBitmap()

        // Verarbeite das Bild - jetzt mit nur einer optimierten Version
        val processedBitmap = ImageProcessingUtils.createProcessedVersionsWithNames(bitmap)

        // Job für die Überwachung und Zeitbegrenzung
        var timeoutJob: Job? = null
        val foundValidTime = AtomicBoolean(false)

        timeoutJob = coroutineScope.launch {
            // Timeout nach 300ms (reduziert von 500ms, da wir nur eine Verarbeitung haben)
            delay(300)
            if (!foundValidTime.get() && isActive) {
                Log.d("RaceTextAnalyzer", "Processing timeout reached, using cached result")
                val cachedTime = getMostFrequentTime()
                if (cachedTime != null) {
                    listener.onTextRecognized(cachedTime)
                    foundValidTime.set(true)
                }
                // ImageProxy schließen
                imageProxy.close()
                processingInProgress.set(false)
            }
        }

        // OCR auf dem verarbeiteten Bild durchführen
        coroutineScope.launch {
            try {
                val image = InputImage.fromBitmap(processedBitmap, imageProxy.imageInfo.rotationDegrees)

                recognizer.process(image)
                    .addOnSuccessListener { visionText ->
                        Log.i("RaceTextAnalyzer", "Raw text detected: ${visionText.text}")

                        // Nur weitermachen, wenn noch keine Zeit erkannt wurde
                        if (foundValidTime.get()) {
                            return@addOnSuccessListener
                        }

                        // Text verarbeiten
                        val fullText = StringBuilder()
                        visionText.textBlocks.forEach { block ->
                            block.lines.forEach { line ->
                                fullText.append(line.text).append(" ")
                            }
                        }

                        val correctedText = correctOcrErrors(fullText.toString()).replace(" ", "")
                        var formattedText = correctTimeFormat(correctedText)

                        // 1. Suche nach vollständigem Zeitmuster
                        var bestMatch = findTimePattern(formattedText)

                        // 2. Wenn keine vollständige Zeit gefunden, versuche partielle Zeitformate zu erkennen
                        if (bestMatch.isEmpty()) {
                            bestMatch = findPartialTimePattern(correctedText)
                        }

                        // 3. Wenn eine Zeit gefunden wurde
                        if (bestMatch.isNotEmpty()) {
                            addTimeToCache(bestMatch)

                            // Nur weitermachen, wenn wir noch keine Zeit zurückgegeben haben
                            if (!foundValidTime.getAndSet(true)) {
                                Log.d("RaceTextAnalyzer", "Found valid time: $bestMatch")
                                listener.onTextRecognized(bestMatch)

                                // Timeout-Job abbrechen
                                timeoutJob?.cancel()

                                // Ressourcen freigeben
                                imageProxy.close()
                                processingInProgress.set(false)
                                return@addOnSuccessListener
                            }
                        } else {
                            // Wenn keine Zeit erkannt wurde, prüfe den Cache
                            val cachedTime = getMostFrequentTime()
                            if (cachedTime != null && !foundValidTime.getAndSet(true)) {
                                listener.onTextRecognized(cachedTime)
                            } else if (!foundValidTime.getAndSet(true)) {
                                listener.onTextRecognized("")
                            }

                            // Timeout-Job abbrechen
                            timeoutJob?.cancel()

                            // Ressourcen freigeben
                            imageProxy.close()
                            processingInProgress.set(false)
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.e("RaceTextAnalyzer", "Text recognition failed", e)

                        // Wenn keine Zeit erkannt wurde, prüfe den Cache
                        if (!foundValidTime.getAndSet(true)) {
                            val cachedTime = getMostFrequentTime()
                            if (cachedTime != null) {
                                listener.onTextRecognized(cachedTime)
                            } else {
                                listener.onTextRecognized("")
                            }
                        }

                        // Ressourcen freigeben
                        imageProxy.close()
                        processingInProgress.set(false)
                    }
            } catch (e: Exception) {
                Log.e("RaceTextAnalyzer", "Error processing image", e)

                if (!foundValidTime.getAndSet(true)) {
                    val cachedTime = getMostFrequentTime()
                    if (cachedTime != null) {
                        listener.onTextRecognized(cachedTime)
                    } else {
                        listener.onTextRecognized("")
                    }
                }

                // Ressourcen freigeben
                imageProxy.close()
                processingInProgress.set(false)
            }
        }
    }

    // Vereinfachte Cache-Funktionen für die letzten erkannten Zeiten
    private fun addTimeToCache(time: String) {
        // Füge die Zeit vorne in die Liste ein
        lastDetectedTimes.add(0, time)

        // Halte die Liste auf maximaler Größe
        if (lastDetectedTimes.size > maxCacheSize) {
            lastDetectedTimes.removeAt(lastDetectedTimes.size - 1)
        }
    }

    private fun getMostFrequentTime(): String? {
        if (lastDetectedTimes.isEmpty()) return null

        // Vereinfachte Implementierung: Gib die zuletzt erkannte Zeit zurück
        // Für eine robustere Implementierung könnte man hier die häufigste Zeit bestimmen
        return lastDetectedTimes.firstOrNull()
    }

    // Die restlichen Methoden bleiben unverändert
    private fun findTimePattern(text: String): String {
        // Flexible Regex-Muster für Zeitformate
        val patterns = listOf(
            Regex("(\\d+):(\\d{2})\\.(\\d{3})"),  // Vollständiges Format m:ss.fff
            Regex("(\\d+):(\\d{2})\\.(\\d{2})(\\d?)"), // Format mit optionaler letzter Ziffer
            Regex("(\\d+)[:\\.]\\d{2}[\\.,]\\d{2,3}") // Allgemeiner Pattern
        )

        for (pattern in patterns) {
            val match = pattern.find(text)
            if (match != null) {
                val timeStr = match.value
                val normalized = normalizeTimeFormat(timeStr)
                if (isValidTimeFormat(normalized)) {
                    return normalized
                }
            }
        }
        return ""
    }

    private fun findPartialTimePattern(text: String): String {
        // Suche nach Teilen einer Zeit, die möglicherweise unvollständig erkannt wurde
        val minutesSecondsPattern = Regex("(\\d+):(\\d{2})[\\.,](\\d{2})")
        val match = minutesSecondsPattern.find(text)

        if (match != null) {
            val minutes = match.groupValues[1]
            val seconds = match.groupValues[2]
            val centiSeconds = match.groupValues[3]

            // Prüfe ob wir im Text nach dem Match noch eine einzelne Ziffer finden können
            val afterMatchText = text.substring(match.range.last + 1).take(5)
            val singleDigitMatch = Regex("\\d").find(afterMatchText)

            // Wenn eine einzelne Ziffer gefunden wurde, füge sie hinzu
            val milliseconds = if (singleDigitMatch != null) {
                "${centiSeconds}${singleDigitMatch.value}"
            } else {
                // Wenn keine Ziffer gefunden, versuche die letzte "1" zu ergänzen,
                // wenn der Kontext darauf hindeutet (bei Mario Kart oft 1 oder 0 am Ende)
                "${centiSeconds}1"
            }

            val reconstructedTime = "$minutes:$seconds.$milliseconds"
            if (isValidTimeFormat(reconstructedTime)) {
                return reconstructedTime
            }
        }
        return ""
    }

    private fun correctOcrErrors(text: String): String {
        return text
            .replace("O", "0").replace("o", "0")
            .replace("S", "5").replace("s", "5")
            .replace("l", "1").replace("I", "1").replace("i", "1").replace("|", "1")
            .replace("Z", "2").replace("z", "2")
            .replace("B", "8").replace("b", "8")
            .replace(",", ".").replace(" ", "")
    }

    private fun normalizeTimeFormat(timeStr: String): String {
        val cleaned = timeStr.replace(",", ".").replace(" ", "")

        // Überprüfe verschiedene Formate und normalisiere sie
        if (!cleaned.contains(":") && cleaned.count { it == '.' } == 2) {
            val parts = cleaned.split(".", limit = 3)
            if (parts.size == 3) {
                return "${parts[0]}:${parts[1]}.${parts[2]}"
            }
        }

        // Wenn nur 2 Nachkommastellen erkannt wurden, füge "1" hinzu (häufiger Fall für Mario Kart)
        val regex = Regex("(\\d+):(\\d{2})\\.(\\d{2})$")
        val match = regex.matchEntire(cleaned)
        if (match != null) {
            val minutes = match.groupValues[1]
            val seconds = match.groupValues[2]
            val centiSeconds = match.groupValues[3]
            // Bei Mario Kart ist die letzte Stelle oft eine 1 (oder häufiger Wert)
            return "$minutes:$seconds.${centiSeconds}1"
        }

        return cleaned
    }

    private fun isValidTimeFormat(timeStr: String): Boolean {
        // Validiert das Format und prüft, ob die Werte im sinnvollen Bereich liegen
        val regex = Regex("(\\d+):(\\d{2})\\.(\\d{2,3})")
        val match = regex.matchEntire(timeStr) ?: return false

        val minutes = match.groupValues[1].toIntOrNull() ?: return false
        val seconds = match.groupValues[2].toIntOrNull() ?: return false
        val millis = match.groupValues[3].toIntOrNull() ?: return false

        // Prüfe auf plausible Werte (Mario Kart Zeiten sind meist unter 10 Minuten)
        return minutes in 0..10 && seconds in 0..59 &&
                (millis in 0..999 || millis in 0..99)
    }

    fun shutdown() {
        coroutineScope.cancel()
    }
}

/**
 * Korrigiert häufige OCR-Fehler bei Zeitformaten, insbesondere fehlende Doppelpunkte
 * Format sollte immer sein: 1 Ziffer, dann ':', dann 2 Ziffern, dann '.', dann 3 Ziffern
 */
fun correctTimeFormat(detectedText: String): String {
    // Zuerst alle Leerzeichen und unerwünschten Zeichen entfernen
    var text = detectedText.replace(" ", "").trim()

    // Typische OCR-Fehler korrigieren (z.B. O->0, l->1)
    text = text.replace("O", "0").replace("o", "0")
        .replace("l", "1").replace("I", "1").replace("i", "1").replace("|", "1")
        .replace(",", ".")

    // Prüfe ob das Format bereits korrekt ist: #:##.###
    val correctPattern = Regex("^\\d:\\d{2}\\.\\d{3}$")
    if (correctPattern.matches(text)) {
        return text
    }

    // Wenn der Text nur Ziffern und einen Punkt enthält (z.B. "124.955")
    val digitsAndDotPattern = Regex("^(\\d+)\\.(\\d+)$")
    val digitsAndDotMatch = digitsAndDotPattern.find(text)

    if (digitsAndDotMatch != null) {
        val beforeDot = digitsAndDotMatch.groupValues[1]
        val afterDot = digitsAndDotMatch.groupValues[2]

        // Wenn vor dem Punkt genau 3 Ziffern stehen, formatiere zu #:##.###
        if (beforeDot.length == 3) {
            val minutes = beforeDot[0].toString()
            val seconds = beforeDot.substring(1, 3)

            // Stellen Sie sicher, dass genau 3 Nachkommastellen vorhanden sind
            val milliseconds = if (afterDot.length >= 3) {
                afterDot.substring(0, 3)
            } else {
                // Wenn weniger als 3 Nachkommastellen, füge 0en hinzu
                afterDot.padEnd(3, '0')
            }

            return "$minutes:$seconds.$milliseconds"
        }
    }

    // Wenn der Text nur Ziffern enthält (z.B. "124955")
    val onlyDigitsPattern = Regex("^(\\d+)$")
    val onlyDigitsMatch = onlyDigitsPattern.find(text)

    if (onlyDigitsMatch != null && text.length >= 6) {
        // Format wäre: 1 Ziffer für Minuten, 2 Ziffern für Sekunden, 3 Ziffern für Millisekunden
        val digits = onlyDigitsMatch.groupValues[1]

        if (digits.length >= 6) {
            val minutes = digits[0].toString()
            val seconds = digits.substring(1, 3)
            val milliseconds = digits.substring(3, min(digits.length, 6))

            return "$minutes:$seconds.$milliseconds"
        }
    }

    // Wenn wir nur eine partielle Zeit haben (z.B. nur "124"), versuchen wir zu rekonstruieren
    if (text.length >= 3 && text.all { it.isDigit() }) {
        // Wenn wir nur Ziffern haben, nehmen wir an:
        // 1. Ziffer = Minuten, nächste 2 = Sekunden, Rest (falls vorhanden) = Millisekunden
        val minutes = text[0].toString()
        val seconds = text.substring(1, min(text.length, 3))
        val milliseconds = if (text.length > 3) {
            text.substring(3, min(text.length, 6))
        } else {
            "000" // Standard-Millisekunden, wenn keine angegeben sind
        }

        return "$minutes:$seconds." + milliseconds.padEnd(3, '0')
    }

    // Wenn nichts passt, gib den Originaltext zurück
    return text
}