package com.buffe.mariokarttimetracker.util.scanner
import android.annotation.SuppressLint
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
class RaceTextAnalyzer (private val listener: TextResultListener) : ImageAnalysis.Analyzer  {
    // TextRecognizer initialisieren (für lateinische Schriften)
    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        // Erstelle ein InputImage aus dem ImageProxy
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            // ML Kit Text Recognition starten
            recognizer.process(image)
                .addOnSuccessListener { visionText ->
                    // Hier kannst du den erkannten Text filtern, z. B. nach einem Regex, der das Format "m:ss.fff" erkennt.
                    // Beispiel: Suche nach dem ersten Vorkommen eines Musters wie "1:24.955"
                    val regex = Regex("\\d+:\\d{2}\\.\\d{3}")
                    val match = regex.find(visionText.text)
                    if (match != null) {
                        listener.onTextRecognized(match.value)
                    } else {
                        // Kein gültiges Zeitformat gefunden
                        listener.onTextRecognized("")
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("RaceTextAnalyzer", "Text recognition failed", e)
                    listener.onTextRecognized("")
                }
                .addOnCompleteListener {
                    // Wichtig: ImageProxy muss geschlossen werden, damit CameraX mit dem nächsten Bild fortfahren kann.
                    imageProxy.close()
                }
        } else {
            imageProxy.close()
        }
    }
}