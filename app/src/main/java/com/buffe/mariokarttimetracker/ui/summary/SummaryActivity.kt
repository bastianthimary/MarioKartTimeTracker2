package com.buffe.mariokarttimetracker.ui.summary
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi

import androidx.appcompat.app.AppCompatActivity
import com.buffe.mariokarttimetracker.data.model.Track
import com.buffe.mariokarttimetracker.databinding.ActivitySummaryBinding
import com.buffe.mariokarttimetracker.ui.main.MainActivity
import com.buffe.mariokarttimetracker.ui.main.RaceTime
import com.buffe.mariokarttimetracker.ui.main.Run
import com.buffe.mariokarttimetracker.util.TimeFormatUtils


class SummaryActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySummaryBinding

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySummaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val runOfSummary = intent.getSerializableExtra("Run", Run::class.java)
       if (runOfSummary==null){
           Toast.makeText(this,"Fehler beim Laden des Runs",Toast.LENGTH_SHORT).show()
           finish()
           return
       }else{
           val totalTime=TimeFormatUtils.calcRunsTotalTime(runOfSummary)
           binding.tvCompletionMessage.text = "Durchlauf Abgeschlossen!"
           binding.tvTotalTime.text = "Gesamtzeit: $totalTime"

         // Bereite die Kind-Daten vor: Für jeden Race im Run holen wir den Tracknamen und die formatierte Rennzeit
           val childData: List<Pair<String, String>> = runOfSummary.races.map { race ->
               race.track.displayName to TimeFormatUtils.formatTime(race.raceTime.timeMillis)
           }
           // Setze den Adapter (nur 1 Gruppe: "Rennzeiten Übersicht")
           binding.expListView.setAdapter(SummaryExpandableListAdapter(this, childData))

       }

        binding.btnToMain.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(intent)
            finish()
        }
    }

}