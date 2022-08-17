package com.example.app

import android.media.SoundPool
import android.os.*
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import java.io.*


class HomeFragment : Fragment() {

    lateinit var name: String

    var st_date: String? = null
    val soundPool = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        SoundPool.Builder().build()
    } else {
        TODO("VERSION.SDK_INT < LOLLIPOP")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //서버에서 잠금장치 상태값 받아오기
        var response = ""
        val job = GlobalScope.launch {
            var request = Requester()
            response = request.Read("https://hyeonyeong.site/Data/GetStateLocker?idx=1")
            Log.d(tag, "result : " + response)

            //현재 잠겨있을때
            if (response.toInt() == 0) {
                GlobalScope.launch {
                    withContext(Dispatchers.Main) {
                        openBtn.visibility = VISIBLE
                        closeBtn.visibility = INVISIBLE
                    }
                }
            }
            //현재 열려있을때
            if (response.toInt() == 1) {
                GlobalScope.launch {
                    withContext(Dispatchers.Main) {
                        openBtn.visibility = INVISIBLE
                        closeBtn.visibility = VISIBLE
                    }
                }
            }
        }

        //도어락 열기
        openBtn.setOnClickListener {
            //잠금장치 상태값 전달
            var response = ""
            val job = GlobalScope.launch {
                var request = Requester()
                response = request.Send("https://hyeonyeong.site/Data/InsertStateLocker?idx=1&state=1")
                Log.d(tag, "result : " + response)
            }

            openBtn.visibility = INVISIBLE
            closeBtn.visibility = VISIBLE

            //출입시간 알아내기
            val long_now: Long = System.currentTimeMillis()
            val st_now = Date(long_now)
            val st_format = SimpleDateFormat("yyyy-MM-dd kk:mm:ss E", Locale("ko", "kr"))
            st_date = st_format.format(st_now)
            val txt = "unlock : " + st_date

            //기존 출입시간 읽어오기
            var filePath = context!!.getExternalFilesDir(null)
            val file = File(filePath, "time.txt")
            if (!file.exists()) {
                file.createNewFile()
            }
            val reader = BufferedReader(FileReader(file))
            var result = ""
            var line: String? = null
            while (true) {
                line = reader.readLine() ?: break
                result = result + (line + "\n")
            }
            reader.close()
            //기존+현재 출입시간 파일(외부저장소)에 저장
            val writer = FileWriter(file, false)
            var time = result + "\n" + txt
            writer.write(time)
            writer.close()
            Toast.makeText(getActivity(), st_date, Toast.LENGTH_LONG).show()

            try {
                val soundId = soundPool.load(context, R.raw.lock, 1)
                Thread.sleep(1000)
                soundPool.play(soundId, 1.0f, 1.0f, 0, 0, 1.0f)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        //도어락 닫기
        closeBtn.setOnClickListener {
            //잠금장치 상태값 전달
            var response = ""
            val job = GlobalScope.launch {
                var request = Requester()
                response = request.Send("https://hyeonyeong.site/Data/InsertStateLocker?idx=1&state=0")
                Log.d(tag, "result : " + response)
            }

            openBtn.visibility = VISIBLE
            closeBtn.visibility = INVISIBLE

            //출입시간 알아내기
            val long_now: Long = System.currentTimeMillis()
            val st_now = Date(long_now)
            val st_format = SimpleDateFormat("yyyy-MM-dd kk:mm:ss E", Locale("ko", "kr"))
            st_date = st_format.format(st_now)
            val txt = "lock : " + st_date

            //기존 출입시간 읽어오기
            var filePath = context!!.getExternalFilesDir(null)
            val file = File(filePath, "time.txt")
            if (!file.exists()) {
                file.createNewFile()
            }
            val reader1 = BufferedReader(FileReader(file))
            var result1 = ""
            var line1: String? = null
            while (true) {
                line1 = reader1.readLine() ?: break
                result1 = result1 + (line1 + "\n")
            }
            reader1.close()

            ///기존+현재 출입시간 파일(외부저장소)에 저장
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
}