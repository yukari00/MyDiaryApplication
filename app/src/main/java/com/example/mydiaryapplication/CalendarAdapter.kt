package com.example.mydiaryapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_calendar.view.*
import kotlinx.android.synthetic.main.list_item_calendar_header.view.*
import java.lang.IllegalStateException

class CalendarAdapter(val listener : OnClickCalendarListener) : RecyclerView.Adapter<CalendarAdapter.BaseViewHolder>() {

    companion object{
        private const val VIEW_TYPE_HEADER = R.layout.list_item_calendar_header
        private const val VIEW_TYPE_DAY = R.layout.list_item_calendar
    }

    interface OnClickCalendarListener{
        fun OnClick(item: CalendarItem.Day)
    }

    var dataSource: Array<CalendarItem> = emptyArray()
    set(value){
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when(viewType){
            VIEW_TYPE_HEADER -> CalendarHeaderViewHolder(LayoutInflater.from(parent.context).inflate(viewType, parent, false))
            VIEW_TYPE_DAY -> CalendarDayViewHolder(LayoutInflater.from(parent.context).inflate(viewType, parent, false))
            else -> throw IllegalStateException("Bad view type!!")
        }
    }

    override fun getItemCount(): Int {
        return dataSource.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val item = dataSource[position]
        when(item){
            is CalendarItem.Header -> {
                holder.setViewData(item)
            }
            is CalendarItem.Day -> {
                holder.setViewData(item)
                holder.itemView.card_day.setOnClickListener {
                    listener.OnClick(item)
                }
            }
        }


    }

    override fun getItemViewType(position: Int): Int {
        return when(dataSource[position]){
            is CalendarItem.Header -> VIEW_TYPE_HEADER
            is CalendarItem.Day -> VIEW_TYPE_DAY
        }
    }


    abstract class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun setViewData(item: CalendarItem)
    }

    private class CalendarHeaderViewHolder(view: View) : BaseViewHolder(view){
        private val headerLabel: TextView = view.text_header

        override fun setViewData(item: CalendarItem){
            item as CalendarItem.Header
            headerLabel.text = item.text
        }
    }

    private class CalendarDayViewHolder(view: View) : BaseViewHolder(view){
        private val dayLabel: TextView = view.text_day

        override fun setViewData(item: CalendarItem) {
            item as CalendarItem.Day
            dayLabel.text = item.day
            dayLabel.visibility = if(item.day.isEmpty()){
                View.GONE
            }else{
                View.VISIBLE
            }
            if(item.isToday){
                itemView.setBackgroundResource(R.color.colorPrimary)
            }else{
                itemView.setBackgroundResource(R.color.colorWhite)
            }
        }
    }

}