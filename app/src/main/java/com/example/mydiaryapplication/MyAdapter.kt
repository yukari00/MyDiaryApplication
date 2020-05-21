package com.example.mydiaryapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recycler_text.view.*

class MyAdapter(val data : List<NoteData>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    class MyViewHolder(val mView: View) : RecyclerView.ViewHolder(mView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter.MyViewHolder {
        val mView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_text, parent, false)
        return MyViewHolder(mView)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: MyAdapter.MyViewHolder, position: Int) {
        holder.mView.card_date.text = data[position].date.toString()
        holder.mView.card_title.text = data[position].title
    }


}