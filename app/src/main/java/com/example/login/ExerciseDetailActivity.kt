package com.example.login

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.login.databinding.ActivityExerciseDetailBinding

class ExerciseDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExerciseDetailBinding
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var exerciseAdapter: ExerciseAdapter
    private var exerciseId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseHelper(this)

        // Fetching the list of exercises from the database
        val exercisesList: List<Exercise> = dbHelper.getAllExercises()
        exerciseAdapter = ExerciseAdapter(exercisesList)

        exerciseId = intent.getIntExtra("EXERCISE_ID", 0)

        val exercise = dbHelper.getExerciseById(exerciseId)
        if (exercise != null) {
            binding.textViewExerciseName.text = exercise.name
            binding.textViewTargetMuscle.text = exercise.targetMuscle
        }

        binding.btnAdd.setOnClickListener {
            val sets = binding.editTextSets.text.toString().toIntOrNull() ?: 0
            val reps = binding.editTextReps.text.toString().toIntOrNull() ?: 0

            if (dbHelper.addExerciseDetail(exerciseId, sets, reps)) {
                val updatedExercise = dbHelper.getExerciseById(exerciseId)
                updatedExercise?.let {
                    val position = exercisesList.indexOf(it)
                    if (position != -1) {
                        exerciseAdapter.expandedPosition = position
                        exerciseAdapter.notifyDataSetChanged()
                    }
                }
                Toast.makeText(this, "Exercise added successfully!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to add exercise.", Toast.LENGTH_SHORT).show()
            }

            setResult(Activity.RESULT_OK)
            finish()
        }
    }
}