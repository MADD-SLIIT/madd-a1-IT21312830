package com.example.island_spotter

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.island_spotter.databinding.ActivityEditPlanBinding
import com.google.firebase.database.*

class EditPlanActivity : AppCompatActivity() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var planId: String

    private lateinit var editTextTopic: EditText
    private lateinit var editTextDestination: EditText
    private lateinit var editTextDate: EditText
    private lateinit var checkBoxReminder: CheckBox

    private lateinit var binding: ActivityEditPlanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditPlanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase reference
        databaseReference = FirebaseDatabase.getInstance().reference.child("planner")

        // Retrieve the planId from the intent
        planId = intent.getStringExtra("planId") ?: ""

        // Initialize views
        editTextTopic = findViewById(R.id.editTextTopic)
        editTextDestination = findViewById(R.id.editTextDestination)
        editTextDate = findViewById(R.id.editTextDate)
        checkBoxReminder = findViewById(R.id.checkBoxReminder)

        // Load the existing plan data
        loadPlanData()

        // Save button click listener
        findViewById<Button>(R.id.buttonSave).setOnClickListener {
            savePlanChanges()
        }
    }

    private fun loadPlanData() {
        // Retrieve the plan from Firebase based on the planId
        databaseReference.child(planId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val plannerData = snapshot.getValue(PlannerData::class.java)
                if (plannerData != null) {
                    // Populate the form fields with existing data
                    editTextTopic.setText(plannerData.topic)
                    editTextDestination.setText(plannerData.destination)
                    editTextDate.setText(plannerData.date)
                    checkBoxReminder.isChecked = plannerData.sendReminder ?: false
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@EditPlanActivity, "Failed to load data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun savePlanChanges() {
        // Get updated values from the form fields
        val updatedTopic = editTextTopic.text.toString().trim()
        val updatedDestination = editTextDestination.text.toString().trim()
        val updatedDate = editTextDate.text.toString().trim()
        val updatedSendReminder = checkBoxReminder.isChecked

        // Create a map of the updated values
        val updatedPlan = mapOf(
            "topic" to updatedTopic,
            "destination" to updatedDestination,
            "date" to updatedDate,
            "sendReminder" to updatedSendReminder
        )

        // Update the plan data in Firebase
        databaseReference.child(planId).updateChildren(updatedPlan).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Plan updated successfully", Toast.LENGTH_SHORT).show()
                finish() // Close the activity after saving
            } else {
                Toast.makeText(this, "Failed to update plan", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
