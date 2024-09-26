package com.example.island_spotter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase

class PlannerAdapter(private val plannerList: List<PlannerData>) :
    RecyclerView.Adapter<PlannerAdapter.PlannerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlannerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.planner_list_layout, parent, false)
        return PlannerViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlannerViewHolder, position: Int) {
        val plannerData = plannerList[position]

        holder.textViewTopic.text = plannerData.topic

        // Delete button click listener
        holder.deleteButton.setOnClickListener {
            plannerData.id?.let { id ->
                deletePlan(id, holder.itemView)  // Pass the itemView here
            }
        }

        // Edit button click listener
        holder.editButton.setOnClickListener {
            // Pass the plan ID to the EditPlanActivity
            val context = holder.itemView.context
            val intent = Intent(context, EditPlanActivity::class.java)
            intent.putExtra("planId", plannerData.id)  // Pass planId to the EditPlanActivity
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = plannerList.size

    class PlannerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewTopic: TextView = itemView.findViewById(R.id.textViewTopic)
        val deleteButton: ImageButton = itemView.findViewById(R.id.deleteButton)
        val editButton: ImageButton = itemView.findViewById(R.id.editButton)  // Declare the editButton here
    }

    // Function to delete the plan from Firebase
    private fun deletePlan(planId: String, itemView: View) {
        val databaseReference = FirebaseDatabase.getInstance().reference.child("planner").child(planId)
        databaseReference.removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(
                    itemView.context,  // Use the view context
                    "Planner entry deleted successfully",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    itemView.context,  // Use the view context
                    "Failed to delete planner entry",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
