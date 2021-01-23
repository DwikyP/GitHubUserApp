package com.dp13.githubuserapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class GithubUserSettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_github_user_setting)

        supportFragmentManager.beginTransaction().add(R.id.setting_holder, GithubPreferenceFragment()).commit()
    }
}