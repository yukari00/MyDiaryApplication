package com.example.mydiaryapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mydiaryapplication.databinding.RecyclerTextBinding

class MyAdapter(val data: List<NoteData>, val listener: OnClickNoteListener) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    interface OnClickNoteListener {
        fun OnClick(data: NoteData)
        fun OnLongClick(data: NoteData)
    }

    class MyViewHolder(val binding: RecyclerTextBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter.MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RecyclerTextBinding.inflate(layoutInflater, parent, false)

        return MyViewHolder(binding)

    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: MyAdapter.MyViewHolder, position: Int) {
        val item = data[position]

        holder.binding.noteData = item
        holder.binding.cardColor.setBackgroundResource(R.color.colorPrimary)

        holder.binding.cardView.setOnClickListener {
            listener.OnClick(item)
        }
        holder.binding.cardView.setOnLongClickListener {
            listener.OnLongClick(item)
            true
        }
    }


}