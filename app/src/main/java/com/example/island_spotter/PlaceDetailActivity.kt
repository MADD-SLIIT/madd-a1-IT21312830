package com.example.island_spotter

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.*

class PlaceDetailActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var detailImage: ImageView
    private lateinit var detailName: TextView
    private lateinit var detailDescription: TextView
    private lateinit var detailMapUrl: TextView
    private lateinit var addToFavBtn: ImageButton
    private lateinit var databaseRef: DatabaseReference

    private lateinit var mapFragment: SupportMapFragment
    private lateinit var googleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_detail)

        // Initialize views
        detailImage = findViewById(R.id.detail_image)
        detailName = findViewById(R.id.detail_name)
        detailDescription = findViewById(R.id.detail_description)
        detailMapUrl = findViewById(R.id.detail_map_url)
        addToFavBtn = findViewById(R.id.addToFavBtn)

        // Retrieve data from intent
        val placeName = intent.getStringExtra("PLACE_NAME")
        val placeDescription = intent.getStringExtra("PLACE_DESCRIPTION")
        val placeImageSrc = intent.getStringExtra("PLACE_IMAGE_SRC")
        val placeMapUrl = intent.getStringExtra("PLACE_MAP_URL")

        // Set data to views
        detailName.text = placeName
        detailDescription.text = placeDescription
        val imageResId = resources.getIdentifier(placeImageSrc, "drawable", packageName)
        detailImage.setImageResource(imageResId)

        // Set up map URL click
        detailMapUrl.text = placeMapUrl
        detailMapUrl.setOnClickListener {
            // Open the map URL in a web browser
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(placeMapUrl))
            startActivity(intent)
        }

        // Initialize Google Map
        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Initialize Firebase Database reference
        databaseRef = FirebaseDatabase.getInstance().getReference("Places")

        // Fetch the location from Firebase based on place name
        if (placeName != null) {
            fetchLocationFromDatabase(placeName)
        }

        // Handle Add to Favorites button
        addToFavBtn.setOnClickListener {
            // Add functionality to save this spot as a favorite
            val userData = getUserData()
            addToFavorites(userData, placeName, placeDescription, placeImageSrc, placeMapUrl)
        }

    }

    private fun getUserData(): UserData {
        val sharedPreferences = getSharedPreferences("your_pref_name", MODE_PRIVATE)
        val id = sharedPreferences.getString("userId", "") ?: ""
        val name = sharedPreferences.getString("userName", "") ?: ""
        val email = sharedPreferences.getString("userEmail", "") ?: ""
        val contact = sharedPreferences.getString("userContact", "") ?: ""

        return UserData(id, name, email, contact)
    }


    // Fetch latitude and longitude from Firebase
    private fun fetchLocationFromDatabase(placeName: String) {
        val query = databaseRef.orderByChild("name").equalTo(placeName)

        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (placeSnapshot in snapshot.children) {
                    val latitude = placeSnapshot.child("latitude").getValue(Double::class.java)
                    val longitude = placeSnapshot.child("longitude").getValue(Double::class.java)

                    if (latitude != null && longitude != null) {
                        showLocationOnMap(LatLng(latitude, longitude), placeName)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }
        })
    }

    // Display location on the map
    private fun showLocationOnMap(latLng: LatLng, placeName: String) {
        if (::googleMap.isInitialized) {
            googleMap.addMarker(MarkerOptions().position(latLng).title(placeName))
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
        } else {
            Log.e("PlaceDetailActivity", "GoogleMap not initialized")
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
    }

//    private fun addToFavorites(userData: UserData,placeName: String?, placeDescription: String?, placeImageSrc: String?, placeMapUrl: String?) {
//        // Logic to add the spot to Firebase or local storage as a favorite
//        // This can involve saving to Firebase Database or SharedPreferences
//        val userId = "CURRENT_USER_ID" // Replace with actual user ID logic
//        val databaseRef = FirebaseDatabase.getInstance().getReference("Favorites").child(userId)
//        val favoritePlace = mapOf(
//            "name" to placeName,
//            "description" to placeDescription,
//            "imageSrc" to placeImageSrc,
//            "mapUrl" to placeMapUrl,
//
//            // Add other necessary fields
//        )
//        databaseRef.push().setValue(favoritePlace)
//    }

    private fun addToFavorites(userData: UserData, placeName: String?, placeDescription: String?, placeImageSrc: String?, placeMapUrl: String?) {
        // Use the actual user ID from userData
        val userId = userData.id

        // Reference the correct path in Firebase
        val databaseRef = FirebaseDatabase.getInstance().getReference("Favorites")

        // Create a map for the favorite place
        val favoritePlace = mapOf(
            "name" to placeName,
            "description" to placeDescription,
            "imageSrc" to placeImageSrc,
            "mapUrl" to placeMapUrl
        )

        // Push the new favorite place to the user's favorites list
        databaseRef.push().setValue(favoritePlace).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("Favorites", "Favorite place added successfully.")
            } else {
                Log.e("Favorites", "Error adding favorite place: ${task.exception?.message}")
            }
        }
    }

}
