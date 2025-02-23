package com.example.mobcapp

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

data class EqualizerDataClass(
    val id: Int? = null,
    val items: Map<String, Int>? = null,
    val itemsCountMin: Int? = null,
    val itemsCountMax: Int? = null,
    val priceMin: Double? = null,
    val priceMax: Double? = null,
    val ingredient: List<Ingredient>? = null,
    val ingredientsCountMin: Int? = null,
    val ingredientsCountMax: Int? = null,
    val caloriesMin: Int? = null,
    val caloriesMax: Int? = null,
    val timeMin: Int? = null,
    val timeMax: Int? = null,
    val customerTimeLeftMin: Int? = null,
    val customerTimeLeftMax: Int? = null,
    val salesMin: Int? = null,
    val salesMax: Int? = null,
    val gramsMin: Int? = null,
    val gramsMax: Int? = null
)
