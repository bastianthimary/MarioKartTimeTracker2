package com.buffe.mariokarttimetracker.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.buffe.mariokarttimetracker.databinding.ActivityRaceBinding
import com.buffe.mariokarttimetracker.ui.summary.SummaryActivity
import com.buffe.mariokarttimetracker.util.TimeFormatUtils
import com.buffe.mariokarttimetracker.util.TimeFormatUtils.isValidTimeFormat
import com.buffe.mariokarttimetracker.util.scanner.RaceTextAnalyzer
import com.buffe.mariokarttimetracker.util.scanner.TextResultListener
import java.util.concurrent.Executors
import androidx.camera.camera2.interop.Camera2CameraInfo
import android.hardware.camera2.CameraCharacteristics
import android.os.Build
import android.widget.SeekBar
import androidx.annotation.OptIn
import androidx.annotation.RequiresApi
import androidx.camera.camera2.interop.ExperimentalCamera2Interop
import com.buffe.mariokarttimetracker.data.manager.RunManager
import com.buffe.mariokarttimetracker.data.manager.StatisticManager
import com.buffe.mariokarttimetracker.data.model.RaceTime


class RaceActivity : AppCompatActivity(), TextResultListener {

    private lateinit var binding: ActivityRaceBinding
    private var isFormatting = false
    private var cameraProvider: ProcessCameraProvider? = null // Instanzvariable zum Speichern des Providers
    private var camera: Camera? = null
    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private var currentZoomRatio = 1.0f
    private val runManager = RunManager()
    private val statisticManager=StatisticManager()
    // Zoom-Konfiguration
    private var minZoomRatio = 1.0f
    private var maxZoomRatio = 1.0f
    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRaceBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Zoom-Gesten-Initialisierung
        scaleGestureDetector = ScaleGestureDetector(this, ScaleListener())
        binding.previewView.setOnTouchListener { _, event ->
            scaleGestureDetector.onTouchEvent(event)
            true
        }
        binding.zoomSlider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val zoomRatio = minZoomRatio + (maxZoomRatio - minZoomRatio) * (progress / 100f)
                camera?.cameraControl?.setZoomRatio(zoomRatio)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
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
      val totalTimeBeforeRace= TimeFormatUtils.parseTime(statisticManager.getCurrentRunTotalTimeFormatted(runManager.getCurrentRun()))
        val yourTimeNowInMs=totalTimeBeforeRace+TimeFormatUtils.parseTime(binding.etRaceTime.text.toString())
        return TimeFormatUtils.formatTime(yourTimeNowInMs)
    }

    private fun updateUI() {
        if(runManager.isFinished()){
            val intent = Intent(this, SummaryActivity::class.java).apply {
                putExtra("Run", runManager.getCurrentRun())
            }
            startActivity(intent)
            finish()
        }
        // Hole das aktuelle Rennen vom RunManager
        val currentTrack = runManager.getCurrentTrack()
        supportActionBar?.title = currentTrack.displayName
        // Aktualisiere aggregierte Zeiten (Gesamtzeit bis zur aktuellen Strecke, beste und durchschnittliche Zeit)
        binding.tvRunTotalTime.text = "Gesamtzeit: ${statisticManager.getCurrentRunTotalTimeFormatted(runManager.getCurrentRun())}"
        binding.tvBestTime.text = "Beste Zeit: ${statisticManager.getCurrentBestTotalTimeFormatted(runManager.getCurrentTrack().id)}"
        binding.tvAverageTime.text = "Durchschnitt: ${statisticManager.getAverageTotalTimeFormattedBeforeCurrent(runManager.getCurrentTrack().id)}"

        // Leere das Eingabefeld, falls vorhanden
        binding.etRaceTime.text?.clear()
        //Aktualisiere Historische Strecken Zeiten Durchschnitt und Bestzeiten
        binding.tvAverageRaceTime.text="Beste strecken Zeit: ${statisticManager.getCurrentBestTrackTimeFormatted(runManager.getCurrentTrack().id)}"
        binding.tvBestRaceTime.text="Durchschnitt Streckenzeit: ${statisticManager.getCurrentAverageTrackTimeFormatted(runManager.getCurrentTrack().id)}"

        // Aktivieren/Deaktivieren des "Nächstes Rennen"-Buttons
        binding.btnNextRace.isEnabled =
            false // Beispielweise erst aktivieren, wenn eine gültige Eingabe vorliegt.
    }


    @RequiresApi(Build.VERSION_CODES.R)
    private fun startCamera() {
        binding.previewView.visibility = View.VISIBLE
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            // CameraProvider erhalten
            cameraProvider = cameraProviderFuture.get()
           bindCameraUseCases()

        }, ContextCompat.getMainExecutor(this))
    }

    @RequiresApi(Build.VERSION_CODES.R)
    @OptIn(ExperimentalCamera2Interop::class)
    private fun bindCameraUseCases() {
        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(binding.previewView.surfaceProvider)
        }

        val imageAnalysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
            .also {
                it.setAnalyzer(Executors.newSingleThreadExecutor(), RaceTextAnalyzer(this))
            }

        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        try {
            cameraProvider?.unbindAll()
            camera = cameraProvider?.bindToLifecycle(
                this,
                cameraSelector,
                preview,
                imageAnalysis
            )

            // Zoom-Bereich initialisieren
            camera?.cameraInfo?.let { cameraInfo ->
                // Konvertiere CameraX-Info zu Camera2-Charakteristiken
                val camera2Info = Camera2CameraInfo.from(cameraInfo)

                // Holen der Camera2-Charakteristiken
                val characteristics = camera2Info.getCameraCharacteristic(CameraCharacteristics.CONTROL_ZOOM_RATIO_RANGE)

                // Für Android API 30+ (präziser Zoom)
                val zoomRatioRange = characteristics?.let {
                    camera2Info.getCameraCharacteristic(CameraCharacteristics.CONTROL_ZOOM_RATIO_RANGE)
                }

                // Für ältere Android Versionen (digitaler Zoom)
                if (zoomRatioRange == null) {
                    val maxDigitalZoom = camera2Info.getCameraCharacteristic(CameraCharacteristics.SCALER_AVAILABLE_MAX_DIGITAL_ZOOM) ?: 1.0f
                    minZoomRatio = 1.0f
                    maxZoomRatio = maxDigitalZoom
                } else {
                    minZoomRatio = zoomRatioRange.lower
                    maxZoomRatio = zoomRatioRange.upper
                }
            }
        } catch (exc: Exception) {
            Log.e("RaceActivity", "Use case binding failed", exc)
        }
    }

    // Diese Methode stoppt die Kamera
    private fun stopCamera() {
        cameraProvider?.unbindAll()
        cameraProvider = null
        binding.previewView.visibility = View.GONE
    }
    // Zoom-Gesten-Listener
    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            val scaleFactor = detector.scaleFactor
            currentZoomRatio = currentZoomRatio * scaleFactor
            currentZoomRatio = currentZoomRatio.coerceIn(minZoomRatio, maxZoomRatio)
            camera?.cameraControl?.setZoomRatio(currentZoomRatio)
            return true
        }
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
