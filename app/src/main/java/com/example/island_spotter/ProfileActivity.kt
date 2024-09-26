package com.example.island_spotter

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.island_spotter.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        if (currentUser != null) {
            // Get the current user's ID
            val userId = currentUser.uid

            // Initialize the Firebase Database reference
            database = FirebaseDatabase.getInstance().getReference("Users").child(userId)

            // Fetch user data
            fetchUserData(userId)
        } else {
            Toast.makeText(this, "No user logged in", Toast.LENGTH_SHORT).show()
        }

        // Back button
        binding.backButton.setOnClickListener {
            onBackPressed()
        }
    }

    private fun fetchUserData(userId: String) {
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(UserData::class.java)
                if (user != null) {
                    // Update UI with user details
                    binding.userName.text = user.name
                    binding.userEmail.text = user.email
                    binding.userPhone.text = user.contact
                    binding.userLocation.text = "Colombo, Western Province, Sri Lanka"  // Hardcoded for now
                } else {
                    Toast.makeText(this@ProfileActivity, "User data not found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ProfileActivity, "Failed to load user data", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
