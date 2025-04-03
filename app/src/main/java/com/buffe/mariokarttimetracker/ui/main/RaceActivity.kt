package com.buffe.mariokarttimetracker.ui.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.buffe.mariokarttimetracker.databinding.ActivityRaceBinding
import com.buffe.mariokarttimetracker.util.TimeFormatUtils
import com.buffe.mariokarttimetracker.util.TimeFormatUtils.isValidTimeFormat

class RaceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRaceBinding
    private var isFormatting = false
    private val runManager = RunManager()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRaceBinding.inflate(layoutInflater)
        setContentView(binding.root)


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
                    binding.btnNextRace.isEnabled=true
                }
                isFormatting = false
            }
        })
        // Button für (zukünftige) Kamera-Erfassung – hier nur als Platzhalter
        binding.btnCameraCapture.setOnClickListener {
            Toast.makeText(this, "Funktion noch nicht Implementiert", Toast.LENGTH_SHORT).show()
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

    private fun updateUI() {
        if(runManager.isFinished()){

        }
        // Hole das aktuelle Rennen vom RunManager
        val currentTrack = runManager.getCurrentTrack()
        supportActionBar?.title = currentTrack.displayName
        // Aktualisiere aggregierte Zeiten (Gesamtzeit bis zur aktuellen Strecke, beste und durchschnittliche Zeit)
        binding.tvRunTotalTime.text = "Gesamtzeit: ${runManager.getCurrentRunTotalTimeFormatted()}"
        binding.tvBestTime.text = "Beste Zeit: ${runManager.getCurrentBestTimeFormatted()}"
        binding.tvAverageTime.text = "Durchschnitt: ${runManager.getCurrentAverageTimeFormatted()}"

        // Leere das Eingabefeld, falls vorhanden
        binding.etRaceTime.text?.clear()

        // Aktivieren/Deaktivieren des "Nächstes Rennen"-Buttons
        binding.btnNextRace.isEnabled =
            false // Beispielweise erst aktivieren, wenn eine gültige Eingabe vorliegt.
    }


}
