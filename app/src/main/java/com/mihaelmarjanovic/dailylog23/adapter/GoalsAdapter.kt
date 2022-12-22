package com.mihaelmarjanovic.dailylog23.adapter

import android.app.Application
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.mihaelmarjanovic.dailylog23.R
import com.mihaelmarjanovic.dailylog23.database.GoalsDao
import com.mihaelmarjanovic.dailylog23.database.GoalsRepository
import com.mihaelmarjanovic.dailylog23.database.LogsDatabase
import com.mihaelmarjanovic.dailylog23.models.Goals
import com.mihaelmarjanovic.dailylog23.models.GoalsViewModel
import com.mihaelmarjanovic.dailylog23.models.Logs

class GoalsAdapter(private val context: Context, val listener: GoalsAdapter.GoalsClickListener): RecyclerView.Adapter<GoalsAdapter.GoalsViewHolder>() {
    private val GoalsList = ArrayList<Goals>()
    private val fullList = ArrayList<Goals>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalsViewHolder {
        return GoalsViewHolder(
            LayoutInflater.from(context).inflate(R.layout.goal_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: GoalsViewHolder, position: Int) {
        val currentGoal = GoalsList[position]
        holder.goal.text = currentGoal.goal
        holder.checkBox.isChecked = currentGoal.isChecked

        holder.checkBox.setOnCheckedChangeListener{buttonView, isChecked ->
            listener.onCheckboxClick(isChecked, GoalsList[holder.adapterPosition])
        }

        holder.goals_layout.setOnClickListener {
            listener.onItemClicked(GoalsList[holder.adapterPosition])
        }
        holder.goals_layout.setOnLongClickListener {
            listener.onLongItemClicked(GoalsList[holder.adapterPosition], holder.goals_layout)
            true
        }
    }

    override fun getItemCount(): Int {
        return GoalsList.size
    }

    fun updateList(newList: List<Goals>){
        fullList.clear()
        fullList.addAll(newList)

        GoalsList.clear()
        GoalsList.addAll(fullList)
        notifyDataSetChanged()
    }

    inner class GoalsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val goals_layout = itemView.findViewById<CardView>(R.id.goalCardLayout)
        val checkBox = itemView.findViewById<CheckBox>(R.id.checkBoxGoal)
        val goal = itemView.findViewById<TextView>(R.id.tvGoal)
    }

    interface GoalsClickListener{
        fun onItemClicked(goals: Goals)
        fun onLongItemClicked(goals: Goals, cardView: CardView)
        fun onCheckboxClick(isChecked: Boolean, goals: Goals)
    }
}