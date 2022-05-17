package com.example.app

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_user.*
import android.R
import android.media.SoundPool


class UserFragment : Fragment() {

    val soundPool = SoundPool.Builder().build()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?
    ) : View ? {
        return inflater.inflate (com.example.app.R.layout.fragment_user, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        userBtn.setOnClickListener {
            val soundId = soundPool.load(context, com.example.app.R.raw.save,1)
            Thread.sleep(1000)
            soundPool.play(soundId,1.0f, 1.0f,0,0,1.0f)
            val intent = Intent(context,UserActivity::class.java)
            startActivity(intent)
        }

        timeBtn.setOnClickListener {
            val soundId = soundPool.load(context, com.example.app.R.raw.time,1)
            Thread.sleep(1000)
            soundPool.play(soundId,1.0f, 1.0f,0,0,1.0f)
            Toast.makeText(activity,"출입시간 조회", Toast.LENGTH_LONG).show()
            val intent = Intent(context,TimeActivity::class.java)
            startActivity(intent)
        }

        albumBtn.setOnClickListener {
            val soundId = soundPool.load(context, com.example.app.R.raw.album,1)
            Thread.sleep(1000)
            soundPool.play(soundId,1.0f, 1.0f,0,0,1.0f)
            Toast.makeText(activity,"출입사진 조회", Toast.LENGTH_LONG).show()
            val intent = Intent(context,AlbumActivity::class.java)
            startActivity(intent)
        }
    }
}
