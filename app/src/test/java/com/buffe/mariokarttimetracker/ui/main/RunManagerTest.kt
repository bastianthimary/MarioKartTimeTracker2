package com.buffe.mariokarttimetracker.ui.main
import com.buffe.mariokarttimetracker.data.model.Track
import com.buffe.mariokarttimetracker.data.repository.TrackRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class RunManagerTest {

 private lateinit var runManager: RunManager
 private lateinit var mockTrackRepository: TrackRepository

 @Before
 fun setUp() {
  // Mock-Objekt für das Repository
  mockTrackRepository = mockk()



  // Beispielstrecken für den Test
  val testTracks = listOf(
   Track(1, "Mario Circuit"),
   Track(2, "Rainbow Road"),
   Track(3, "Toad Harbor"),
   Track(4, "Yoshi Valley")
  )

  // Mock-Verhalten definieren
  every { mockTrackRepository.getAllTracks() } returns testTracks

  // RunManager initialisieren
  runManager = RunManager(mockTrackRepository)
 }

 @Test
 fun `test new run starts with first track`() {
  val newRun = runManager.startNewRun()
  val firstTrack = runManager.getCurrentTrack()

  assertNotNull("Run sollte nicht null sein", newRun)
  assertEquals("Die erste Strecke sollte 'Mario Circuit' sein", "Mario Circuit", firstTrack?.name)
 }

 @Test
 fun `test race can be added and next track is correct`() {
  runManager.startNewRun()
  runManager.addCurrentRace(RaceTime("1:30.000")) // 1:30 Minuten
  runManager.moveToNextRace()
  val nextTrack = runManager.getCurrentTrack()
  assertEquals("Die nächste Strecke sollte 'Rainbow Road' sein", "Rainbow Road", nextTrack?.name)
 }
}

