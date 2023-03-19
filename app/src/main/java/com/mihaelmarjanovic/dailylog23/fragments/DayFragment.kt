package com.mihaelmarjanovic.dailylog23.fragments

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.mihaelmarjanovic.dailylog23.AddLog
import com.mihaelmarjanovic.dailylog23.R
import com.mihaelmarjanovic.dailylog23.adapter.LogsAdapter
import com.mihaelmarjanovic.dailylog23.database.LogsDatabase
import com.mihaelmarjanovic.dailylog23.databinding.FragmentDayBinding
import com.mihaelmarjanovic.dailylog23.models.Logs
import com.mihaelmarjanovic.dailylog23.models.LogsViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*

class DayFragment : Fragment(), LogsAdapter.LogsClickListener, PopupMenu.OnMenuItemClickListener {

    private lateinit var binding: FragmentDayBinding
    private lateinit var database: LogsDatabase
    lateinit var adapter: LogsAdapter
    lateinit var selectedLog: Logs

    private val viewModel: LogsViewModel by activityViewModels()

    private val updateLog = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result ->
        if(result.resultCode == Activity.RESULT_OK){
            val log = result.data?.getSerializableExtra("log") as? Logs
            if(log != null){
                runBlocking {
                    viewModel.updateLogs(log)
                    delay(50)
                    viewModel.initializeLogs(viewModel.currentDate)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Inflate the layout for this fragment
        binding = FragmentDayBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.logs.observe(viewLifecycleOwner){
            adapter.updateList(it)
        }

        val currentDate = binding.tvCurrentDate

        viewModel.currentDateIs.observe(viewLifecycleOwner, Observer {
                currentDate.text = it
                viewModel.initializeLogs(it)
        })

        database = LogsDatabase.getDatabase(Application())

        setUI()
    }

    private fun setUI(){
        //Set recyclerView
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = LogsAdapter(requireContext(), this)
        binding.recyclerView.adapter = adapter

        //Result for add log activity
        val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
            if(result.resultCode == Activity.RESULT_OK){
                val log = result.data?.getSerializableExtra("log") as? Logs
                if(log != null){
                    runBlocking {
                        viewModel.insertLog(log)
                        delay(50)
                        viewModel.initializeLogs(viewModel.currentDate)
                    }
                }
            }
        }
        viewModel.initializeLogs(viewModel.currentDate)

        //Previous date
        binding.btnPrev.setOnClickListener{
            viewModel.prevDate()
            viewModel.initializeLogs(viewModel.currentDate)
        }
        //Next date
        binding.btnNext.setOnClickListener{
            viewModel.nextDate()
            viewModel.initializeLogs(viewModel.currentDate)
        }

        //Binding add log button
        binding.fbAddLog.setOnClickListener{
            val intent = Intent(context, AddLog::class.java)
            intent.putExtra("date",  viewModel.currentDate)
            intent.putExtra("time",  viewModel.currentTime)
            getContent.launch(intent)
        }

    }

    override fun onItemClicked(logs: Logs) {
        val intent = Intent(context, AddLog::class.java)
        intent.putExtra("current_log", logs)
        intent.putExtra("date", logs.date)
        intent.putExtra("time", logs.timeOfLog)
        intent.putExtra("image", logs.image)
        updateLog.launch(intent)
    }

    override fun onLongItemClicked(logs: Logs, cardView: CardView) {
        selectedLog = logs
        popUpDisplay(cardView)
    }

    private fun popUpDisplay(cardView: CardView) {
        val popup = PopupMenu(context, cardView)
        popup.setOnMenuItemClickListener(this@DayFragment)
        popup.inflate(R.menu.pop_up)
        popup.show()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if(item?.itemId == R.id.deleteLog){
            runBlocking {
                viewModel.deleteLog(selectedLog)
                delay(50)
                viewModel.initializeLogs(viewModel.currentDate)
            }
            return true
        }
        return false
    }
}