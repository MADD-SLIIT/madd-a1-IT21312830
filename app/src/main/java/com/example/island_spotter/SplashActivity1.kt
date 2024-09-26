package com.example.island_spotter

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.island_spotter.databinding.ActivitySplash1Binding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SplashActivity1 : AppCompatActivity() {

    private lateinit var binding: ActivitySplash1Binding
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplash1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference()

        // Call addPlace function when needed
        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
//        sharedPreferences.edit().putBoolean("isFirstRun", true).apply()

        val isFirstRun = sharedPreferences.getBoolean("isFirstRun", true)






        if (isFirstRun) {
            addPlace(
                id = "place1",
                placeName = "Kandy Lake",
                description = "Beautiful lake in the heart of Kandy",
                image_src = "kandy_lake",
                mapUrl = "https://maps.google.com/kandylake",
                district = "Kandy"
            )

            // Kandy Tea Museum
            addPlace(
                id = "place2",
                placeName = "Kandy Tea Museum",
                description = "A museum showcasing the history and process of tea production in Sri Lanka.",
                image_src = "ceylon_tea_museum_image",  // Image stored in drawable
                mapUrl = "https://maps.google.com/kandyteamuseum",
                district = "Kandy"
            )

            // Shangri-La Hambantota
            addPlace(
                id = "place3",
                placeName = "Shangri-La Hambantota",
                description = "A luxurious resort located along the southern coast of Sri Lanka.",
                image_src = "shangrila_hambantota",  // Image stored in drawable
                mapUrl = "https://maps.google.com/shangrilahambantota",
                district = "Hambantota"
            )

            // Hambantota Botanical Garden
            addPlace(
                id = "place4",
                placeName = "Hambantota Botanical Garden",
                description = "A beautiful garden with a variety of plants and landscapes in the Hambantota district.",
                image_src = "hambantota_botanical_garden",  // Image stored in drawable
                mapUrl = "https://maps.google.com/hambantotabotanicalgarden",
                district = "Hambantota"
            )


            addPlace(
                id = "place5",
                placeName = "Sigiriya Rock",
                description = "Ancient rock fortress in the Matale District",
                image_src = "sigiriya_rock",
                mapUrl = "https://maps.google.com/sigiriya",
                district = "Matale"
            )

            // Nelum Kuluna (Lotus Tower)
            addPlace(
                id = "place6",
                placeName = "Nelum Kuluna (Lotus Tower)",
                description = "A symbol of Colombo, the tallest tower in South Asia, offering panoramic views of the city.",
                image_src = "colombo",  // Image stored in drawable
                mapUrl = "https://maps.google.com/lotustowercolombo",
                district = "Colombo"
            )

// Victoria Park (Viharamahadevi Park)
            addPlace(
                id = "place7",
                placeName = "Victoria Park (Viharamahadevi Park)",
                description = "A large urban park with a variety of trees, open spaces, and a relaxing atmosphere in the heart of Colombo.",
                image_src = "viharamahadevi_park",  // Image stored in drawable
                mapUrl = "https://maps.google.com/victoriaparkcolombo",
                district = "Colombo"
            )


            sharedPreferences.edit().putBoolean("isFirstRun", false).apply()
        }


        //delay for 2 seconds before navigating to SplashActivity2
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, SplashActivity2::class.java)
            startActivity(intent)
            finish() // Close SplashActivity1 so it can't be returned to
        }, 2000) // 2000 milliseconds = 2 seconds
    }

    private fun addPlace(
        id: String,
        placeName: String,
        description: String,
        image_src: String,  // Change from imageUrl to imageResourceName
        mapUrl: String,
        district: String
    ) {
        val placeRef = databaseReference.child("Places").child(id)

        placeRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    val placeData = HashMap<String, Any>()
                    placeData["placeName"] = placeName
                    placeData["description"] = description
                    placeData["image_src"] = image_src  // Storing resource name
                    placeData["mapUrl"] = mapUrl
                    placeData["district"] = district
                    placeData["fav"] = false
                    placeData["rating"] = 0

                    placeRef.setValue(placeData)
                    Toast.makeText(this@SplashActivity1, "$placeName added to database", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@SplashActivity1, "$placeName already exists in the database", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@SplashActivity1, "Database Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}