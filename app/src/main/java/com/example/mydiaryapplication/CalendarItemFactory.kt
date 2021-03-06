package com.example.mydiaryapplication

import java.text.SimpleDateFormat
import java.util.*

class CalendarItemFactory {

    companion object {
        private const val MAX_ROW = 6
        private const val NUM_WEEK = 7

        fun create(offsetMonth: Int): Array<CalendarItem> {
            val itemList: MutableList<CalendarItem> = arrayListOf()
            val calendar = Calendar.getInstance()
            val currentDay = calendar.get(Calendar.DATE)
            calendar.add(Calendar.MONTH, offsetMonth)
            val dayOfMonth = calendar.getActualMaximum(Calendar.DATE)
            calendar.set(Calendar.DATE, 1)
            val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

            arrayOf("日", "月", "火", "水", "木", "金", "土").forEach {
                itemList.add(CalendarItem.Header(it))
            }
            val headerSize = itemList.size

            for (i in 1 until dayOfWeek) {
                itemList.add(CalendarItem.Day("", null))
            }


            for (i in 1..dayOfMonth) {
                if (offsetMonth == 0 && i == currentDay) {
                    val date =
                        getDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, i)
                    itemList.add(CalendarItem.Day("$i", date, true))
                } else {
                    val date =
                        getDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, i)
                    itemList.add(CalendarItem.Day("$i", date))
                }
            }

            val fillSize = (MAX_ROW * NUM_WEEK + headerSize) - itemList.size
            for (i in 0 until fillSize) {
                itemList.add(CalendarItem.Day("", null))
            }
            return itemList.toTypedArray()
        }

        private fun getDate(year: Int, month: Int, day: Int): Date? {
            val dateString = "$year/$month/$day"
            val dateFormat = SimpleDateFormat("yyyy/MM/dd")
            val date = dateFormat.parse(dateString)

            return date
        }
    }
}