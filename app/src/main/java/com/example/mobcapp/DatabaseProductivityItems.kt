package com.example.mobcapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseProductivityItems(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_ORDERED INTEGER DEFAULT 0, " + // 0 = false, 1 = true
                "$COLUMN_NAME TEXT, " +
                "$COLUMN_PRICE REAL, " +
                "$COLUMN_DESCRIPTION TEXT, " +
                "$COLUMN_TIMESTAMP TIMESTAMP DEFAULT CURRENT_TIMESTAMP)"
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertItem(ordered: Boolean, name: String, price: Double, description: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ORDERED, if (ordered) 1 else 0)
            put(COLUMN_NAME, name)
            put(COLUMN_PRICE, price)
            put(COLUMN_DESCRIPTION, description)
        }
        return db.insert(TABLE_NAME, null, values)
    }

    fun getAllItems(): List<DataClassProductivityItem> {
        val items = mutableListOf<DataClassProductivityItem>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME ORDER BY $COLUMN_TIMESTAMP DESC", null)

        with(cursor) {
            while (moveToNext()) {
                val item = DataClassProductivityItem(
                    getInt(getColumnIndexOrThrow(COLUMN_ID)),
                    getInt(getColumnIndexOrThrow(COLUMN_ORDERED)) == 1, // Convert 1/0 to Boolean
                    getString(getColumnIndexOrThrow(COLUMN_NAME)),
                    getDouble(getColumnIndexOrThrow(COLUMN_PRICE)),
                    getString(getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                    getString(getColumnIndexOrThrow(COLUMN_TIMESTAMP))
                )
                items.add(item)
            }
            close()
        }
        return items
    }

    fun deleteItem(id: Int): Int {
        val db = writableDatabase
        return db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(id.toString()))
    }

    companion object {
        private const val DATABASE_NAME = "productivity.db"
        private const val DATABASE_VERSION = 2 // Increased version since structure changed
        const val TABLE_NAME = "productivity_items"
        const val COLUMN_ID = "id"
        const val COLUMN_ORDERED = "ordered"
        const val COLUMN_NAME = "name"
        const val COLUMN_PRICE = "price"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_TIMESTAMP = "timestamp"
    }
}
