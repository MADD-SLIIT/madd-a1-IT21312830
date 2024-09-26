package com.example.island_spotter

data class PlannerData(
    val id: String? = null,  // Unique ID for the planner item
    val userId: String? = null,
    val topic: String? = null,
    val destination: String? = null,
    val date: String? = null,
    val sendReminder: Boolean? = null
)
