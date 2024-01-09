package com.example.login

data class Exercise(val id: Int, val name: String, val targetMuscle: String, var sets: Int = 0, var reps: Int = 0, var isExpanded: Boolean = false)
