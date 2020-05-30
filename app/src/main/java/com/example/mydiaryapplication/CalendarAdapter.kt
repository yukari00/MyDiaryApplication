package com.example.mydiaryapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mydiaryapplication.databinding.ListItemCalendarBinding
import com.example.mydiaryapplication.databinding.ListItemCalendarHeaderBinding
import java.lang.IllegalStateException

class CalendarAdapter(var noteData: List<NoteData>, val listener: OnClickCalendarListener) :
    RecyclerView.Adapter<BaseViewHolder>() {

    companion object {
        private const val VIEW_TYPE_HEADER = R.layout.list_item_calendar_header
        private const val VIEW_TYPE_DAY = R.layout.list_item_calendar
    }

    interface OnClickCalendarListener {
        fun OnClick(item: CalendarItem.Day)
    }

    var dataSource: Array<CalendarItem> = emptyArray()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        when (viewType) {
            VIEW_TYPE_HEADER -> {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemCalendarHeaderBinding.inflate(layoutInflater, parent, false)

                return CalendarHeaderViewHolder(binding)
            }
            VIEW_TYPE_DAY -> {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemCalendarBinding.inflate(layoutInflater, parent, false)

                return CalendarDayViewHolder(binding)
            }
            else -> throw IllegalStateException("Bad view type!!")
        }
    }

    override fun getItemCount(): Int {
        return dataSource.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val item = dataSource[position]
        when (item) {
            is CalendarItem.Header -> {
                holder.setViewData(item, listener, emptyList())
            }
            is CalendarItem.Day -> {
                holder.setViewData(item, listener, noteData)

            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (dataSource[position]) {
            is CalendarItem.Header -> VIEW_TYPE_HEADER
            is CalendarItem.Day -> VIEW_TYPE_DAY
        }
    }

}