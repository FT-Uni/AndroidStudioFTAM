package com.example.login

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ExerciseListActivity : AppCompatActivity() {

    private lateinit var exerciseAdapter: ExerciseAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise_list)

        dbHelper = DatabaseHelper(this)

        val exercisesList = dbHelper.getAllExercises()
        val btnDone = findViewById<TextView>(R.id.btnDone)

        recyclerView = findViewById(R.id.recyclerViewExerciseList)
        exerciseAdapter = ExerciseAdapter(exercisesList)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@ExerciseListActivity)
            adapter = exerciseAdapter
        }

        btnDone.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
