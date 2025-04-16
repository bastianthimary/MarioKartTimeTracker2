package com.buffe.mariokarttimetracker.ui.summary
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi

import androidx.appcompat.app.AppCompatActivity
import com.buffe.mariokarttimetracker.data.model.RaceTime
import com.buffe.mariokarttimetracker.databinding.ActivitySummaryBinding
import com.buffe.mariokarttimetracker.ui.main.MainActivity
import com.buffe.mariokarttimetracker.data.model.Run
import com.buffe.mariokarttimetracker.data.model.RunStatistic
import com.buffe.mariokarttimetracker.util.TimeFormatUtils


class SummaryActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySummaryBinding

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySummaryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val runOfSummary = intent.getSerializableExtra("Run", Run::class.java)
        val statisticRunOfSummary = intent.getSerializableExtra("RunStatistic", RunStatistic::class.java)
        val bestRunTime = intent.getSerializableExtra("BestRunTime", RaceTime::class.java)?: RaceTime("0:00.000")
        val averageRunTime = intent.getSerializableExtra("AverageRunTime", RaceTime::class.java)?: RaceTime("0:00.000")
        val rankOf = intent.getStringExtra("RankOf")

       if (runOfSummary==null){
           Toast.makeText(this,"Fehler beim Laden des Runs",Toast.LENGTH_SHORT).show()
           finish()
           return
       }else{
           val totalTime=TimeFormatUtils.calcRunsTotalTime(runOfSummary)
           val totalRecord=statisticRunOfSummary?.recordCount

           binding.tvTotalTime.text = "Gesamtzeit: $totalTime"
           binding.tvRank.text="Platzierung:"+rankOf
           binding.tvTotalRecords.text = "Anzahl Gebrochene Rekorde: : $totalRecord"

           binding.tvSumBestTime.text="Bester Durchgang: "+TimeFormatUtils.hourFormatTime(bestRunTime?.timeMillis
               ?: 0)+"\n" +
           "    ("+TimeFormatUtils.signedFormatTime(determineTimeDifference(runOfSummary.calculateTotalTime(),bestRunTime))+")"
            binding.tvSumAverageTime.text="Durschnittszeit Durchgang: "+ TimeFormatUtils.hourFormatTime(averageRunTime?.timeMillis
                ?:0)+"\n" +
                    "    ("+TimeFormatUtils.signedFormatTime(determineTimeDifference(runOfSummary.calculateTotalTime(),averageRunTime))+")"

         // Bereite die Kind-Daten vor: Für jeden Race im Run holen wir den Tracknamen und die formatierte Rennzeit
           val childData: List<Pair<String, String>> = runOfSummary.races.map { race ->
               race.track.displayName to TimeFormatUtils.formatTime(race.raceTime.timeMillis)+" ("+ statisticRunOfSummary!!.trackPlacements[race.track.id]+")"
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

    private fun determineTimeDifference(
        currentTime:RaceTime,
        otherTime: RaceTime
    ): Long {
       return otherTime.timeMillis.minus(currentTime.timeMillis)
    }

}