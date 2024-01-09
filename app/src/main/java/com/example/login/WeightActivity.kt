package com.example.login
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast

class WeightActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var username: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weight)

        dbHelper = DatabaseHelper(this)

        username = intent.getStringExtra("USERNAME") ?: ""

        val btnSubmit = findViewById<Button>(R.id.btnSubmit)
        val editTextWeight = findViewById<EditText>(R.id.editTextWeight)
        val editTextHeight = findViewById<EditText>(R.id.editTextHeight)
        val editTextAge = findViewById<EditText>(R.id.editTextAge)



        btnSubmit.setOnClickListener {
            val weight = editTextWeight.text.toString().toDoubleOrNull() ?: 0.0
            val height = editTextHeight.text.toString().toDoubleOrNull() ?: 0.0
            val age = editTextAge.text.toString().toIntOrNull() ?: 0

            if (dbHelper.addProfile(username, weight, height, age)) {
                Toast.makeText(this, "Information saved successfully!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Failed to save information. Please try again.", Toast.LENGTH_SHORT).show()
            }

        }
    }
}


