package com.example.island_spotter

data class Place(
    val placeName: String = "",
    val description: String = "",
    val image_src: String = "",
    val mapUrl: String = "",
    val district: String = "",
    val fav: Boolean = false,
    val rating: Int = 0,
//    val latitude: Double,  // Add this
//    val longitude: Double   // Add this
)

