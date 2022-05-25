package com.example.app

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.media.SoundPool
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_user.*
import java.lang.Exception
import java.io.ByteArrayOutputStream
import android.graphics.BitmapFactory

class UserActivity : AppCompatActivity() {

    lateinit var myHelper: MainActivity.myDBHelper
    lateinit var sqlDB: SQLiteDatabase

    lateinit var username: EditText
    lateinit var userpass: EditText
    lateinit var usertel1: EditText
    lateinit var usertel2: EditText
    lateinit var usertel3: EditText
    lateinit var userbt: ToggleButton
    lateinit var user_saveBtn: Button
    lateinit var user_modiBtn: Button
    lateinit var userimg: ImageView    //이미지뷰

    lateinit var name: String
    lateinit var pass: String
    lateinit var tel1: String
    lateinit var tel2: String
    lateinit var tel3: String
    lateinit var bt:String
    var img = ByteArray(100)

    lateinit var image: String
    lateinit var imgUri: String

    lateinit var bitmap: Bitmap

    private val GALLERY = 1

    lateinit var cam:String

    val soundPool = SoundPool.Builder().build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_user)

        username = findViewById(R.id.username)
        usertel1 = findViewById(R.id.usertel1)
        usertel2 = findViewById(R.id.usertel2)
        usertel3 = findViewById(R.id.usertel3)
        userpass = findViewById(R.id.userpass)
        user_saveBtn = findViewById(R.id.user_saveBtn)
        user_modiBtn = findViewById(R.id.user_modiBtn)
        userimg = findViewById(R.id.userImg)
        userbt = findViewById(R.id.camTog)

        myHelper = MainActivity.myDBHelper(this)

        //데베의 내용을 읽어옴
        sqlDB = myHelper.readableDatabase
        var cursor: Cursor
        cursor = sqlDB.rawQuery("SELECT * FROM userTABLE;", null)
        while (cursor.moveToNext()) {
            name = cursor.getString(0)
            pass = cursor.getString(1)
            tel1 = cursor.getString(2)
            tel2 = cursor.getString(3)
            tel3 = cursor.getString(4)
            bt = cursor.getString(5)
            img = cursor.getBlob(6)


            //가져온 값을 사용자 정보 페이지에 출력
            username.setText(name)
            if (name !== "") {
                username.setEnabled(false)
                user_saveBtn.visibility = INVISIBLE
                user_modiBtn.visibility = VISIBLE
            }
            userpass.setText(pass)
            usertel1.setText(tel1)
            usertel2.setText(tel2)
            usertel3.setText(tel3)
            if(bt=="USE") {
                userbt.setChecked(true)
            }
            else {
                userbt.setChecked(false)
            }
            val b = BitmapFactory.decodeByteArray(img, 0, img.size)
            userimg.setImageBitmap(b)

            //데베에 저장되어있는 경로를 비트맵으로 변경하여 이미지뷰에 출력(->문제 : 앱을 껐다가 다시 실행하면 안됨)
            //val uri = Uri.parse(image)
            //val bitmap1 = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
            //userimg.setImageBitmap(bitmap1)
        }
        cursor.close()
        sqlDB.close()

        //홈으로
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
                Toast.makeText(applicationContext, "최소 3가지 정보를 입력하세요", Toast.LENGTH_SHORT).show()
            } else {
                sqlDB = myHelper.writableDatabase
                sqlDB.execSQL(
                    "INSERT INTO userTABLE VALUES ( '" + username.text.toString()
                            + "' , '" + userpass.text.toString()
                            + "', '" + usertel1.text.toString()
                            + "' , '" + usertel2.text.toString()
                            + "' , '" + usertel3.text.toString()
                            + "' , bt = '" + cam
                            + "' , '" + img + "' );"
                )
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
                sqlDB = myHelper.writableDatabase
                sqlDB.execSQL(
                    "UPDATE userTABLE SET pass = '" + userpass.text.toString()
                            + "' , tel1 = '" + usertel1.text.toString()
                            + "' , tel2 = '" + usertel2.text.toString()
                            + "' , tel3 = '" + usertel3.text.toString()
                            + "' , bt = '" + cam
                            + "' , image = '" + img
                            + "' WHERE name ='" + username.text.toString() + "';"
                )
                val soundId = soundPool.load(this, R.raw.user_update, 1)
                Thread.sleep(1000)
                soundPool.play(soundId, 1.0f, 1.0f, 0, 0, 1.0f)
                Toast.makeText(applicationContext, "수정완료", Toast.LENGTH_SHORT).show()
                sqlDB.close()
            }
        }

        //정보초기화
        user_delBtn.setOnClickListener {
            sqlDB = myHelper.writableDatabase
            myHelper.onUpgrade(sqlDB, 1, 2)
            val soundId = soundPool.load(this, R.raw.user_init, 1)
            Thread.sleep(1000)
            soundPool.play(soundId, 1.0f, 1.0f, 0, 0, 1.0f)
            Toast.makeText(applicationContext, "초기화완료", Toast.LENGTH_SHORT).show()
            sqlDB.close()
        }

        //사진 불러오기
        user_photoBtn.setOnClickListener {
            val soundId = soundPool.load(this, R.raw.user_img, 1)
            Thread.sleep(1000)
            soundPool.play(soundId, 1.0f, 1.0f, 0, 0, 1.0f)

            val intent: Intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.setType("image/*")
            startActivityForResult(intent, GALLERY)
        }

        //카메라 사용 여부 결정
        camTog.setOnClickListener {
            if(camTog.isChecked) {
                val soundId = soundPool.load(this, R.raw.camera_use,1)
                Thread.sleep(1000)
                soundPool.play(soundId,1.0f, 1.0f,0,0,1.0f)
                cam="USE"
                Toast.makeText(applicationContext,"카메라 사용", Toast.LENGTH_LONG).show()
            }
            else {
                val soundId = soundPool.load(this, R.raw.camera_not_use,1)
                Thread.sleep(1000)
                soundPool.play(soundId,1.0f, 1.0f,0,0,1.0f)
                cam="NOTUSE"
                Toast.makeText(applicationContext,"카메라 미사용", Toast.LENGTH_LONG).show()
            }
        }

    }

    //갤러리에서 이미지를 갖고오면 그 경로를 얻는 메소드
    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY) {
                var ImnageData: Uri? = data?.data   //이미지 경로 추출
                imgUri = ImnageData.toString()
                Toast.makeText(this, imgUri, Toast.LENGTH_SHORT).show()
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(contentResolver, ImnageData) //이미지경로 비트맵으로 변환
                    userimg.setImageBitmap(bitmap)  //갤러리에서 선택한 이미지를 이미지뷰에 출력
                    var stream = ByteArrayOutputStream()
                    img = stream.toByteArray()

                    Toast.makeText(applicationContext, "success", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(applicationContext, "fail", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
