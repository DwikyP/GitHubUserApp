package com.dp13.githubuserapp

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.CheckBoxPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference

class GithubPreferenceFragment: PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {
    private lateinit var alarmReceiver: AlarmReceiver
    private lateinit var ALARM: String
    private lateinit var isReminderPreference: SwitchPreference

    override fun onCreatePreferences(bundle: Bundle?, s: String?) {
        addPreferencesFromResource(R.xml.preferences)
        alarmReceiver = AlarmReceiver()
        init()
        setSummaries()
    }

    private fun init() {
        ALARM = resources.getString(R.string.alarm)
        isReminderPreference = findPreference<CheckBoxPreference>(ALARM) as SwitchPreference
    }

    private fun setSummaries() {
        val sh = preferenceManager.sharedPreferences
        isReminderPreference.isChecked = sh.getBoolean(ALARM, false)
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (key == ALARM) {
            val repeatTime = "09:00"
            val repeatMessage = "Reminder Github Apps"
            isReminderPreference.isChecked = sharedPreferences.getBoolean(ALARM, false)
            if(isReminderPreference.isChecked)
                alarmReceiver.setRepeatingAlarm(requireContext(), AlarmReceiver.TYPE_REPEATING, repeatTime, repeatMessage)
            else
                alarmReceiver.cancelAlarm(requireContext(), AlarmReceiver.TYPE_REPEATING)
        }
    }
}