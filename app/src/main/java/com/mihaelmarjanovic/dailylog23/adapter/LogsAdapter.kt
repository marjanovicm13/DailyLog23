package com.mihaelmarjanovic.dailylog23.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.mihaelmarjanovic.dailylog23.R
import com.mihaelmarjanovic.dailylog23.fragments.DayFragment
import com.mihaelmarjanovic.dailylog23.models.Logs

class LogsAdapter(private val context: Context, val listener: LogsClickListener): RecyclerView.Adapter<LogsAdapter.LogsViewHolder>() {

    private val LogsList = ArrayList<Logs>()
    private val fullList = ArrayList<Logs>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogsViewHolder {
        return LogsViewHolder(
            LayoutInflater.from(context).inflate(R.layout.log_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: LogsViewHolder, position: Int) {
        val currentLog = LogsList[position]
        holder.title.text = currentLog.title
        //ŠTA IS SELECTED ZNAČI???
        holder.title.isSelected = true
        holder.log.text = currentLog.log
        holder.time.text = currentLog.timeOfLog
        holder.time.isSelected = true

        holder.logs_layout.setOnClickListener{
            listener.onItemClicked(LogsList[holder.adapterPosition])
        }
        holder.logs_layout.setOnLongClickListener{
            listener.onLongItemClicked(LogsList[holder.adapterPosition], holder.logs_layout)
            true
        }

    }

    override fun getItemCount(): Int {
        return LogsList.size
    }

    fun updateList(newList: List<Logs>){
        fullList.clear()
        fullList.addAll(newList)

        LogsList.clear()
        LogsList.addAll(fullList)
        notifyDataSetChanged()
    }

    // For search bar - minute 1h 13min fun filterList()

    inner class LogsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val logs_layout = itemView.findViewById<CardView>(R.id.cardLayout)
        val title = itemView.findViewById<TextView>(R.id.tvTitle)
        val log = itemView.findViewById<TextView>(R.id.tvLog)
        val time = itemView.findViewById<TextView>(R.id.tvTime)
    }

    interface LogsClickListener{
        fun onItemClicked(logs: Logs)
        fun onLongItemClicked(logs: Logs, cardView: CardView)
    }

}