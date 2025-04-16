package com.buffe.mariokarttimetracker.util.scanner

import android.graphics.Bitmap
import androidx.core.graphics.createBitmap
import androidx.core.graphics.scale
import org.opencv.android.Utils
import org.opencv.core.Mat

object ImageProcessingUtils {

    /**
     * Erstellt mehrere verarbeitete Versionen des Bildes mit Namen für bessere Identifikation
     */
    fun createProcessedVersionsWithNames(inputBitmap: Bitmap):  Bitmap {
        // Optimierung: Reduziere die Bildgröße für schnellere Verarbeitung
        val scaledBitmap = scaleBitmapDown(inputBitmap, 720)
 // 1. Original-Bild mit verbessertem Kontrast (schnell und oft effektiv)
        return enhanceContrast(scaledBitmap)
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
            bitmap.scale(scaledWidth, scaledHeight)
        } else {
            bitmap
        }
    }

    private fun enhanceContrast(bitmap: Bitmap): Bitmap {
        val src = Mat()
        Utils.bitmapToMat(bitmap, src)

        // Erhöhe den Kontrast stark für bessere Texterkennung
        val enhanced = Mat()
        src.convertTo(enhanced, -1, 2.0, -100.0)  // Alpha=2.0 (Kontrast), Beta=-100 (Helligkeit)

        // Konvertiere zurück zu Bitmap
        val resultBitmap = createBitmap(bitmap.width, bitmap.height, bitmap.config)
        Utils.matToBitmap(enhanced, resultBitmap)

        // Ressourcen freigeben
        src.release()
        enhanced.release()

        return resultBitmap
    }
}