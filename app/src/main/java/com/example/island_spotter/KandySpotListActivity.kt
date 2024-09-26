package com.example.island_spotter

import PlaceAdapter
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.island_spotter.databinding.ActivityKandySpotListBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class KandySpotListActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var adapter: PlaceAdapter
    private lateinit var placeList: MutableList<Place>
    private lateinit var binding: ActivityKandySpotListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize binding
        binding = ActivityKandySpotListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Back button
        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        // Initialize binding
        binding = ActivityKandySpotListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize place list
        placeList = mutableListOf()

        // Initialize Firebase Database reference
        database = FirebaseDatabase.getInstance().getReference("Places")

        // Setup RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = PlaceAdapter(this, placeList) { place ->
            // Handle item click, e.g., open detailed view
        }
        binding.recyclerView.adapter = adapter

        // Fetch places in the Kandy district from Firebase
        fetchKandyPlaces()
    }

    private fun fetchKandyPlaces() {
        // Attach a listener to the "places" node
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Clear the current list to avoid duplication
                placeList.clear()

                // Iterate through all place entries
                for (placeSnapshot in dataSnapshot.children) {
                    val place = placeSnapshot.getValue(Place::class.java)
                    // Check if the place belongs to the Kandy district
                    if (place?.district == "Kandy") {
                        placeList.add(place)
                    }
                }

                // Notify adapter that data has changed
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Log error or handle failure
                Log.e("Firebase", "Error fetching data", databaseError.toException())
            }
        })
    }
}
