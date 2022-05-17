package com.example.app

import android.content.Intent
import android.media.SoundPool
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_time.*
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

class TimeActivity : AppCompatActivity() {

    val soundPool = SoundPool.Builder().build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time)

        var filePath = this.getExternalFilesDir(null)
        val file = File(filePath,"time.txt")
        val reader1 = BufferedReader(FileReader(file))
        var result1 = ""
        var line1: String?=null
        while(true) {
            line1=reader1.readLine() ?: break
            result1=result1+(line1+"\n")
        }

        date_time.setText(result1)

        println("불러온 내용 : $result1")
        reader1.close()

        time_backBtn.setOnClickListener {
            val soundId = soundPool.load(this, R.raw.home,1)
            Thread.sleep(1000)
            soundPool.play(soundId,1.0f, 1.0f,0,0,1.0f)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}