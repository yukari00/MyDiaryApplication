package com.example.mydiaryapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recycler_text.view.*

class MyAdapter(val data : List<NoteDataWithId>, val listener : OnClickNoteListener) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    interface OnClickNoteListener {
        fun OnClick(data : NoteDataWithId)
        fun OnLongClick(data : NoteDataWithId)
    }

    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_text, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: MyAdapter.MyViewHolder, position: Int) {
        val item = data[position]
        holder.view.card_date.text = data[position].date
        holder.view.card_title.text = data[position].title

        holder.view.card_view.setOnClickListener{
            listener.OnClick(item)
        }
        holder.view.card_view.setOnLongClickListener{
            listener.OnLongClick(item)
            true
        }
    }


}