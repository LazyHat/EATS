package com.example.eats.data.products.db.day


data class Day(
    val time: DateTime,
    val caloriesDay: Float,
    val caloriesCurrent: Float,
    val eatBoxCalories: List<Float> = listOf(0f, 0f, 0f, 0f)
)

data class DateTime(
    val year: Int,
    val month: Int,
    val day: Int
)