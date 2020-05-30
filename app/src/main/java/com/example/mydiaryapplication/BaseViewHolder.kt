package com.example.mydiaryapplication

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.mydiaryapplication.databinding.ListItemCalendarBinding
import com.example.mydiaryapplication.databinding.ListItemCalendarHeaderBinding

abstract class BaseViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
    abstract fun setViewData(item: CalendarItem, listener: CalendarAdapter.OnClickCalendarListener, noteData: List<NoteData>)
}

class CalendarHeaderViewHolder(binding: ListItemCalendarHeaderBinding) : BaseViewHolder(binding) {
    private val bindingHeader = binding

    override fun setViewData(item: CalendarItem, listener: CalendarAdapter.OnClickCalendarListener, noteData: List<NoteData>) {
        item as CalendarItem.Header
        bindingHeader.header = item
    }
}

class CalendarDayViewHolder(binding: ListItemCalendarBinding) : BaseViewHolder(binding) {
    private val bindingDay = binding

    override fun setViewData(item: CalendarItem, listener: CalendarAdapter.OnClickCalendarListener, noteData: List<NoteData>) {
        item as CalendarItem.Day
        bindingDay.day = item

        bindingDay.textDay.visibility = if (item.day.isEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
        if (item.isToday) {
            itemView.setBackgroundResource(R.color.colorPurple)
        } else {
            itemView.setBackgroundResource(R.color.colorWhite)
        }

        bindingDay.cardDay.setOnClickListener{
            listener.OnClick(item)
        }

        for (note in noteData) {
            if (note.date == item.date) {
                bindingDay.imageCheck.setImageResource(R.drawable.ic_done_black_24dp)
            }
        }
    }
}
