package com.example.app

import android.content.Intent
import android.media.SoundPool
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_album.*
import java.lang.Thread.sleep

class AlbumActivity : AppCompatActivity() {

    val soundPool = SoundPool.Builder().build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album)

        //서버에서 이미지파일? 받아서 이미지뷰에 출력시키기

        album_backBtn.setOnClickListener {
            val soundId = soundPool.load(this, R.raw.home,1)
            sleep(1000)
            soundPool.play(soundId,1.0f, 1.0f,0,0,1.0f)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}