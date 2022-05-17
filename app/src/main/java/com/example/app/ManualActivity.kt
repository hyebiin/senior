package com.example.app

import android.content.Intent
import android.media.SoundPool
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_manual.*
import kotlinx.android.synthetic.main.activity_user.user_backBtn

class ManualActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manual)

        val soundPool = SoundPool.Builder().build()

        user_backBtn.setOnClickListener {
            val soundId = soundPool.load(this, R.raw.home,1)
            Thread.sleep(1000)
            soundPool.play(soundId,1.0f, 1.0f,0,0,1.0f)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        voiceBtn.setOnClickListener {
            val soundId = soundPool.load(this, R.raw.manualvoice,1)
            Thread.sleep(5000)
            soundPool.play(soundId,1.0f, 1.0f,0,0,1.0f)
        }
    }

}