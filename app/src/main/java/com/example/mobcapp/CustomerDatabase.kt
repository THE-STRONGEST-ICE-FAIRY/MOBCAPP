package com.example.mobcapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CustomerDatabase(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_ORDERING INTEGER, " +
                "$COLUMN_NAME TEXT, " +
                "$COLUMN_IMAGE TEXT, " +
                "$COLUMN_DIALOG_INTRO TEXT, " +
                "$COLUMN_DIALOG_OUTRO TEXT, " +
                "$COLUMN_DIALOG_FILLER TEXT)"
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertData(
        ordering: Int?,
        name: String?,
        image: List<String>?,
        dialogIntro: List<String>?,
        dialogOutro: List<String>?,
        dialogFiller: List<String>?
    ): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ORDERING, ordering)
            put(COLUMN_NAME, name)
            put(COLUMN_IMAGE, listToJson(image))
            put(COLUMN_DIALOG_INTRO, listToJson(dialogIntro))
            put(COLUMN_DIALOG_OUTRO, listToJson(dialogOutro))
            put(COLUMN_DIALOG_FILLER, listToJson(dialogFiller))
        }
        val result = db.insert(TABLE_NAME, null, values)
        db.close()
        return result
    }

    fun getAllData(): List<CustomerDataClass> {
        val customerList = mutableListOf<CustomerDataClass>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)

        if (cursor.moveToFirst()) {
            do {
                customerList.add(
                    CustomerDataClass(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        ordering = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDERING)),
                        name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                        image = jsonToList(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE))),
                        dialogIntro = jsonToList(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIALOG_INTRO))),
                        dialogOutro = jsonToList(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIALOG_OUTRO))),
                        dialogFiller = jsonToList(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DIALOG_FILLER)))
                    )
                )
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return customerList
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
        private const val DATABASE_NAME = "CustomerDatabase.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "Customers"
        private const val COLUMN_ID = "id"
        private const val COLUMN_ORDERING = "ordering"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_IMAGE = "image"
        private const val COLUMN_DIALOG_INTRO = "dialog_intro"
        private const val COLUMN_DIALOG_OUTRO = "dialog_outro"
        private const val COLUMN_DIALOG_FILLER = "dialog_filler"
    }

    fun listToJson(list: List<String>?): String {
        return Gson().toJson(list ?: emptyList<String>())
    }

    fun jsonToList(json: String?): List<String> {
        return json?.takeIf { it.isNotEmpty() }?.let {
            Gson().fromJson(it, object : TypeToken<List<String>>() {}.type)
        } ?: emptyList()
    }

    fun sampleInsert() {
        val result = insertData(
            ordering = 0,
            name = "Mr. Sample",
            image = listOf("customer/sample/neutral.png", "customer/sample/happy.png", "customer/sample/unhappy.png"),
            dialogIntro = listOf("Hello!", "I'm starving!"),
            dialogOutro = listOf("Thanks!", "That was great!"),
            dialogFiller = listOf("Hmm...", "Let me think...")
        )

        if (result == -1L) {
            println("💀 FAILED TO INSERT CUSTOMER!")
        } else {
            println("🔥 SUCCESS! INSERTED CUSTOMER ID: $result")
        }
    }
}