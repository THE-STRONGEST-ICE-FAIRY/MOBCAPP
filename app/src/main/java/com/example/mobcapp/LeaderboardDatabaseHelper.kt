package com.example.mobcapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class LeaderboardDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_LEADERBOARD (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT,
                $COLUMN_SCORE INTEGER
            )
        """.trimIndent()
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_LEADERBOARD")
        onCreate(db)
    }

    fun insertEntry(name: String, score: Int) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_SCORE, score)
        }
        db.insert(TABLE_LEADERBOARD, null, values)
        db.close()
    }

    fun getTopScores(): List<Pair<String, Int>> {
        val leaderboard = mutableListOf<Pair<String, Int>>()
        val db = readableDatabase
        val query = "SELECT $COLUMN_NAME, $COLUMN_SCORE FROM $TABLE_LEADERBOARD ORDER BY $COLUMN_SCORE DESC, $COLUMN_ID DESC LIMIT 10"
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
                val score = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SCORE))
                leaderboard.add(Pair(name, score))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return leaderboard
    }

    fun clearLeaderboard() {
        val db = writableDatabase
        db.execSQL("DELETE FROM $TABLE_LEADERBOARD")
        db.close()
    }

    companion object {
        private const val DATABASE_NAME = "leaderboard.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_LEADERBOARD = "leaderboard"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_SCORE = "score"
    }

    fun sampleInsert() {
        val sampleData = listOf(
            Pair("Alice", 150),
            Pair("Bob", 120),
            Pair("Charlie", 180),
            Pair("Dave", 90),
            Pair("Eve", 200)
        )

        for ((name, score) in sampleData) {
            insertEntry(name, score) // Uses the insert() method to add test data
        }
    }

    fun dropTable() {
        val db = writableDatabase
        db.execSQL("DROP TABLE IF EXISTS $TABLE_LEADERBOARD")
        db.close()
    }
}