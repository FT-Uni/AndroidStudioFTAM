package com.example.login
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 4
        private const val DATABASE_NAME = "fitnessTracker.db"

        private const val TABLE_USERS = "userLoginInfo"
        private const val TABLE_PROFILE = "userProfile"
        private const val TABLE_EXERCISE = "pastExercises"
        private const val TABLE_EXERCISE_DETAILS = "exerciseDetails"
        private const val TABLE_ALL_EXERCISES = "allExercises"

        private const val COLUMN_ID = "id"
        private const val COLUMN_USERNAME = "username"
        private const val COLUMN_PASSWORD = "password"
        private const val COLUMN_WEIGHT = "weight"
        private const val COLUMN_HEIGHT = "height"
        private const val COLUMN_AGE = "age"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_CATEGORY = "category"
        private const val COLUMN_DATE = "date"
        private const val COLUMN_DAY_OF_WEEK = "day"
        private const val COLUMN_WEEK_NUMBER = "week"
        private const val COLUMN_EXERCISE_NAME = "exerciseName"
        private const val COLUMN_TARGET_MUSCLE = "targetMuscle"
        private const val COLUMN_EXERCISE_ID = "exerciseID"
        private const val COLUMN_SETS = "sets"
        private const val COLUMN_REPS = "reps"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createUserTableQuery = ("CREATE TABLE $TABLE_USERS " +
                "($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_USERNAME TEXT, $COLUMN_PASSWORD TEXT)")
        db?.execSQL(createUserTableQuery)

        val createUserInfoTableQuery = ("CREATE TABLE $TABLE_PROFILE " +
                "($COLUMN_USERNAME TEXT PRIMARY KEY, " +
                "$COLUMN_WEIGHT REAL, $COLUMN_HEIGHT REAL, $COLUMN_AGE INTEGER)")
        db?.execSQL(createUserInfoTableQuery)

        val createExerciseTableQuery = ("CREATE TABLE $TABLE_EXERCISE " +
                "($COLUMN_ID INTEGER PRIMARY KEY, " +
                "$COLUMN_NAME TEXT, " +
                "$COLUMN_CATEGORY TEXT, " +
                "$COLUMN_DATE TEXT) ")
        db?.execSQL(createExerciseTableQuery)

        val createWorkoutTableQuery = ("CREATE TABLE $TABLE_EXERCISE_DETAILS " +
                "($COLUMN_EXERCISE_ID INTEGER PRIMARY KEY, " +
                "$COLUMN_SETS INTEGER, " +
                "$COLUMN_REPS INTEGER)")


        val createAllExercisesTableQuery = ("CREATE TABLE $TABLE_ALL_EXERCISES " +
                "($COLUMN_EXERCISE_ID INTEGER PRIMARY KEY, " +
                "$COLUMN_EXERCISE_NAME TEXT, " +
                "$COLUMN_TARGET_MUSCLE TEXT)")

        try {
            db?.execSQL(createWorkoutTableQuery)
            db?.execSQL(createAllExercisesTableQuery)
        } catch (e: Exception) {
            Log.e("DatabaseHelper", "Error creating tables: ${e.message}")
        }

        db?.let {
            populateExercises(it)
        }
    }

    private fun populateExercises(db: SQLiteDatabase) {
        val exercises = listOf(
            Exercise(1, "Push-up", "Chest"),
            Exercise(2, "Squats", "Legs"),
            Exercise(3, "Bench Press", "Chest"),
            Exercise(4, "Incline Dumbbell Press", "Chest"),
            Exercise(5, "Lat Pull Downs", "Back")
        )

        for (exercise in exercises) {
            val contentValues = ContentValues().apply {
                put("exerciseID", exercise.id)
                put("exerciseName", exercise.name)
                put("targetMuscle", exercise.targetMuscle)
            }
            db.insert(TABLE_ALL_EXERCISES, null, contentValues)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_PROFILE")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_EXERCISE")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_EXERCISE_DETAILS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_ALL_EXERCISES")

        onCreate(db)
    }

    fun addUser(username: String, password: String): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_USERNAME, username)
        contentValues.put(COLUMN_PASSWORD, password)

        return try {
            val result = db.insertWithOnConflict(TABLE_USERS, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE)
            return result != -1L
        } catch (e: Exception) {
            Log.e("DatabaseHelper", "Error adding user: ${e.message}")
            false
        } /*finally {
            db.close()
        }*/
    }

    fun addProfile(username: String, weight: Double, height: Double, age: Int): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_USERNAME, username)
        contentValues.put(COLUMN_WEIGHT, weight)
        contentValues.put(COLUMN_HEIGHT, height)
        contentValues.put(COLUMN_AGE, age)

        return try {
            val result = db.insertWithOnConflict(TABLE_PROFILE, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE)
            return result != -1L
        } catch (e: Exception) {
            Log.e("DatabaseHelper", "Error adding profile: ${e.message}")
            false
        } /*finally {
            db.close()
        }*/
    }

    fun addExercise(username: String, exercise: PastExercises) {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_USERNAME, username)
        contentValues.put(COLUMN_EXERCISE_NAME, exercise.name)
        contentValues.put(COLUMN_DATE, exercise.date)
        contentValues.put(COLUMN_WEEK_NUMBER, exercise.weekNumber)
        contentValues.put(COLUMN_DAY_OF_WEEK, exercise.day)
        db.insert(TABLE_EXERCISE, null, contentValues)
        /*db.close()*/
    }

    @SuppressLint("Range")
    fun getExerciseById(exerciseId: Int): Exercise? {
        val db = this.readableDatabase
        var exercise: Exercise? = null
        val query = "SELECT * FROM $TABLE_EXERCISE WHERE $COLUMN_ID = ?"
        val cursor = db.rawQuery(query, arrayOf(exerciseId.toString()))

        if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
            val name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
            val targetMuscle = cursor.getString(cursor.getColumnIndex(COLUMN_TARGET_MUSCLE))
            exercise = Exercise(id, name, targetMuscle)
        }

        cursor.close()
        return exercise
    }

    fun addExerciseDetail(exerciseId: Int, sets: Int, reps: Int): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put(COLUMN_EXERCISE_ID, exerciseId)
        contentValues.put(COLUMN_SETS, sets)
        contentValues.put(COLUMN_REPS, reps)

        val result = db.insert(TABLE_EXERCISE_DETAILS, null, contentValues)
        return result != -1L
    }

    @SuppressLint("Range")
    fun getAllExercises(): List<Exercise> {
        val exercises = mutableListOf<Exercise>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_ALL_EXERCISES", null)
        while (cursor.moveToNext()) {
            val exercise = Exercise(
                cursor.getInt(cursor.getColumnIndex("exerciseID")),
                cursor.getString(cursor.getColumnIndex("exerciseName")),
                cursor.getString(cursor.getColumnIndex("targetMuscle")),
            )
            exercises.add(exercise)
        }
        cursor.close()
        return exercises
    }

}