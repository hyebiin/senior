package com.example.app

import android.content.Intent
import android.media.SoundPool
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_set.*

class SetFragment : Fragment() {

    val soundPool = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        SoundPool.Builder().build()
    } else {
        TODO("VERSION.SDK_INT < LOLLIPOP")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?
    ) : View ? {
        return inflater.inflate (com.example.app.R.layout.fragment_set, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //'메뉴얼보기'로 이동
        manualBtn.setOnClickListener {
            val soundId = soundPool.load(context, R.raw.manual,1)
            Thread.sleep(1000)
            soundPool.play(soundId,1.0f, 1.0f,0,0,1.0f)
            val intent = Intent(context,ManualActivity::class.java)
            startActivity(intent)
        }
    }
}
