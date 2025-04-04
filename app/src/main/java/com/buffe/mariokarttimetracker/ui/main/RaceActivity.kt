package com.buffe.mariokarttimetracker.ui.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.buffe.mariokarttimetracker.databinding.ActivityRaceBinding
import com.buffe.mariokarttimetracker.util.TimeFormatUtils
import com.buffe.mariokarttimetracker.util.TimeFormatUtils.isValidTimeFormat
import com.buffe.mariokarttimetracker.util.scanner.TextResultListener
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import com.buffe.mariokarttimetracker.util.scanner.RaceTextAnalyzer
import kotlinx.metadata.Visibility


class RaceActivity : AppCompatActivity(), TextResultListener {

    private lateinit var binding: ActivityRaceBinding
    private var isFormatting = false
    private var cameraProvider: ProcessCameraProvider? = null // Instanzvariable zum Speichern des Providers
    private val runManager = RunManager()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRaceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Prüfe Berechtigungen und starte Kameravorschau

        // Lade den aktuellen Run; falls keiner offen ist, wird automatisch ein neuer gestartet.
        runManager.initializeOrContinueCurrentRun()

        // Update der UI anhand des aktuellen Runs und des nächsten Rennens
        updateUI()

        binding.etRaceTime.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                if (isFormatting) return
                isFormatting = true

                val input = s.toString().filter { it.isDigit() }
                val formatted = TimeFormatUtils.formatTimeInput(input)
                if (formatted != s.toString()) {
                    binding.etRaceTime.setText(formatted)
                    binding.etRaceTime.setSelection(formatted.length)
                }
                if(isValidTimeFormat(formatted)){
                    binding.tvRunTotalTime.text="Gesamtzeit: ${calcYourTimeNow()}"
                    binding.btnNextRace.isEnabled=true
                }
                isFormatting = false
            }
        })
        // Button für (zukünftige) Kamera-Erfassung – hier nur als Platzhalter
        binding.btnCameraCapture.setOnClickListener {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
            }
        }

        // Button zum Speichern des aktuellen Rennens und zum Wechseln zum nächsten Rennen
        binding.btnNextRace.setOnClickListener {
            val input = binding.etRaceTime.text.toString()
            if (isValidTimeFormat(input)) {
                val raceTime = RaceTime.fromString(input)
                runManager.addCurrentRace(raceTime)
                updateUI() // UI aktualisieren: nächstes Rennen laden, Statistiken aktualisieren
            } else {
                Toast.makeText(this, "Das Zeitformat ist nicht Valide", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun calcYourTimeNow(): String {
      val totalTimeBeforeRace= TimeFormatUtils.parseTime(runManager.getCurrentRunTotalTimeFormatted())
        val yourTimeNowInMs=totalTimeBeforeRace+TimeFormatUtils.parseTime(binding.etRaceTime.text.toString())
        return TimeFormatUtils.formatTime(yourTimeNowInMs)
    }

    private fun updateUI() {
        if(runManager.isFinished()){
            //TODO: Run Beenden wenn Fertig
        }
        // Hole das aktuelle Rennen vom RunManager
        val currentTrack = runManager.getCurrentTrack()
        supportActionBar?.title = currentTrack.displayName
        // Aktualisiere aggregierte Zeiten (Gesamtzeit bis zur aktuellen Strecke, beste und durchschnittliche Zeit)
        binding.tvRunTotalTime.text = "Gesamtzeit: ${runManager.getCurrentRunTotalTimeFormatted()}"
        binding.tvBestTime.text = "Beste Zeit: ${runManager.getCurrentBestTotalTimeFormatted()}"
        binding.tvAverageTime.text = "Durchschnitt: ${runManager.getCurrentAverageTotalTimeFormatted()}"

        // Leere das Eingabefeld, falls vorhanden
        binding.etRaceTime.text?.clear()
        //Aktualisiere Historische Strecken Zeiten Durchschnitt und Bestzeiten
        binding.tvAverageRaceTime.text="Beste strecken Zeit: ${runManager.getCurrentBestTrackTimeFormatted()}"
        binding.tvBestRaceTime.text="Durchschnitt Streckenzeit: ${runManager.getCurrentAverageTrackTimeFormatted()}"

        // Aktivieren/Deaktivieren des "Nächstes Rennen"-Buttons
        binding.btnNextRace.isEnabled =
            false // Beispielweise erst aktivieren, wenn eine gültige Eingabe vorliegt.
    }


    private fun startCamera() {
        binding.previewView.visibility = View.VISIBLE
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            // CameraProvider erhalten
            cameraProvider = cameraProviderFuture.get()
            // Preview Use-Case erstellen und binden
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.previewView.surfaceProvider)
            }
            // ImageAnalysis-UseCase einrichten
            val imageAnalysis = ImageAnalysis.Builder()
                .build()
                .also {
                    it.setAnalyzer(ContextCompat.getMainExecutor(this), RaceTextAnalyzer(this))
                }

            // Wähle die Rückkamera aus
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Vorherige UseCases binden (falls nötig)
                cameraProvider?.unbindAll()

                // Binde den ImageAnalysis-UseCase
                cameraProvider?.bindToLifecycle(this, cameraSelector,preview, imageAnalysis)
            } catch (exc: Exception) {
                Log.e("RaceActivity", "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(this))
    }


    // Diese Methode stoppt die Kamera
    private fun stopCamera() {
        cameraProvider?.unbindAll()
        cameraProvider = null
        binding.previewView.visibility = View.GONE
    }

    // Implementierung des Callback-Interfaces: hier den erkannten Text in das Textfeld eintragen
    override fun onTextRecognized(result: String) {
        runOnUiThread {
            if (result.isNotEmpty()) {
                binding.etRaceTime.setText(result)
                stopCamera()
            }
        }
    }

    // Berechtigungsprüfung
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}
