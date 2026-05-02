package com.example.nammaplatform.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.nammaplatform.data.Coach
import com.example.nammaplatform.data.CoachType
import com.example.nammaplatform.data.Station
import com.example.nammaplatform.data.Train
import org.json.JSONArray

class RailwayViewModel : ViewModel() {

    private var allTrains = listOf<Train>()
    private var stations = listOf<Station>()

    fun loadTrainData(context: Context): List<Train> {
        val inputStream = context.assets.open("trains.json")
        val json = inputStream.bufferedReader().use { it.readText() }

        val jsonArray = JSONArray(json)
        val trains = mutableListOf<Train>()

        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)

            val trainNumber = obj.optString("trainNumber", "")
            val trainName = obj.getString("trainName")
            val platform = obj.getString("platform")
            val departureTime = obj.optString("departureTime", "")

            val coachArray = obj.getJSONArray("coaches")
            val coaches = mutableListOf<Coach>()

            for (j in 0 until coachArray.length()) {
                val coachStr = coachArray.getString(j)
                val coachType = parseCoachType(coachStr)
                val position = calculateCoachPosition(j, coachArray.length())

                coaches.add(
                    Coach(
                        coachId = "$trainNumber$coachStr",
                        coachType = coachType,
                        displayName = coachStr,
                        position = position
                    )
                )
            }

            trains.add(
                Train(
                    trainNumber = trainNumber,
                    trainName = trainName,
                    platform = platform,
                    departureTime = departureTime,
                    coaches = coaches
                )
            )
        }

        allTrains = trains
        return trains
    }

    fun getNextTrains(count: Int = 3): List<Train> {
        return allTrains.take(count)
    }

    fun getNextTrain(): Train? {
        return allTrains.firstOrNull()
    }

    fun loadStations(): List<Station> {
        if (stations.isNotEmpty()) return stations

        val sampleStations = listOf(
            Station("TK", "ತುಮಕೂರು", "Tumkur", "SWR - Karnataka"),
            Station("HAS", "ಹಾಸನ", "Hassan", "SWR - Karnataka"),
            Station("MYS", "ಮೈಸೂರು", "Mysuru", "SWR - Karnataka"),
            Station("MNDYA", "ಮಂಡ್ಯ", "Mandya", "SWR - Karnataka"),
            Station("ASK", "ಅರಸಿಕೇರೆ", "Arsikere", "SWR - Karnataka")
        )

        stations = sampleStations
        return stations
    }

    private fun parseCoachType(coachName: String): CoachType {
        return when {
            coachName.contains("Engine", ignoreCase = true) -> CoachType.ENGINE
            coachName.contains("General", ignoreCase = true) -> CoachType.GENERAL
            coachName.contains("Ladies", ignoreCase = true) -> CoachType.LADIES
            coachName.contains("Sleeper", ignoreCase = true) || coachName.contains("S", ignoreCase = true) -> CoachType.SLEEPER
            coachName.contains("AC", ignoreCase = true) -> CoachType.AC
            coachName.contains("Guard", ignoreCase = true) -> CoachType.GUARD
            else -> CoachType.GENERAL
        }
    }

    private fun calculateCoachPosition(index: Int, total: Int): Int {
        // Calculate position as percentage along platform
        return if (total > 0) ((index + 1) * 100) / total else 0
    }
}

