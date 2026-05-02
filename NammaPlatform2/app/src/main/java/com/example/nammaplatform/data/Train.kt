package com.example.nammaplatform.data

import com.example.nammaplatform.R

data class Train(
    val trainNumber: String,
    val trainName: String,
    val platform: String,
    val departureTime: String,
    val coaches: List<Coach>
)

data class Coach(
    val coachId: String,
    val coachType: CoachType,
    val displayName: String,
    val position: Int // Position on platform (percentage)
)

enum class CoachType(val colorRes: Int, val displayName: String) {
    ENGINE(R.color.coach_engine, "Engine"),
    GENERAL(R.color.coach_general, "General"),
    SLEEPER(R.color.coach_sleeper, "Sleeper"),
    AC(R.color.coach_ac, "AC"),
    LADIES(R.color.coach_ladies, "Ladies"),
    GUARD(R.color.coach_guard, "Guard")
}

data class Station(
    val stationCode: String,
    val stationName: String,
    val kannada: String,
    val region: String
)
