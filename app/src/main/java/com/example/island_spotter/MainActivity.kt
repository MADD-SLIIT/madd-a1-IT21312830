package com.example.island_spotter

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.island_spotter.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        // Initialize the RecyclerView and set the adapter
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view_horizontal)
//        val horizontalAdapter = HorizontalAdapter(districts)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
//        recyclerView.adapter = horizontalAdapter



        // redirect to kandy list
        binding.spotImage1.setOnClickListener {
            startActivity(Intent(this, KandySpotListActivity::class.java))
        }



        // Set up BottomNavigationView
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav_bar)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.main -> {
                    // Handle Home action
                    true
                }
                R.id.favorites -> {
                    // Navigate to ListActivity
                    startActivity(Intent(this, FavoritesActivity::class.java))
                    true
                }
                R.id.plans -> {
                    // Navigate to ListActivity
                    startActivity(Intent(this, PlannerListActivity::class.java))
                    true
                }
                R.id.profile -> {
                    // Handle Profile action
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }
}