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

    private lateinit var binding: FragmentSettingsBinding
    private var mSwitchPreference: SwitchPreferenceCompat? = null
    private var preferenceChangeListener: SharedPreferences.OnSharedPreferenceChangeListener? = null
   // private val prefs = PreferenceManager.getDefaultSharedPreferences()

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
            //emailIntent.type = "text/plain" // set content type here

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

    fun sendEmail(ctx: Context) {
        val emailIntent = Intent(Intent.ACTION_SEND)
        val recipient = "marjanovicm314@@gmail.com" // Replace your email id here
        emailIntent.putExtra(Intent.EXTRA_EMAIL, recipient)
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "TITLE") // Replace your title with "TITLE"
        emailIntent.putExtra(Intent.EXTRA_TEXT, "TEXT")
        emailIntent.type = "text/plain" // set content type here
        ctx.startActivity(
            Intent.createChooser(
                emailIntent,
                "Send E-mail..."
            )
        ) // it will provide you supported all app to send email.
    }

   /* override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(layoutInflater)
        mSwitchPreference =
            preferenceManager.findPreference<Preference>("darkMode") as SwitchPreferenceCompat?

        preferenceChangeListener = SharedPreferences.OnSharedPreferenceChangeListener {sharedPreferences, key ->
            val preference = findPreference<Preference>(key)
            when(preference){
                is SwitchPreferenceCompat -> {
                    if(key == "darkMode"){
                        if(preference.isChecked){
                            AppCompatDelegate.MODE_NIGHT_YES
                        }
                        else{
                            AppCompatDelegate.MODE_NIGHT_NO
                        }
                    }
                }
            }
        }

        return binding.root
    }*/

}