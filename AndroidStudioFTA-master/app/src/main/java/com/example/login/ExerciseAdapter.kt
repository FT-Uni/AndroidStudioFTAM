package com.example.login

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ExerciseAdapter(private val exercises: List<Exercise>) :
    RecyclerView.Adapter<ExerciseAdapter.ExerciseDetailViewHolder>() {

    var expandedPosition = -1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseDetailViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_exercise_detail, parent, false)
        return ExerciseDetailViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExerciseDetailViewHolder, position: Int) {
        val exercise = exercises[position]

        holder.textViewExerciseName.text = exercise.name
        holder.textViewMuscleGroup.text = exercise.targetMuscle

        if (exercise.isExpanded) {
            holder.textViewSets.setText(exercise.sets.toString())
            holder.textViewReps.setText(exercise.reps.toString())
            holder.expandDetails()
        } else {
            holder.collapseDetails()
        }

        if (position == expandedPosition) {
            holder.itemView.setBackgroundColor(Color.parseColor("#00FF00"))  // Light green color
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE)
        }

        holder.itemView.setOnClickListener {
            if (expandedPosition >= 0) {
                val prevExpandedPosition = expandedPosition
                exercises[prevExpandedPosition].isExpanded = false
                notifyItemChanged(prevExpandedPosition)
            }

            exercise.isExpanded = !exercise.isExpanded
            expandedPosition = if (exercise.isExpanded) position else -1

            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int = exercises.size

    inner class ExerciseDetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewExerciseName: TextView = itemView.findViewById(R.id.textViewExerciseName)
        val textViewMuscleGroup: TextView = itemView.findViewById(R.id.textViewTargetMuscle)
        val textViewSets: EditText = itemView.findViewById(R.id.editTextSets)
        val textViewReps: EditText = itemView.findViewById(R.id.editTextReps)
        private val btnAdd: Button = itemView.findViewById(R.id.btnAdd)

        fun expandDetails() {
            textViewSets.visibility = View.VISIBLE
            textViewReps.visibility = View.VISIBLE
            btnAdd.visibility = View.VISIBLE
        }

        fun collapseDetails() {
            textViewSets.visibility = View.GONE
            textViewReps.visibility = View.GONE
            btnAdd.visibility = View.GONE
        }
    }
}