package com.example.island_spotter

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.island_spotter.databinding.ActivitySigninBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class SigninActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySigninBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("Users")

        // Handle sign-in
        binding.buttonSignIn.setOnClickListener {
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                signinUser(email, password)
            } else {
                Toast.makeText(this, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()
            }
        }

        // Handle click, open sign-up activity
        binding.startSignupBtn.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
            finish()
        }
    }

    private fun signinUser(email: String, password: String) {
        // Query Firebase Realtime Database for the user with the provided email
        databaseReference.orderByChild("email").equalTo(email)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (userSnapshot in dataSnapshot.children) {
                            val userData = userSnapshot.getValue(UserData::class.java)

                            if (userData != null && userData.password == password) {
                                // Login successful
                                Toast.makeText(
                                    this@SigninActivity,
                                    "Login Successful",
                                    Toast.LENGTH_SHORT
                                ).show()

                                startActivity(Intent(this@SigninActivity, MainActivity::class.java))
                                finish()
                                return
                            }
                        }
                        // If email exists but password doesn't match
                        Toast.makeText(
                            this@SigninActivity,
                            "Login Failed: Incorrect password",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        // If email doesn't exist
                        Toast.makeText(
                            this@SigninActivity,
                            "Login Failed: User not found",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(
                        this@SigninActivity,
                        "Database Error: ${databaseError.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

//    // Define UserData class to map user details from Firebase Realtime Database
//    data class UserData(
//        val name: String = "",
//        val email: String = "",
//        val contact: String = "",
//        val password: String = ""
//    )
}
