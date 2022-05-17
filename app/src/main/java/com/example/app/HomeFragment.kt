package com.example.app

import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.media.SoundPool
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_home.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import java.io.*


class HomeFragment : Fragment() {

    lateinit var name: String

    var st_date: String? = null
    val soundPool = SoundPool.Builder().build()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val one="home"  //HomeFragment임을 알리기 위해 "home"전달
        val text=SocketActivity.SocketAsyncTask().execute(one) //소켓통신 시작 -> 서버에서 잠금장치 상태정보 받기
        Toast.makeText(getActivity(), text.toString(), Toast.LENGTH_LONG).show()    //확인용


        //서버에서 받은 text값(잠금장치 상태, 서버에서 임의로 열려있다고 가정했음)에 따라 잠금버튼 이미지 변경
        if(text!=null) {    //잠금장치가 열려있을 경우
            openBtn.visibility = INVISIBLE
            closeBtn.visibility = VISIBLE
        }
        else {  //잠금장치가 닫혀있을 경우
            openBtn.visibility = VISIBLE
            closeBtn.visibility = INVISIBLE
        }

        openBtn.setOnClickListener {
            openBtn.visibility = INVISIBLE
            closeBtn.visibility = VISIBLE
            val long_now: Long = System.currentTimeMillis()
            val st_now = Date(long_now)
            val st_format = SimpleDateFormat("yyyy-MM-dd kk:mm:ss E", Locale("ko", "kr"))
            st_date = st_format.format(st_now)
            val txt = "unlock : " + st_date

            //외부 저장소에 출입시간 저장
            var filePath = context!!.getExternalFilesDir(null)
            val file = File(filePath, "time.txt")

            if (!file.exists()) {
                file.createNewFile()
                Toast.makeText(getActivity(), filePath.toString(), Toast.LENGTH_LONG).show()
            }

            //외부 저장소 기존 내용 먼저 불러오기
            val reader = BufferedReader(FileReader(file))
            var result = ""
            var line: String? = null
            while (true) {
                line = reader.readLine() ?: break
                result = result + (line + "\n")
            }

            println("불러온 내용 : $result")
            reader.close()

            //기존+현재 출입시간 저장
            val writer = FileWriter(file, false)
            var time = result + "\n" + txt
            writer.write(time)
            writer.close()

            Toast.makeText(getActivity(), st_date, Toast.LENGTH_LONG).show()

            try {
                val alarm1: Uri =
                    RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                val a1: Ringtone = RingtoneManager.getRingtone(context, alarm1)
                a1.play()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        closeBtn.setOnClickListener {
            openBtn.visibility = VISIBLE
            closeBtn.visibility = INVISIBLE
            val long_now: Long = System.currentTimeMillis()
            val st_now = Date(long_now)
            val st_format = SimpleDateFormat("yyyy-MM-dd kk:mm:ss E", Locale("ko", "kr"))
            st_date = st_format.format(st_now)
            val txt = "lock : " + st_date

            //외부 저장소에 출입시간 저장
            var filePath = context!!.getExternalFilesDir(null)
            val file = File(filePath, "time.txt")
            if (!file.exists()) {
                file.createNewFile()
                Toast.makeText(getActivity(), filePath.toString(), Toast.LENGTH_LONG).show()
            }

            //외부 저장소 기존 내용 먼저 불러오기
            val reader1 = BufferedReader(FileReader(file))
            var result1 = ""
            var line1: String? = null
            while (true) {
                line1 = reader1.readLine() ?: break
                result1 = result1 + (line1 + "\n")
            }

            println("불러온 내용 : $result1")
            reader1.close()

            //기존+현재 출입시간 저장
            val writer = FileWriter(file, false)
            var time = result1 + "\n" + txt
            writer.write(time)
            writer.close()

            Toast.makeText(getActivity(), st_date, Toast.LENGTH_LONG).show()

            try {
                val soundId = soundPool.load(context, R.raw.unlock, 1)
                Thread.sleep(1000)
                soundPool.play(soundId, 1.0f, 1.0f, 0, 0, 1.0f)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    //일단 사용안함
    /*
    fun isExternalStorageWritable(): Boolean {
        when {
            //외부저장장치 상태가 media-mounted면 사용가능
            Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED -> return true
            else -> return false
        }
    }
     */

}