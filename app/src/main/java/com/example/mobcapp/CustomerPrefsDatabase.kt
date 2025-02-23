package com.example.mobcapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CustomerPrefsDatabase(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_NAME TEXT, " +
                "$COLUMN_EASY TEXT, " +
                "$COLUMN_MEDIUM TEXT, " +
                "$COLUMN_HARD TEXT)"
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertData(
        name: String?,
        easy: Map<String, Double>?,
        medium: Map<String, List<EqualizerDataClass>>?,
        hard: Map<String, List<EqualizerDataClass>>?
    ): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_EASY, mapToJson(easy))
            put(COLUMN_MEDIUM, mapToJson(medium))
            put(COLUMN_HARD, mapToJson(hard))
        }
        val result = db.insert(TABLE_NAME, null, values)
        db.close()
        return result
    }

    fun getAllData(): List<CustomerPrefsDataClass> {
        val prefsList = mutableListOf<CustomerPrefsDataClass>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)

        if (cursor.moveToFirst()) {
            do {
                prefsList.add(
                    CustomerPrefsDataClass(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                        easy = jsonToMap(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EASY))),
                        medium = jsonToMap(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MEDIUM))),
                        hard = jsonToMap(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HARD)))
                    )
                )
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return prefsList
    }

    fun deleteData(id: Int): Int {
        val db = this.writableDatabase
        val result = db.delete(TABLE_NAME, "$COLUMN_ID=?", arrayOf(id.toString()))
        db.close()
        return result
    }

    fun dropTable() {
        val db = this.writableDatabase
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    companion object {
        private const val DATABASE_NAME = "CustomerPrefsDatabase.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "CustomerPreferences"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_EASY = "easy"
        private const val COLUMN_MEDIUM = "medium"
        private const val COLUMN_HARD = "hard"
    }

    fun <K, V> mapToJson(map: Map<K, V>?): String {
        return Gson().toJson(map ?: emptyMap<K, V>())
    }

    inline fun <reified K, reified V> jsonToMap(json: String?): Map<K, V> {
        return json?.takeIf { it.isNotEmpty() }?.let {
            Gson().fromJson(it, object : TypeToken<Map<K, V>>() {}.type)
        } ?: emptyMap()
    }

    fun sampleInsert() {
        val easyPrefs = mapOf(
            "Cake" to 0.5,
            "Ice Cream" to 0.2
        )

        val mediumPrefs = mapOf(
            "Spicy" to listOf(EqualizerDataClass(
                id = 2,
                items = mapOf("Spicy Noodles" to 1),
                timeMin = 5,
                timeMax = 10
            ))
        )

        val hardPrefs = mapOf(
            "Impossible" to listOf(EqualizerDataClass(
                id = 3,
                items = mapOf("Ghost Pepper Wings" to 2),
                customerTimeLeftMin = 1,
                customerTimeLeftMax = 5,
                salesMin = 0,
                salesMax = 10
            ))
        )

        val result = insertData(
            name = "Mr. Picky",
            easy = easyPrefs,
            medium = mediumPrefs,
            hard = hardPrefs
        )

        if (result == -1L) {
            println("💀 FAILED TO INSERT CUSTOMER PREFERENCES!")
        } else {
            println("🔥 SUCCESS! INSERTED PREFERENCES FOR CUSTOMER ID: $result")
        }
    }
}
