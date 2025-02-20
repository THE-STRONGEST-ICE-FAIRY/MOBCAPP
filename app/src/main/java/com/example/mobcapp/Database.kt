package com.example.mobcapp
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class Database(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """CREATE TABLE $TABLE_ORDERS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_MEAL_NAME TEXT NOT NULL,
                $COLUMN_MEAL_PRICE REAL NOT NULL,
                $COLUMN_ORDER_AMOUNT INTEGER NOT NULL,
                $COLUMN_IS_VALUE_MEAL INTEGER NOT NULL CHECK ($COLUMN_IS_VALUE_MEAL IN (0,1)),
                $COLUMN_FRIES_SIZE TEXT,
                $COLUMN_DRINK_SIZE TEXT,
                $COLUMN_TOTAL_PRICE REAL NOT NULL,
                $COLUMN_ORDER_TIME TEXT DEFAULT CURRENT_TIMESTAMP
            )"""
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ORDERS")
        onCreate(db)
    }

    fun insertOrder(
        mealName: String, mealPrice: Double, orderAmount: Int, isValueMeal: Boolean,
        friesSize: String?, drinkSize: String?, totalPrice: Double
    ): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_MEAL_NAME, mealName)
            put(COLUMN_MEAL_PRICE, mealPrice)
            put(COLUMN_ORDER_AMOUNT, orderAmount)
            put(COLUMN_IS_VALUE_MEAL, if (isValueMeal) 1 else 0)
            put(COLUMN_FRIES_SIZE, friesSize)
            put(COLUMN_DRINK_SIZE, drinkSize)
            put(COLUMN_TOTAL_PRICE, totalPrice)
        }

        return db.insert(TABLE_ORDERS, null, values)
    }

    companion object {
        private const val DATABASE_NAME = "orders.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_ORDERS = "CompletedOrders"
        const val COLUMN_ID = "order_id"
        const val COLUMN_MEAL_NAME = "meal_name"
        const val COLUMN_MEAL_PRICE = "meal_price"
        const val COLUMN_ORDER_AMOUNT = "order_amount"
        const val COLUMN_IS_VALUE_MEAL = "is_value_meal"
        const val COLUMN_FRIES_SIZE = "fries_size"
        const val COLUMN_DRINK_SIZE = "drink_size"
        const val COLUMN_TOTAL_PRICE = "total_price"
        const val COLUMN_ORDER_TIME = "order_time"
    }

    fun getAllOrders(): List<Order> {
        val orders = mutableListOf<Order>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_ORDERS ORDER BY $COLUMN_ORDER_TIME DESC", null)

        while (cursor.moveToNext()) {
            val order = Order(
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MEAL_NAME)),
                cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_MEAL_PRICE)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDER_AMOUNT)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_VALUE_MEAL)) == 1,
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FRIES_SIZE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DRINK_SIZE)),
                cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_TOTAL_PRICE)),
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_TIME))
            )
            orders.add(order)
        }

        cursor.close()
        return orders
    }

    fun resetDatabase() {
        val db = writableDatabase
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ORDERS") // Delete the table
        onCreate(db) // Recreate the table
    }

}
