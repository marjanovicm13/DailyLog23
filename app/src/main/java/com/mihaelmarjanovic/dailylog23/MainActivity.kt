package com.mihaelmarjanovic.dailylog23

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.mihaelmarjanovic.dailylog23.databinding.ActivityMainBinding
import com.mihaelmarjanovic.dailylog23.models.LogsViewModel


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var logsViewModel: LogsViewModel? = null
    private lateinit var mPreferences: SharedPreferences
    private val SP_THEME_KEY = "darkMode"
    private var themeIs: Boolean = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNavigationView = binding.bottomNavigationView
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView2) as NavHostFragment
        val navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.dayFragment, R.id.goalsFragment, R.id.calendarFragment, R.id.settingsFragment))
        setupActionBarWithNavController(navController, appBarConfiguration)

        bottomNavigationView.setupWithNavController(navController)
        logsViewModel = ViewModelProvider(this).get(LogsViewModel::class.java)
        mPreferences = getDefaultSharedPreferences(applicationContext)

        println(mPreferences)

        if (mPreferences!!.contains(SP_THEME_KEY)) {
            themeIs = mPreferences.getBoolean(SP_THEME_KEY, false)
            if(themeIs == true){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
        else{
            println("Nothing here.")
        }
    }

}