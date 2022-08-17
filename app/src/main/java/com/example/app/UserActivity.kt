package com.example.app

import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.media.SoundPool
import android.os.*
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_user.*
import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class UserActivity : AppCompatActivity() {

    //DB
    lateinit var myHelper: MainActivity.myDBHelper
    lateinit var sqlDB: SQLiteDatabase
    //사용자 정보(이름, 비밀번호, 연락처) 입력
    lateinit var username: EditText
    lateinit var userpass: EditText
    lateinit var usertel1: EditText
    //저장하기, 수정하기 버튼
    lateinit var user_saveBtn: Button
    lateinit var user_modiBtn: Button
    //사용자 정보 문자열로 저장
    lateinit var name: String
    lateinit var pass: String
    lateinit var tel1: String

    val soundPool = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        SoundPool.Builder().build()
    } else {
        TODO("VERSION.SDK_INT < LOLLIPOP")
    }

    @SuppressLint("WrongThread")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        username = findViewById(R.id.username)
        usertel1 = findViewById(R.id.usertel1)
        userpass = findViewById(R.id.userpass)
        user_saveBtn = findViewById(R.id.user_saveBtn)
        user_modiBtn = findViewById(R.id.user_modiBtn)

        myHelper = MainActivity.myDBHelper(this)

        //DB내용 읽어옴
        sqlDB = myHelper.readableDatabase
        var cursor: Cursor
        cursor = sqlDB.rawQuery("SELECT * FROM userTABLE;", null)
        while (cursor.moveToNext()) {
            name = cursor.getString(0)
            pass = cursor.getString(1)
            tel1 = cursor.getString(2)

            //읽어온 정보를 화면에 표시
            username.setText(name)
            if (name !== "") {
                username.setEnabled(false)
                user_saveBtn.visibility = INVISIBLE
                user_modiBtn.visibility = VISIBLE
            }
            userpass.setText(pass)
            usertel1.setText(tel1)
        }
        cursor.close()
        sqlDB.close()

        //'홈'으로 이동
        user_backBtn.setOnClickListener {
            val soundId = soundPool.load(this, R.raw.home, 1)
            Thread.sleep(1000)
            soundPool.play(soundId, 1.0f, 1.0f, 0, 0, 1.0f)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        //저장하기
        user_saveBtn.setOnClickListener {
            if (username.text.toString() == "" || userpass.text.toString() == "" || usertel1.text.toString() == "") {
                Toast.makeText(applicationContext, "!!!모든 정보를 입력하세요!!!", Toast.LENGTH_SHORT).show()
            } else {
                //DB에 사용자 정보 저장
                sqlDB = myHelper.writableDatabase
                sqlDB.execSQL(
                    "INSERT INTO userTABLE VALUES ( '" + username.text.toString()
                            + "' , '" + userpass.text.toString()
                            + "', '" + usertel1.text.toString()
                            //+ "' , '" + cam + "' );"
                )

                //사용자 정보 하나(user)로 묶기
                var u_name=username.text.toString()
                var u_pw = userpass.text.toString()
                var u_tel = usertel1.text.toString()
                var user = "name="+u_name+"&password="+u_pw+"&tel="+u_tel

                //서버로 사용자 정보 보내기
                var responseUser = ""
                val job = GlobalScope.launch {
                    var requestUser = Requester()
                    responseUser = requestUser.User("https://hyeonyeong.site/Data/InsertUser?$user")
                    Log.d("결과", "result : " + responseUser)
                }

                val soundId = soundPool.load(this, R.raw.user_save, 1)
                Thread.sleep(1000)
                soundPool.play(soundId, 1.0f, 1.0f, 0, 0, 1.0f)
                Toast.makeText(applicationContext, "저장완료", Toast.LENGTH_SHORT).show()
                username.setEnabled(false)
                user_saveBtn.visibility = INVISIBLE
                user_modiBtn.visibility = VISIBLE

                sqlDB.close()
            }
        }

        //수정하기
        user_modiBtn.setOnClickListener {
            if (username.text.toString() == "" || userpass.text.toString() == "" || usertel1.text.toString() == "") {
                Toast.makeText(applicationContext, "최소 3가지 정보를 입력하세요", Toast.LENGTH_SHORT).show()
            }
            else {
                //DB에 사용자 정보 업데이트
                sqlDB = myHelper.writableDatabase
                sqlDB.execSQL(
                    "UPDATE userTABLE SET pass = '" + userpass.text.toString()
                            + "' , tel1 = '" + usertel1.text.toString()
                            //+ "' , bt = '" + cam
                            + "' WHERE name ='" + username.text.toString() + "';"
                )

                //사용자 정보 하나(user)로 묶기
                var u_name=username.text.toString()
                var u_pw = userpass.text.toString()
                var u_tel = usertel1.text.toString()
                var user = "name="+u_name+"&password="+u_pw+"&tel="+u_tel

                // 서버로 사용자정보 보내기
                var responseUser = ""
                val job = GlobalScope.launch {
                    var requestUser = Requester()
                    responseUser = requestUser.User("https://hyeonyeong.site/Data/InsertUser?$user")
                    Log.d("결과", "result : " + responseUser)
                }

                val soundId = soundPool.load(this, R.raw.user_update, 1)
                Thread.sleep(1000)
                soundPool.play(soundId, 1.0f, 1.0f, 0, 0, 1.0f)
                Toast.makeText(applicationContext, "수정완료", Toast.LENGTH_SHORT).show()

                sqlDB.close()
            }
        }

        //DB의 사용자 정보 초기화
        user_delBtn.setOnClickListener {
            sqlDB = myHelper.writableDatabase
            myHelper.onUpgrade(sqlDB, 1, 2)
            val soundId = soundPool.load(this, R.raw.user_init, 1)
            Thread.sleep(1000)
            soundPool.play(soundId, 1.0f, 1.0f, 0, 0, 1.0f)
            user_saveBtn.visibility = VISIBLE
            user_modiBtn.visibility = INVISIBLE
            Toast.makeText(applicationContext, "초기화완료", Toast.LENGTH_SHORT).show()
            sqlDB.close()
        }
    }

}
