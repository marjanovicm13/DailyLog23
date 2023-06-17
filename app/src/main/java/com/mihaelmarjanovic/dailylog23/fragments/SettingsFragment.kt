package com.mihaelmarjanovic.dailylog23.fragments

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.preference.*
import com.mihaelmarjanovic.dailylog23.R


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
            when(preference){
                is SwitchPreferenceCompat -> {
                    if(key == "darkMode"){
                        if(preference.isChecked){
                            AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
                        }
                        else{
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