package com.buffe.mariokarttimetracker.util.scanner

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import org.opencv.android.Utils
import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object ImageProcessingUtils {

    /**
     * Erstellt mehrere verarbeitete Versionen des Bildes mit Namen für bessere Identifikation
     */
    fun createProcessedVersionsWithNames(inputBitmap: Bitmap): Map<String, Bitmap> {
        val resultImagesMap = mutableMapOf<String, Bitmap>()

        // Optimierung: Reduziere die Bildgröße für schnellere Verarbeitung
        val scaledBitmap = scaleBitmapDown(inputBitmap, 720)

        // 1. Original-Bild mit verbessertem Kontrast (schnell und oft effektiv)
        resultImagesMap["enhanceContrast"] = enhanceContrast(scaledBitmap)

        // 2. Gelber Text isoliert (sehr effektiv für Mario Kart UI)
        resultImagesMap["isolateYellowText"] = isolateYellowText(scaledBitmap)

        // 3. Binarisierte Version für bessere Kantenerkennung
        resultImagesMap["binarized"] = createBinarizedVersion(scaledBitmap)

        // 4. Originalversion mit höherem Kontrast fokussiert auf die wichtige Region
        val timeRegion = findTimeDisplayRegion(scaledBitmap)
        if (timeRegion != null) {
            resultImagesMap["timeRegion"] = extractAndEnhanceRegion(scaledBitmap, timeRegion)
        }

        return resultImagesMap
    }

    /**
     * Skaliert das Bitmap auf eine maximale Größe, behält das Seitenverhältnis bei
     */
    private fun scaleBitmapDown(bitmap: Bitmap, maxDimension: Int): Bitmap {
        val originalWidth = bitmap.width
        val originalHeight = bitmap.height

        var scaledWidth = originalWidth
        var scaledHeight = originalHeight

        if (originalWidth > maxDimension || originalHeight > maxDimension) {
            val ratio = if (originalWidth > originalHeight) {
                maxDimension.toFloat() / originalWidth.toFloat()
            } else {
                maxDimension.toFloat() / originalHeight.toFloat()
            }

            scaledWidth = (originalWidth * ratio).toInt()
            scaledHeight = (originalHeight * ratio).toInt()
        }

        return if (scaledWidth != originalWidth || scaledHeight != originalHeight) {
            Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true)
        } else {
            bitmap
        }
    }

    private fun isolateYellowText(bitmap: Bitmap): Bitmap {
        val src = Mat()
        Utils.bitmapToMat(bitmap, src)

        // Optimierung: Verwende RGB direkt für schnellere Verarbeitung
        // Gelber Text in Mario Kart hat oft ein bestimmtes RGB-Profil
        val channels = ArrayList<Mat>()
        Core.split(src, channels)

        // Einfache Erkennung für gelben Text: R und G hoch, B niedrig
        val yellowMask = Mat()
        Core.inRange(
            src,
            Scalar(0.0, 150.0, 150.0), // B, G, R (niedrig, hoch, hoch)
            Scalar(100.0, 255.0, 255.0), // B, G, R
            yellowMask
        )

        // Vergrößere die Maske leicht
        val kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, Size(2.0, 2.0))
        Imgproc.dilate(yellowMask, yellowMask, kernel)

        // Erstelle ein weißes Bild mit schwarzem Hintergrund für besseren Kontrast
        val result = Mat.zeros(src.size(), CvType.CV_8UC3)
        result.setTo(Scalar(255.0, 255.0, 255.0)) // Weißer Text
        src.copyTo(result, yellowMask)

        // Konvertiere zurück zu Bitmap
        val resultBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config)
        Utils.matToBitmap(result, resultBitmap)

        // Ressourcen freigeben
        src.release()
        yellowMask.release()
        result.release()
        channels.forEach { it.release() }

        return resultBitmap
    }

    private fun createBinarizedVersion(bitmap: Bitmap): Bitmap {
        val src = Mat()
        Utils.bitmapToMat(bitmap, src)

        // Konvertiere zu Graustufen
        val gray = Mat()
        Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY)

        // Optimierung: Einfache Schwellwertmethode ist schneller
        val binary = Mat()
        Imgproc.threshold(gray, binary, 0.0, 255.0, Imgproc.THRESH_BINARY + Imgproc.THRESH_OTSU)

        // Konvertiere zurück zu Bitmap
        val resultBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config)
        Utils.matToBitmap(binary, resultBitmap)

        // Ressourcen freigeben
        src.release()
        gray.release()
        binary.release()

        return resultBitmap
    }

    private fun findTimeDisplayRegion(bitmap: Bitmap): Rect? {
        // Optimierung: Nutze ein schnelles Heuristik-Verfahren
        // In Mario Kart befindet sich die Zeit oft im oberen Drittel der rechten Bildschirmhälfte

        val width = bitmap.width
        val height = bitmap.height

        // Bildschirmbereich, in dem sich typischerweise die Zeit befindet
        val roi = Rect(
            width / 2,  // Start in der rechten Hälfte
            0,          // Oberer Rand
            width / 2,  // Rechte Hälfte des Bildschirms
            height / 3  // Oberes Drittel
        )

        return roi
    }

    private fun extractAndEnhanceRegion(bitmap: Bitmap, rect: Rect): Bitmap {
        // Sicherstellen, dass das Rechteck innerhalb der Bitmap-Grenzen ist
        val x = rect.x.coerceIn(0, bitmap.width - 1)
        val y = rect.y.coerceIn(0, bitmap.height - 1)
        val width = rect.width.coerceAtMost(bitmap.width - x)
        val height = rect.height.coerceAtMost(bitmap.height - y)

        if (width <= 0 || height <= 0) {
            // Fallback, falls Grenzen ungültig sind
            return enhanceContrast(bitmap)
        }

        // Schneide das Rechteck aus
        val regionBitmap = Bitmap.createBitmap(bitmap, x, y, width, height)

        // Verbessere den Kontrast stark für bessere OCR
        return enhanceContrast(regionBitmap)
    }

    private fun enhanceContrast(bitmap: Bitmap): Bitmap {
        val src = Mat()
        Utils.bitmapToMat(bitmap, src)

        // Erhöhe den Kontrast stark für bessere Texterkennung
        val enhanced = Mat()
        src.convertTo(enhanced, -1, 2.0, -100.0)  // Alpha=2.0 (Kontrast), Beta=-100 (Helligkeit)

        // Konvertiere zurück zu Bitmap
        val resultBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config)
        Utils.matToBitmap(enhanced, resultBitmap)

        // Ressourcen freigeben
        src.release()
        enhanced.release()

        return resultBitmap
    }
}