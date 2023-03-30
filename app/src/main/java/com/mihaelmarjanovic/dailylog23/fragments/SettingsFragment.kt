package com.mihaelmarjanovic.dailylog23.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.preference.*
import com.mihaelmarjanovic.dailylog23.R
import com.mihaelmarjanovic.dailylog23.databinding.FragmentSettingsBinding


class SettingsFragment : PreferenceFragmentCompat() {

    private var preferenceChangeListener: SharedPreferences.OnSharedPreferenceChangeListener? = null

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference, rootKey)
        val emailPreference = findPreference<Preference>("feedback")
        emailPreference!!.setOnPreferenceClickListener { it ->
            val emailIntent = Intent(Intent.ACTION_SENDTO)
            emailIntent.data = Uri.parse("mailto:")
            val recipient = "marjanovicm314@gmail.com" // Replace your email id here
            emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "DailyLog feedback") // Replace your title with "TITLE"
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Feedback here")

            requireContext().startActivity(
                Intent.createChooser(
                    emailIntent,
                    "Choose email client.."
                )
            )
            true
        }

        preferenceChangeListener = SharedPreferences.OnSharedPreferenceChangeListener {sharedPreferences, key ->
            val preference = findPreference<Preference>(key)
            println("in prefchangelistener")
            when(preference){
                is SwitchPreferenceCompat -> {
                    println("in switchprefcompat")
                    if(key == "darkMode"){
                        if(preference.isChecked){
                            println("is checked")
                            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
                        }
                        else{
                            println("is not checked")
                            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences
            ?.registerOnSharedPreferenceChangeListener(preferenceChangeListener)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences
            ?.unregisterOnSharedPreferenceChangeListener(preferenceChangeListener)
    }

}