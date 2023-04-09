package com.example.hritage.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import com.example.hritage.R
import com.example.hritage.databinding.ActivityHitkSplashScreenBinding
import java.util.*

@SuppressLint("CustomSplashScreen")
class HitkSplashScreen : AppCompatActivity() {
    private var binding:ActivityHitkSplashScreenBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHitkSplashScreenBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val animSequential = AnimationUtils.loadAnimation(
            applicationContext,
            R.anim.splash_screen_text_anim
        )
        val animSequential2 = AnimationUtils.loadAnimation(
            applicationContext,
        R.anim.splash_screen_text_anim_2
        )
        val animSequential3 = AnimationUtils.loadAnimation(
            applicationContext,
            R.anim.splash_screen_text_anim_3
        )
        val animSequential4 = AnimationUtils.loadAnimation(
            applicationContext,
            R.anim.splash_screen_text_anim_4
        )
        binding?.h?.startAnimation(animSequential)
        binding?.i?.startAnimation(animSequential2)
        binding?.t?.startAnimation(animSequential3)
        binding?.k?.startAnimation(animSequential4)
        binding?.hitkLogo?.startAnimation(animSequential2)

        val timer= Timer()
        timer.schedule(object :TimerTask(){
            /**
             * The action to be performed by this timer task.
             */
            override fun run() {
                val intent=Intent(this@HitkSplashScreen, MainActivity::class.java)
                startActivity(intent)
                finish()
            }

        },3000)
//
    }
}