package com.example.mobcapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import org.json.JSONArray
import org.json.JSONObject

class ItemsDatabase(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_NAME TEXT, " +
                "$COLUMN_IMAGE TEXT, " +
                "$COLUMN_LABEL TEXT, " +
                "$COLUMN_PRICE DOUBLE, " +
                "$COLUMN_ORDERED INTEGER, " +
                "$COLUMN_DESC_LONG TEXT, " +
                "$COLUMN_CALORIES INTEGER, " +
                "$COLUMN_GRAMS DOUBLE, " +
                "$COLUMN_INGREDIENTS TEXT)" // Storing JSON string
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertData(
        name: String, image: String, info: String, price: Double, ordered: Int,
        descLong: String, calories: Int, grams: Double, ingredients: List<Ingredient>
    ): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_IMAGE, image)
            put(COLUMN_LABEL, info)
            put(COLUMN_PRICE, price)
            put(COLUMN_ORDERED, ordered)
            put(COLUMN_DESC_LONG, descLong)
            put(COLUMN_CALORIES, calories)
            put(COLUMN_GRAMS, grams)
            put(COLUMN_INGREDIENTS, ingredientsToJson(ingredients)) // Convert list to JSON
        }
        val result = db.insert(TABLE_NAME, null, values)
        db.close()
        return result
    }

    fun sampleInsert() {
        insertData(
            name = "Burger",
            image = "items/burger.png",
            info = "A gigantic burger of doom!",
            price = 9.99,
            ordered = 0,
            descLong = "This burger is so big it has its own gravitational pull.",
            calories = 1200,
            grams =  50.0,
            ingredients = listOf(
                Ingredient("Buns", 2),
                Ingredient("Patty", 1),
                Ingredient("Ketchup", 10) // Because you're an absolute mad lad
        ))
    }

    fun getAllData(): List<ItemsDataClass> {
        val itemList = mutableListOf<ItemsDataClass>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)

        if (cursor.moveToFirst()) {
            do {
                itemList.add(
                    ItemsDataClass(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        ordered = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDERED)),
                        name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                        image = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE)),
                        desc = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LABEL)),
                        price = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE)),
                        descLong = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESC_LONG)),
                        calories = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CALORIES)),
                        grams = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_GRAMS)),
                        ingredients = jsonToIngredients(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INGREDIENTS))) // Convert JSON back to list
                    )
                )
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return itemList
    }

    fun getOrderedData(ordered: Int): List<ItemsDataClass> {
        val itemList = mutableListOf<ItemsDataClass>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME WHERE $COLUMN_ORDERED = ?", arrayOf(ordered.toString()))

        if (cursor.moveToFirst()) {
            do {
                itemList.add(
                    ItemsDataClass(
                        id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        ordered = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDERED)),
                        name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                        image = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE)),
                        desc = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LABEL)),
                        price = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_PRICE)),
                        descLong = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESC_LONG)),
                        calories = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CALORIES)),
                        grams = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_GRAMS)),
                        ingredients = jsonToIngredients(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INGREDIENTS)))
                    )
                )
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return itemList
    }

//    fun updateData(
//        id: Int, name: String, image: String, descLong: String, calories: Int, ingredients: List<Ingredient>
//    ): Int {
//        val db = this.writableDatabase
//        val values = ContentValues().apply {
//            put(COLUMN_NAME, name)
//            put(COLUMN_IMAGE, image)
//            put(COLUMN_DESC_LONG, descLong)
//            put(COLUMN_CALORIES, calories)
//            put(COLUMN_INGREDIENTS, ingredientsToJson(ingredients)) // Convert list to JSON
//        }
//        val result = db.update(TABLE_NAME, values, "$COLUMN_ID=?", arrayOf(id.toString()))
//        db.close()
//        return result
//    }

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

    // Convert list of ingredients to JSON string
    private fun ingredientsToJson(ingredients: List<Ingredient>): String {
        val jsonArray = JSONArray()
        ingredients.forEach { ingredient ->
            val jsonObject = JSONObject().apply {
                put("name", ingredient.name)
                put("quantity", ingredient.quantity)
            }
            jsonArray.put(jsonObject)
        }
        return jsonArray.toString()
    }

    // Convert JSON string back to list of ingredients
    private fun jsonToIngredients(jsonString: String): List<Ingredient> {
        val ingredientsList = mutableListOf<Ingredient>()
        val jsonArray = JSONArray(jsonString)
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            ingredientsList.add(Ingredient(jsonObject.getString("name"), jsonObject.getInt("quantity")))
        }
        return ingredientsList
    }

    companion object {
        private const val DATABASE_NAME = "MyDatabase.db"
        private const val DATABASE_VERSION = 2 // Upgraded version because we changed schema
        private const val TABLE_NAME = "Users"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_IMAGE = "image"
        private const val COLUMN_LABEL = "info"
        private const val COLUMN_PRICE = "price"
        private const val COLUMN_ORDERED = "ordered"
        private const val COLUMN_DESC_LONG = "descLong"
        private const val COLUMN_CALORIES = "calories"
        private const val COLUMN_GRAMS = "grams"
        private const val COLUMN_INGREDIENTS = "ingredients"
    }
}
