package com.example.mydiaryapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.list_item_calendar.view.*
import kotlinx.android.synthetic.main.list_item_calendar_header.view.*
import java.lang.IllegalStateException

class CalendarAdapter(val listener: OnClickCalendarListener) :
    RecyclerView.Adapter<CalendarAdapter.BaseViewHolder>() {

    companion object {
        private const val VIEW_TYPE_HEADER = R.layout.list_item_calendar_header
        private const val VIEW_TYPE_DAY = R.layout.list_item_calendar

        private const val COLLECTION_USERS = "users"
        private const val COLLECTION_NOTES = "notes"
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
        return when (dataSource[position]) {
            is CalendarItem.Header -> VIEW_TYPE_HEADER
            is CalendarItem.Day -> VIEW_TYPE_DAY
        }
    }


    abstract class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun setViewData(item: CalendarItem)
    }

    private class CalendarHeaderViewHolder(view: View) : BaseViewHolder(view) {
        private val headerLabel: TextView = view.text_header

        override fun setViewData(item: CalendarItem) {
            item as CalendarItem.Header
            headerLabel.text = item.text
        }
    }

    private class CalendarDayViewHolder(view: View) : BaseViewHolder(view) {
        private val dayLabel: TextView = view.text_day
        private val check: ImageView = view.image_check

        override fun setViewData(item: CalendarItem) {
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

            if (item.date != null) {
                val database = FirebaseFirestore.getInstance()
                val user = FirebaseAuth.getInstance().currentUser!!.uid
                val docRef = database.collection(COLLECTION_USERS).document(user)
                    .collection(COLLECTION_NOTES).document(EditActivity.getId(item.date))

                docRef.get().addOnSuccessListener {
                    val title = it[NoteData.KEY_TITLE] as String?
                    val detail = it[NoteData.KEY_DETAIL] as String?

                    if (title != null && detail != null) {
                        check.setImageResource(R.drawable.ic_done_black_24dp)
                    }

                }
            }


        }
    }

}