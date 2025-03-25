package com.buffe.mariokarttimetracker.ui.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.buffe.mariokarttimetracker.databinding.ActivityRaceBinding
import com.buffe.mariokarttimetracker.util.TimeFormatUtils

class RaceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRaceBinding
    private val raceTimes = mutableListOf<RaceTime>()
    private var isFormatting = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRaceBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnAddLap.setOnClickListener { addLapTime() }
        updateStatistics()

        binding.etLapTime.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
            override fun afterTextChanged(s: Editable?) {
                if (isFormatting) return
                isFormatting = true

                val input = s.toString().filter { it.isDigit() }
                val formatted = formatTimeInput(input)
                if (formatted != s.toString()) {
                    binding.etLapTime.setText(formatted)
                    binding.etLapTime.setSelection(formatted.length)
                }
                isFormatting = false
            }
        })
    }

    private fun addLapTime() {
        val input = binding.etLapTime.text.toString()
        if (TimeFormatUtils.isValidTimeFormat(input)) {
            val lapTimeMillis = TimeFormatUtils.parseTime(input)?: run {
                Log.e("RaceActivity", "Fehler beim Parsen der Zeit: $input")
                return
            }
            val newRaceTime = RaceTime(lapTimeMillis) // Neues LapTime-Objekt

            raceTimes.add(newRaceTime) // Fügt das Objekt zur Liste hinzu

            updateStatistics() // Aktualisiert Durchschnitts- und Bestzeiten
            binding.etLapTime.text?.clear() // Löscht die Eingabe
        } else {
            Toast.makeText(this, "Bitte Zeit im Format m:ss.fff eingeben", Toast.LENGTH_SHORT).show()
        }
    }


    private fun updateStatistics() {
        if (raceTimes.isEmpty()) {
            binding.tvAverageRunDiff.text = "Durchschnittszeit: -"
            binding.tvAverageRunDiff.text = "Beste Zeit: -"
            return
        }

      /*  val timesInMillis =
            raceTimes.map { it.timeInMillis } // Extrahiert die Zeiten aus den LapTime-Objekten

        val averageTime = timesInMillis.average().toLong()
        val bestTime = timesInMillis.minOrNull() ?: 0L

        binding.tvAverageRunDiff.text = "Durchschnittszeit: ${TimeFormatUtils.formatTime(averageTime)}"
        binding.tvBestRunDiff.text = "Beste Zeit: ${TimeFormatUtils.formatTime(bestTime)}"
        */
    }


    private fun formatTimeInput(digits: String): String {
        if (digits.isEmpty()) return ""
        return when (digits.length) {
            in 1..3 -> "0:00." + digits.padStart(3, '0')
            in 4..5 -> "0:" + digits.substring(0, digits.length - 3).padStart(2, '0') + "." + digits.takeLast(3)
            else -> {
                val minutes = digits.substring(0, digits.length - 5)
                val seconds = digits.substring(digits.length - 5, digits.length - 3)
                val millis = digits.takeLast(3)
                "$minutes:$seconds.$millis"
            }
        }
    }
}
