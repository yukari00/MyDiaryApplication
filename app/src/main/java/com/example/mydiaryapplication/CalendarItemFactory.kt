package com.example.mydiaryapplication

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
            val dayOfMonth = calendar.getActualMaximum(Calendar.DATE)//当日は何日？
            calendar.set(Calendar.DATE, 1)
            val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)//SUNDAY(1), MONDAY(2)..

            //Header
            arrayOf("日", "月", "火", "水", "木", "金", "土").forEach {
                itemList.add(CalendarItem.Header(it))
            }
            val headerSize = itemList.size

            //　開始日までを埋める処理
            for (i in 1 until dayOfWeek) {
                itemList.add(CalendarItem.Day(""))
            }
            // 有効日を埋める処理
            for (i in 1..dayOfMonth) {
                if (offsetMonth == 0 && i == currentDay) {
                    itemList.add(CalendarItem.Day("$i", true))
                } else {
                    itemList.add(CalendarItem.Day("$i"))
                }
            }
            // 余りセルを埋める処理
            val fillSize = (MAX_ROW * NUM_WEEK + headerSize) - itemList.size
            for (i in 0 until fillSize) {
                itemList.add(CalendarItem.Day(""))
            }
            return itemList.toTypedArray()
        }
    }
}