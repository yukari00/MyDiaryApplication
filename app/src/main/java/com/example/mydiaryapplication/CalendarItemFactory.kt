package com.example.mydiaryapplication

import android.os.Build
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class CalendarItemFactory {

    companion object {
        private const val MAX_ROW = 6
        private const val NUM_WEEK = 7

        fun create(offsetMonth: Int): Array<CalendarItem> {
            val itemList: MutableList<CalendarItem> = arrayListOf()
            val calendar = Calendar.getInstance() //カレンダー取得
            val currentDay = calendar.get(Calendar.DATE) //今日の日付(10日、3日)
            calendar.add(Calendar.MONTH, offsetMonth) //今月(1 -> 2月、 3 -> 4月)
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
                itemList.add(CalendarItem.Day("", null))
            }
            // 有効日を埋める処理
            for (i in 1..dayOfMonth) {
                if (offsetMonth == 0 && i == currentDay) {
                    itemList.add(CalendarItem.Day("$i", Date(), true))
                } else {
                    val date = getDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), i)
                    itemList.add(CalendarItem.Day("$i", date))
                }
            }
            // 余りセルを埋める処理
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