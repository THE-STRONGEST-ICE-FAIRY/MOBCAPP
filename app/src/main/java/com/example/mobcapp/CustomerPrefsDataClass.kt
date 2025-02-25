package com.example.mobcapp

data class CustomerPrefsDataClass(
    val id: Int,
    val name: String,
    val easy: List<Pair<String, Double>>,
    val medium: Pair<String, EqualizerDataClass>,
    val hard: Pair<String, EqualizerDataClass>,
)
