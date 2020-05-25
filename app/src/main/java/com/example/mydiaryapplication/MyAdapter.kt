package com.example.mydiaryapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mydiaryapplication.databinding.RecyclerTextBinding

class MyAdapter(val data : List<NoteDataWithId>, val listener : OnClickNoteListener) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    interface OnClickNoteListener {
        fun OnClick(data : NoteDataWithId)
        fun OnLongClick(data : NoteDataWithId)
    }

    class MyViewHolder(val binding: RecyclerTextBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context)
        val binding = RecyclerTextBinding.inflate(view, parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: MyAdapter.MyViewHolder, position: Int) {
        val item = data[position]
        holder.binding.cardDate.text = data[position].date
        holder.binding.cardTitle.text = data[position].title

        holder.binding.cardView.setOnClickListener{
            listener.OnClick(item)
        }
        holder.binding.cardView.setOnLongClickListener{
            listener.OnLongClick(item)
            true
        }
    }


}