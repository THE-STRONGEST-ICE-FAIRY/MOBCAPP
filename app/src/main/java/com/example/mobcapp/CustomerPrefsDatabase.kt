package com.example.mobcapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CustomerPrefsDatabase(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, 
                $COLUMN_NAME TEXT, 
                $COLUMN_EASY TEXT,  -- Store JSON List<Pair<String, Double>>
                $COLUMN_MEDIUM TEXT, -- Store JSON Pair<String, EqualizerDataClass>
                $COLUMN_HARD TEXT -- Store JSON Pair<String, EqualizerDataClass>
            )
        """.trimIndent()
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertData(
        name: String?,
        easy: List<Pair<String, Double>>?,
        medium: Pair<String, EqualizerDataClass>?,
        hard: Pair<String, EqualizerDataClass>?
    ): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_EASY, listToJson(easy)) // Convert List<Pair<String, Double>> to JSON
            put(COLUMN_MEDIUM, pairToJson(medium)) // Convert Pair<String, EqualizerDataClass> to JSON
            put(COLUMN_HARD, pairToJson(hard)) // Convert Pair<String, EqualizerDataClass> to JSON
        }
        val result = db.insert(TABLE_NAME, null, values)
        db.close()
        return result
    }

    fun getRandomCustomer(): CustomerPrefsDataClass? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME ORDER BY RANDOM() LIMIT 1", null)

        var customer: CustomerPrefsDataClass? = null

        if (cursor.moveToFirst()) {
            customer = CustomerPrefsDataClass(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                easy = jsonToList(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EASY))),
                medium = jsonToPair(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MEDIUM))),
                hard = jsonToPair(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HARD)))
            )
        }

        cursor.close()
        db.close()
        return customer
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
                        easy = jsonToList(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_EASY))),
                        medium = jsonToPair(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MEDIUM))),
                        hard = jsonToPair(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HARD)))
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

    // JSON Conversion Functions
    fun <T> toJson(obj: T): String {
        return Gson().toJson(obj)
    }

    fun <T> fromJson(json: String?, type: TypeToken<T>): T {
        return json?.takeIf { it.isNotEmpty() }?.let {
            Gson().fromJson(it, type.type)
        } ?: throw IllegalArgumentException("Invalid JSON")
    }

    fun listToJson(list: List<Pair<String, Double>>?): String {
        return toJson(list ?: emptyList())
    }

    fun jsonToList(json: String?): List<Pair<String, Double>> {
        return fromJson(json, object : TypeToken<List<Pair<String, Double>>>() {})
    }

    fun pairToJson(pair: Pair<String, EqualizerDataClass>?): String {
        return toJson(pair ?: Pair("", EqualizerDataClass(0)))
    }

    fun jsonToPair(json: String?): Pair<String, EqualizerDataClass> {
        return fromJson(json, object : TypeToken<Pair<String, EqualizerDataClass>>() {})
    }

    // Sample Data Insert
    fun sampleInsert() {
        val easyPrefs = listOf(
            "Cheesy Burger" to 0.5,
            "Cheesy Fry Bucket" to 0.2,
            "Milktea" to 0.4,
        )

        val mediumPrefs = "Spicy" to EqualizerDataClass(
            id = 2,
            items = listOf("Spicy Noodles" to 1),
            timeMin = 5,
            timeMax = 10
        )

        val hardPrefs = "Impossible" to EqualizerDataClass(
            id = 1,
            items = listOf("Cheesy Burger" to 2, "Milktea" to 1, "Classic Fry Bucket" to 1),
            itemsCountMin = 1,
            itemsCountMax = 5,
            priceMin = 10.0,
            priceMax = 50.0,
            ingredient = listOf(
                Ingredient(name = "Lettuce", quantity = 2),
                Ingredient(name = "Tomato", quantity = 1)
            ),
            ingredientsCountMin = 1,
            ingredientsCountMax = 4,
            caloriesMin = 200,
            caloriesMax = 1200,
            timeMin = 5,
            timeMax = 30,
            customerTimeLeftMin = 10,
            customerTimeLeftMax = 60,
            salesMin = 5.0,
            salesMax = 50.0,
            gramsMin = 100.0,
            gramsMax = 500.0
        )

        val result = insertData(
            name = "Mr. Picky",
            easy = easyPrefs,
            medium = mediumPrefs,
            hard = hardPrefs
        )

        if (result == -1L) {
            println("🔥 FAILED TO INSERT CUSTOMER PREFERENCES!")
        } else {
            println("🎉 SUCCESS! INSERTED PREFERENCES FOR CUSTOMER ID: $result")
        }
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
}