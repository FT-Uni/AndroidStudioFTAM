package com.example.login

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.Data
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class RegistrationActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        dbHelper = DatabaseHelper(this)

        val editTextNewUsername = findViewById<EditText>(R.id.editTextNewUsername)
        val editTextNewPassword = findViewById<EditText>(R.id.editTextNewPassword)
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val textViewSignIn = findViewById<TextView>(R.id.textViewSignIn)

        btnRegister.setOnClickListener {
            val newUsername = editTextNewUsername.text.toString()
            val newPassword = editTextNewPassword.text.toString()

            if (isValidRegistration(newUsername, newPassword)) {
                if (dbHelper.addUser(newUsername, newPassword)){
                    // Registration successful
                    Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, WeightActivity::class.java)
                    intent.putExtra("USERNAME", newUsername)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Failed to register. Please try again.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Invalid registration. Please try again.", Toast.LENGTH_SHORT).show()
            }
        }

        textViewSignIn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun isValidRegistration(newUsername: String, newPassword: String): Boolean {
        return newUsername.isNotEmpty() && newPassword.isNotEmpty()
    }


}

