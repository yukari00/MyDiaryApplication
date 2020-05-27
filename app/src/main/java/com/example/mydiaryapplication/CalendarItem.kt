package com.example.mydiaryapplication

import java.util.*

sealed class CalendarItem {

    data class Header(val text : String) : CalendarItem()
    data class Day(val day: String, val date: Date?, val isToday: Boolean = false) : CalendarItem()

}