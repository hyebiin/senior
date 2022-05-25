package com.example.app

import android.content.Context
import android.content.Intent
import android.media.SoundPool
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_set.*
import kotlinx.android.synthetic.main.fragment_user.*

class SetFragment : Fragment() {

    val soundPool = SoundPool.Builder().build()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?
    ) : View ? {
        return inflater.inflate (com.example.app.R.layout.fragment_set, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        manualBtn.setOnClickListener {
            val soundId = soundPool.load(context, R.raw.manual,1)
            Thread.sleep(1000)
            soundPool.play(soundId,1.0f, 1.0f,0,0,1.0f)
            val intent = Intent(context,ManualActivity::class.java)
            startActivity(intent)
        }

        blueBtn.setOnClickListener {
            val soundId = soundPool.load(context, R.raw.blue,1)
            Thread.sleep(1000)
            soundPool.play(soundId,1.0f, 1.0f,0,0,1.0f)
        }

    }
}
