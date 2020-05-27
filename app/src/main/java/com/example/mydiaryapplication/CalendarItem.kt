package com.example.mydiaryapplication

sealed class CalendarItem {

    data class Header(val text : String) : CalendarItem()
    data class Day(val day: String, val isToday: Boolean = false) : CalendarItem()
}