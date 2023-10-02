package com.example.githubuser.ui.splashscreen

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.githubuser.databinding.ActivitySplashScreenBinding
import com.example.githubuser.ui.main.MainActivity
import com.example.githubuser.ui.setting.SettingPreferences
import com.example.githubuser.ui.setting.SettingViewModel
import com.example.githubuser.ui.setting.SettingViewModelFactory
import com.example.githubuser.ui.setting.dataStore

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val pref = SettingPreferences.getInstance(application.dataStore)
        val settingViewModel by viewModels<SettingViewModel> {
            SettingViewModelFactory.getInstance(pref)
        }

        settingViewModel.getThemeSetting().observe(this) { isDarkModeActive ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        val countdownTimer = object : CountDownTimer(DURATION, INTERVAL) {
            override fun onTick(millisUntilFinished: Long) {
                //tidak digunakan karena tidak diperlukan ada countdown
            }
            override fun onFinish() {
                navigateToMainActivity()
            }
        }
        countdownTimer.start()
    }
    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    companion object {
        private const val INTERVAL = 1000L
        private const val DURATION = 3000L
    }
}