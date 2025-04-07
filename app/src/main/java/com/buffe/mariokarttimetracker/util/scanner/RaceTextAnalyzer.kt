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
import java.util.concurrent.atomic.AtomicInteger

class RaceTextAnalyzer(private val listener: TextResultListener) : ImageAnalysis.Analyzer {
    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    private val timeCache = TimeCache(5) // Speichert die letzten 5 erkannten Zeiten
    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private val processingInProgress = AtomicBoolean(false)

    // Priorität der Bildverarbeitungsmethoden (höherer Wert = höhere Priorität)
    private val processingPriorities = mapOf(
        "enhanceContrast" to 3,
        "isolateYellowText" to 5,    // Gelber Text hat höchste Priorität (typisch für Mario Kart UI)
        "binarized" to 2,
        "timeRegion" to 4
    )

    // Frühe Erkennungsschwelle - nach x erfolgreichen Erkennungen wird die Verarbeitung beendet
    private val earlyRecognitionThreshold = 2
    private val successfulRecognitions = AtomicInteger(0)

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        // Wenn bereits eine Verarbeitung läuft, dieses Bild überspringen
        if (processingInProgress.getAndSet(true)) {
            imageProxy.close()
            return
        }

        successfulRecognitions.set(0)
        val bitmap = imageProxy.toBitmap()

        // Verarbeite das Bild mit mehreren Methoden, aber priorisiere sie
        val processedImagesMap = ImageProcessingUtils.createProcessedVersionsWithNames(bitmap)

        // Sortiere nach Priorität
        val sortedImages = processedImagesMap.entries.sortedByDescending {
            processingPriorities[it.key] ?: 0
        }

        val totalImages = sortedImages.size
        val processedCount = AtomicInteger(0)
        val foundValidTime = AtomicBoolean(false)

        // Job für die Überwachung und Zeitbegrenzung
        var timeoutJob: Job? = null

        // Liste von Jobs für die Parallelverarbeitung
        val jobs = mutableListOf<Job>()

        timeoutJob = coroutineScope.launch {
            // Timeout nach 500ms - liefere bestes Ergebnis aus dem Cache
            delay(500)
            if (!foundValidTime.get() && !isActive.not()) {
                Log.d("RaceTextAnalyzer", "Processing timeout reached, using cached result")
                val cachedTime = timeCache.getMostFrequentTime()
                if (cachedTime != null) {
                    listener.onTextRecognized(cachedTime)
                    foundValidTime.set(true)
                }
                // Alle laufenden Jobs abbrechen
                jobs.forEach { it.cancel() }
                // ImageProxy schließen
                imageProxy.close()
                processingInProgress.set(false)
            }
        }

        // Für jede Bildvariante OCR parallel durchführen
        for ((methodName, processedBitmap) in sortedImages) {
            val job = coroutineScope.launch {
                try {
                    val image = InputImage.fromBitmap(processedBitmap, imageProxy.imageInfo.rotationDegrees)

                    // Wenn bereits eine gültige Zeit gefunden wurde und wir das Limit erreicht haben, abbrechen
                    if (foundValidTime.get() && successfulRecognitions.get() >= earlyRecognitionThreshold) {
                        return@launch
                    }

                    recognizer.process(image)
                        .addOnSuccessListener { visionText ->
                            val currentCount = processedCount.incrementAndGet()
                            Log.d("RaceTextAnalyzer", "Raw text detected ($methodName ${currentCount}/${totalImages}): ${visionText.text}")

                            // Wenn bereits eine gültige Zeit gefunden wurde und wir den Schwellenwert erreicht haben, nichts tun
                            if (foundValidTime.get() && successfulRecognitions.get() >= earlyRecognitionThreshold) {
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

                            // 1. Suche nach vollständigem Zeitmuster
                            var bestMatch = findTimePattern(correctedText)

                            // 2. Wenn keine vollständige Zeit gefunden, versuche partielle Zeitformate zu erkennen
                            if (bestMatch.isEmpty()) {
                                bestMatch = findPartialTimePattern(correctedText)
                            }

                            // 3. Wenn eine Zeit gefunden wurde
                            if (bestMatch.isNotEmpty()) {
                                timeCache.addTime(bestMatch)
                                successfulRecognitions.incrementAndGet()

                                // Wenn wir genug Bestätigungen haben oder dies die höchste Prioritätsmethode war
                                if (successfulRecognitions.get() >= earlyRecognitionThreshold ||
                                    processingPriorities[methodName] == sortedImages.first().let { processingPriorities[it.key] }) {

                                    // Nur weitermachen, wenn wir noch keine Zeit zurückgegeben haben
                                    if (!foundValidTime.getAndSet(true)) {
                                        Log.d("RaceTextAnalyzer", "Found valid time early: $bestMatch (method: $methodName)")
                                        listener.onTextRecognized(bestMatch)

                                        // Timeout-Job abbrechen
                                        timeoutJob?.cancel()

                                        // Ressourcen freigeben
                                        imageProxy.close()
                                        processingInProgress.set(false)
                                        return@addOnSuccessListener
                                    }
                                }
                            }

                            // Prüfen, ob alle Bilder verarbeitet wurden
                            if (currentCount == totalImages && !foundValidTime.get()) {
                                val cachedTime = timeCache.getMostFrequentTime()
                                if (cachedTime != null) {
                                    foundValidTime.set(true)
                                    listener.onTextRecognized(cachedTime)
                                } else {
                                    foundValidTime.set(true)
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
                            val currentCount = processedCount.incrementAndGet()
                            Log.e("RaceTextAnalyzer", "Text recognition failed for method $methodName", e)

                            // Prüfen, ob alle Bilder verarbeitet wurden
                            if (currentCount == totalImages && !foundValidTime.get()) {
                                val cachedTime = timeCache.getMostFrequentTime()
                                if (cachedTime != null) {
                                    foundValidTime.set(true)
                                    listener.onTextRecognized(cachedTime)
                                } else {
                                    foundValidTime.set(true)
                                    listener.onTextRecognized("")
                                }

                                // Ressourcen freigeben
                                imageProxy.close()
                                processingInProgress.set(false)
                            }
                        }
                } catch (e: Exception) {
                    Log.e("RaceTextAnalyzer", "Error processing image with method $methodName", e)
                    processedCount.incrementAndGet()
                }
            }
            jobs.add(job)
        }
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

// Hilfklasse zum Zwischenspeichern der erkannten Zeiten
class TimeCache(private val maxSize: Int) {
    private val times = LinkedHashMap<String, Int>()

    fun addTime(time: String) {
        times[time] = (times[time] ?: 0) + 1

        // Cache-Größe begrenzen
        if (times.size > maxSize) {
            val oldestEntry = times.entries.first()
            times.remove(oldestEntry.key)
        }
    }

    fun getMostFrequentTime(): String? {
        return times.entries.maxByOrNull { it.value }?.key
    }
}