package com.example.island_spotter

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.island_spotter.databinding.ActivityPlannerListBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class PlannerListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlannerListBinding
    private lateinit var databaseReference: DatabaseReference
    private lateinit var plannerList: MutableList<PlannerData>
    private lateinit var adapter: PlannerAdapter
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlannerListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        plannerList = mutableListOf()
        adapter = PlannerAdapter(plannerList)
        binding.recyclerView.adapter = adapter

        // Initialize Firebase references
        userId = FirebaseAuth.getInstance().currentUser?.uid.toString()
        databaseReference = FirebaseDatabase.getInstance().reference.child("planner")

        fetchPlannerData()

        // Back button
        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        // FAB to add new plans
        binding.fab.setOnClickListener {
            startActivity(Intent(this, AddPlanActivity::class.java))
        }
    }


    private fun fetchPlannerData() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                plannerList.clear()
                if (snapshot.exists()) {
                    for (planSnapshot in snapshot.children) {
                        val plannerData = planSnapshot.getValue(PlannerData::class.java)
                        Log.d("PlannerListActivity", "Plan data: $plannerData")  // Log the data
                        if (plannerData != null) {
                            plannerList.add(plannerData)
                        }
                    }
                    adapter.notifyDataSetChanged()
                } else {
                    Log.d("PlannerListActivity", "No data found")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("PlannerListActivity", "Error fetching plans: ${error.message}")
            }
        })
    }


}
