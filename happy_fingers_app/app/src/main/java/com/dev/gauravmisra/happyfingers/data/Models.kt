package com.dev.gauravmisra.happyfingers.data


import androidx.compose.ui.graphics.Color

data class ChoiceItem(
    val id: String,
    val label: String,
    val phrase: String = label,
    val color: Long = 0xFF90CAF9,
)

data class ScheduleStep(
    val id: String,
    val label: String,
    val done: Boolean = false
)

fun Long.toColor(): Color = Color(this)