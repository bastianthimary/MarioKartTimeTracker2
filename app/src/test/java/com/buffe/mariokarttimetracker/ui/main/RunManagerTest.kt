package com.buffe.mariokarttimetracker.ui.main

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class RunManagerTest {

 private lateinit var runManager: RunManager


 @Before
 fun setUp() {
  // RunManager initialisieren
  runManager = RunManager()
 }

 //@Test
 fun `test new run starts with first track`() {
  val newRun = runManager.startNewRun()
  val firstTrack = runManager.getCurrentTrack()

  assertNotNull("Run sollte nicht null sein", newRun)
  assertEquals("Die erste Strecke sollte 'Mario Circuit' sein", "Mario Circuit", firstTrack?.name)
 }

// @Test
 fun `test race can be added and next track is correct`() {
  runManager.startNewRun()
  runManager.addCurrentRace(RaceTime("1:30.000")) // 1:30 Minuten
  runManager.moveToNextRace()
  val nextTrack = runManager.getCurrentTrack()
  assertEquals("Die nächste Strecke sollte 'Rainbow Road' sein", "Rainbow Road", nextTrack?.name)
 }
}

