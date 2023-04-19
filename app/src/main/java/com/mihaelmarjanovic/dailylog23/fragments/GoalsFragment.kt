package com.mihaelmarjanovic.dailylog23.fragments

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mihaelmarjanovic.dailylog23.AddGoal
import com.mihaelmarjanovic.dailylog23.R
import com.mihaelmarjanovic.dailylog23.adapter.GoalsAdapter
import com.mihaelmarjanovic.dailylog23.database.LogsDatabase
import com.mihaelmarjanovic.dailylog23.databinding.FragmentGoalsBinding
import com.mihaelmarjanovic.dailylog23.models.DayViewModel
import com.mihaelmarjanovic.dailylog23.models.Goals
import com.mihaelmarjanovic.dailylog23.models.GoalsViewModel
import com.mihaelmarjanovic.dailylog23.models.LogsViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking


class GoalsFragment : Fragment(), GoalsAdapter.GoalsClickListener, PopupMenu.OnMenuItemClickListener {

    private lateinit var binding: FragmentGoalsBinding
    private lateinit var database: LogsDatabase
    private val viewModel : GoalsViewModel by activityViewModels()
    private val dayViewModel : LogsViewModel by activityViewModels()
    lateinit var adapter: GoalsAdapter
    lateinit var selectedGoal: Goals

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentGoalsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.goals.observe(viewLifecycleOwner){
            if (!binding.recyclerViewGoals.isComputingLayout()) {
                adapter.updateList(it)
            }
        }

        val currentDate = binding.tvCurrentDateGoals

        viewModel.currentDateIs.observe(viewLifecycleOwner){
            currentDate.text = it
            viewModel.initializeGoals(it)
        }

        database = LogsDatabase.getDatabase(Application())

        setUI()
    }

    private fun setUI(){
        //binding.recyclerViewGoals.setHasFixedSize(true)
        binding.recyclerViewGoals.layoutManager = LinearLayoutManager(requireContext())
        adapter = GoalsAdapter(requireContext(), this)
        binding.recyclerViewGoals.adapter = adapter

        val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                result ->
            if(result.resultCode == Activity.RESULT_OK){
                val goal = result.data?.getSerializableExtra("goal") as? Goals
                if(goal != null){
                    runBlocking {
                        viewModel.insertGoal(goal)
                        delay(50)
                        viewModel.initializeGoals(viewModel.currentDate)
                    }
                }
            }
        }

        viewModel.initializeGoals(viewModel.currentDate)

        //Previous date
        binding.btnPrevGoals.setOnClickListener{
            viewModel.prevDate()
            dayViewModel.prevDate()
            viewModel.initializeGoals(viewModel.currentDate)
        }
        //Next date
        binding.btnNextGoals.setOnClickListener{
            viewModel.nextDate()
            dayViewModel.nextDate()
            viewModel.initializeGoals(viewModel.currentDate)
        }

        //Binding add goal button
        binding.fbAddGoal.setOnClickListener{
            val intent = Intent(context, AddGoal::class.java)
            intent.putExtra("date",  viewModel.currentDate)
            intent.putExtra("day", viewModel.currentDay)
            intent.putExtra("month", viewModel.currentMonth)
            intent.putExtra("year", viewModel.currentYear)
            getContent.launch(intent)
        }

    }

    override fun onItemClicked(goals: Goals) {
     /*   val intent = Intent(context, AddLog::class.java)
        intent.putExtra("current_log", logs)
        intent.putExtra("date", logs.date)
        intent.putExtra("time", logs.timeOfLog)
        intent.putExtra("image", logs.image)
        updateLog.launch(intent)*/
    }

    override fun onLongItemClicked(goals: Goals, cardView: CardView) {
        selectedGoal = goals
        popUpDisplay(cardView)
    }

    override fun onCheckboxClick(isChecked: Boolean, goals: Goals) {
        runBlocking {
            viewModel.updateCheckedGoal(isChecked, goals.id!!)
            delay(50)
            viewModel.initializeGoals(viewModel.currentDate)
        }
    }

    private fun popUpDisplay(cardView: CardView) {
        val popup = PopupMenu(context, cardView)
        popup.setOnMenuItemClickListener(this@GoalsFragment)
        popup.inflate(R.menu.pop_up)
        popup.show()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if(item?.itemId == R.id.deleteLog){
            runBlocking {
                viewModel.deleteGoal(selectedGoal)
                delay(50)
                viewModel.initializeGoals(viewModel.currentDate)
            }
            return true
        }
        return false
    }

}