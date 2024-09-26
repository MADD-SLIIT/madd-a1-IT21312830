package com.example.island_spotter

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.island_spotter.databinding.ActivityAddPlanBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddPlanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddPlanBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPlanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase instance
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("planner")

        // Set onClickListener for the Save button
        binding.buttonSave.setOnClickListener {

            val userId = FirebaseAuth.getInstance().currentUser?.uid

            val topic = binding.editTextTopic.text.toString().trim()
            val destination = binding.editTextDestinations.text.toString().trim()
            val date = binding.editTextDate.text.toString().trim()
            val sendReminder = binding.checkBoxSendReminder.isChecked

            // Validate inputs before saving
            if (topic.isEmpty() || destination.isEmpty() || date.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                val plannerData = PlannerData(
                    userId = userId,
                    topic = topic,
                    destination = destination,
                    date = date,
                    sendReminder = sendReminder
                )
                Log.d("AddPlanActivity", "Saving plan")
                savePlan(plannerData)
            }
        }
    }

    private fun savePlan(plannerData: PlannerData) {
        val id = databaseReference.push().key
        if (id != null) {
            val updatedPlannerData = plannerData.copy(id = id)  // Save the ID
            databaseReference.child(id).setValue(updatedPlannerData).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Planner entry added successfully", Toast.LENGTH_SHORT).show()
                    finish() // Optional: Close the activity after saving
                } else {
                    Toast.makeText(this, "Failed to add planner entry", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "Error generating entry ID", Toast.LENGTH_SHORT).show()
        }
    }
}
