package com.example.mydiaryapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_calendar.view.*
import java.lang.IllegalStateException

class CalendarAdapter(val noteData: List<NoteData>?, val listener: OnClickCalendarListener) :
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
        return when (viewType) {
            VIEW_TYPE_HEADER -> CalendarHeaderViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    viewType,
                    parent,
                    false
                )
            )
            VIEW_TYPE_DAY -> CalendarDayViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    viewType,
                    parent,
                    false
                )
            )
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
                holder.setViewData(item, null)
            }
            is CalendarItem.Day -> {
                holder.setViewData(item, noteData)
                holder.itemView.card_day.setOnClickListener {
                    listener.OnClick(item)
                }
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