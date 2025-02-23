package com.example.mobcapp

data class ItemNamesDataClass(
    val id: Int,
    val name: String,
    val image: String,
    val price: Double,
    val desc: String,
    val descLong: String,
    val calories: Int,
    val ingredients: List<Ingredient>,
)
