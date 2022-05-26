package com.example.app

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.media.SoundPool
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    lateinit var myHelper : myDBHelper
    lateinit var sqlDB : SQLiteDatabase

    val soundPool = SoundPool.Builder().build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myHelper = myDBHelper(this)

        var bottom_menu = findViewById(R.id.bottom_menu) as BottomNavigationView

        bottom_menu.run{ setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.home -> {
                    val soundId = soundPool.load(context, R.raw.home,1)
                    Thread.sleep(1000)
                    soundPool.play(soundId,1.0f, 1.0f,0,0,1.0f)
                    val homeFragment=HomeFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.bottom, homeFragment).commit()
                }
                R.id.user -> {
                    val soundId = soundPool.load(context, R.raw.user,1)
                    Thread.sleep(1000)
                    soundPool.play(soundId,1.0f, 1.0f,0,0,1.0f)
                    val userFragment=UserFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.bottom, userFragment).commit()
                }
                R.id.set -> {
                    val soundId = soundPool.load(context, R.raw.setting,1)
                    Thread.sleep(1000)
                    soundPool.play(soundId,1.0f, 1.0f,0,0,1.0f)
                    val setFragment=SetFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.bottom,setFragment).commit()
                }
            }
            true
        }
            selectedItemId=R.id.home
        }
    }

    //사용자 정보 데베
    class myDBHelper(context: Context) : SQLiteOpenHelper(context, "userTABLE", null, 1) {
        override fun onCreate(p0: SQLiteDatabase?) {
            p0!!.execSQL("CREATE TABLE userTABLE(name CHAR(30) PRIMARY KEY, pass CHAR(30), tel1 CHAR(30), tel2 CHAR(30), tel3 CHAR(30), bt CHAR(30));")
        }
        override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
            p0!!.execSQL("DROP TABLE IF EXISTS userTABLE")
            onCreate(p0)
        }
    }

}
