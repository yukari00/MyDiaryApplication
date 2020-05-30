package com.example.mydiaryapplication

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_calendar.view.*
import kotlinx.android.synthetic.main.list_item_calendar_header.view.*

abstract class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun setViewData(item: CalendarItem, noteData: List<NoteData>?)
}

class CalendarHeaderViewHolder(view: View) : BaseViewHolder(view) {
    private val headerLabel: TextView = view.text_header

    override fun setViewData(item: CalendarItem, noteData: List<NoteData>?) {
        item as CalendarItem.Header
        headerLabel.text = item.text
    }
}

class CalendarDayViewHolder(view: View) : BaseViewHolder(view) {
    private val dayLabel: TextView = view.text_day
    private val check: ImageView = view.image_check

    override fun setViewData(item: CalendarItem, noteData: List<NoteData>?) {
        item as CalendarItem.Day
        dayLabel.text = item.day
        dayLabel.visibility = if (item.day.isEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
        if (item.isToday) {
            itemView.setBackgroundResource(R.color.colorPurple)
        } else {
            itemView.setBackgroundResource(R.color.colorWhite)
        }

        if (noteData != null) {
            for (note in noteData) {
                if (note.date == item.date) {
                    check.setImageResource(R.drawable.ic_done_black_24dp)
                }
            }
        }

    }

    companion object {
        private const val COLLECTION_USERS = "users"
        private const val COLLECTION_NOTES = "notes"
    }
}