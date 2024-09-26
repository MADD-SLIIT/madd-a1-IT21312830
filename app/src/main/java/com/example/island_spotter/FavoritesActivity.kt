package com.example.island_spotter

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.island_spotter.databinding.ActivityFavoritesBinding
import com.google.firebase.database.*

class FavoritesActivity : AppCompatActivity() {
    // AKD 2024
    private lateinit var favoritesRecyclerView: RecyclerView
    private lateinit var favoritesAdapter: FavoritesAdapter
    private val favoritesList = mutableListOf<FavoritePlace>()
    private lateinit var databaseRef: DatabaseReference
    private lateinit var binding: ActivityFavoritesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        favoritesRecyclerView = findViewById(R.id.recyclerView)
        favoritesRecyclerView.layoutManager = LinearLayoutManager(this)

        // Back button
        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        // Initialize Firebase Database reference
        val userData = getUserData() // Retrieve user data
        val userId = userData.id // Use actual user ID

        databaseRef = FirebaseDatabase.getInstance().getReference("Favorites").child(userId)
        fetchFavorites()

        favoritesAdapter = FavoritesAdapter(this, favoritesList) { favoritePlace ->
            val intent = Intent(this, PlaceDetailActivity::class.java).apply {
                putExtra("PLACE_NAME", favoritePlace.name)
                putExtra("PLACE_DESCRIPTION", favoritePlace.description)
                putExtra("PLACE_IMAGE_SRC", favoritePlace.imageSrc)
                putExtra("PLACE_MAP_URL", favoritePlace.mapUrl)
            }
            startActivity(intent)
        }

        favoritesRecyclerView.adapter = favoritesAdapter
    }

    private fun fetchFavorites() {
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                favoritesList.clear()
                for (placeSnapshot in snapshot.children) {
                    val favoritePlace = placeSnapshot.getValue(FavoritePlace::class.java)
                    if (favoritePlace != null) {
                        favoritesList.add(favoritePlace)
                    }
                }
                favoritesAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FavoritesActivity", "Error fetching data: ${error.message}")
            }
        })
    }

    private fun getUserData(): UserData {
        val sharedPreferences = getSharedPreferences("your_pref_name", MODE_PRIVATE)
        val id = sharedPreferences.getString("userId", "") ?: ""
        val name = sharedPreferences.getString("userName", "") ?: ""
        val email = sharedPreferences.getString("userEmail", "") ?: ""
        val contact = sharedPreferences.getString("userContact", "") ?: ""

        return UserData(id, name, email, contact)
    }
}
