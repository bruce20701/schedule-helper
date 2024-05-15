package com.example.final_b11009025.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.final_b11009025.R
import com.example.final_b11009025.database.TodolistEntity

class TodolistAdapter(input:List<TodolistEntity>): RecyclerView.Adapter<TodolistAdapter.ViewHolder>() {

    var input = input

    //清單點擊事件介面
    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int, item: TodolistEntity)
    }

    private var onItemClickListener: OnItemClickListener? = null

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener?.onItemClick(itemView, position, input.get(position))
                }
            }
        }

        val text:TextView = itemView.findViewById(R.id.todolistText)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context).inflate(R.layout.todolist_item, parent, false)

        return ViewHolder(adapterLayout)
    }

    override fun getItemCount() = input.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.text.text = input.get(position).text
    }

    //設置點擊事件
    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    //資料更新通知
    fun DataSetUpdate(update:List<TodolistEntity>){
        input = update
        notifyDataSetChanged()
    }
}