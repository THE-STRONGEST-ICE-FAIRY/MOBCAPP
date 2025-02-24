package com.example.mobcapp

data class ItemsDataClass(
    val id: Int,
    val ordered: Int,
    val name: String,
    val image: String,
    val price: Double,
    val desc: String,
    val descLong: String,
    val calories: Int,
    val grams: Double,
    val ingredients: List<Ingredient>,
)
