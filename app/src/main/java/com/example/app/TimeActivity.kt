package com.example.app

import android.content.Intent
import android.media.SoundPool
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_time.*
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.FileWriter

class TimeActivity : AppCompatActivity() {

    val soundPool = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        SoundPool.Builder().build()
    } else {
        TODO("VERSION.SDK_INT < LOLLIPOP")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time)

        //파일 내용(출입시간) 읽어오기
        var filePath = this.getExternalFilesDir(null)
        val file = File(filePath,"time.txt")
        val reader1 = BufferedReader(FileReader(file))
        //파일 내용(출입시간) 출력
        var result1 = ""
        var line1: String?=null
        while(true) {
            line1=reader1.readLine() ?: break
            result1=result1+(line1+"\n")
        }
        date_time.setText(result1)
        reader1.close()

        //'홈'으로 이동
        time_backBtn.setOnClickListener {
            val soundId = soundPool.load(this, R.raw.home,1)
            Thread.sleep(1000)
            soundPool.play(soundId,1.0f, 1.0f,0,0,1.0f)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        //출입시간 삭제
        stop_btn.setOnClickListener {
            val soundId = soundPool.load(this, R.raw.stop,1)
            Thread.sleep(1000)
            soundPool.play(soundId,1.0f, 1.0f,0,0,1.0f)

            val writer = FileWriter(file, false)
            var time =""
            writer.write(time)
            writer.close()

            Toast.makeText(applicationContext, "정보삭제완료", Toast.LENGTH_SHORT).show()
            date_time.setText(time)
        }
    }
}