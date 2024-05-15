package com.example.final_b11009025.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.final_b11009025.R
import com.example.final_b11009025.database.CalendarEntity

class CalendarAdapter(input:List<CalendarEntity>): RecyclerView.Adapter<CalendarAdapter.ViewHolder>()  {
    var input = input

    //清單點擊事件介面
    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int, item: CalendarEntity)
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

        val time: TextView = itemView.findViewById(R.id.calendarTime)
        val text: TextView = itemView.findViewById(R.id.calendarText)
        val type: TextView = itemView.findViewById(R.id.calendarType)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context).inflate(R.layout.calendar_item, parent, false)

        return ViewHolder(adapterLayout)
    }

    override fun getItemCount() = input.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if(input.get(position).type.equals("放假"))
            holder.itemView.setBackgroundColor(Color.parseColor("#F0F8FF"))
        else if (input.get(position).type.equals("補班"))
            holder.itemView.setBackgroundColor(Color.parseColor("#FFECF5"))
        else
            holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"))
        holder.time.text = input.get(position).time
        holder.text.text = input.get(position).text
        holder.type.text = input.get(position).type
    }

    //設置點擊事件
    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    //資料更新通知
    fun DataSetUpdate(update:List<CalendarEntity>){
        input = update
        notifyDataSetChanged()
    }
}

