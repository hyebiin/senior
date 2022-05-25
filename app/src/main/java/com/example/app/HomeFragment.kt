package com.example.app

import android.content.Context
import android.media.Ringtone
import android.media.RingtoneManager
import android.media.SoundPool
import android.net.Uri
import android.os.*
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
import java.net.Socket
import android.system.Os.socket
import android.system.Os.socket
import android.system.Os.socket
import android.system.Os.socket








class HomeFragment : Fragment() {

    lateinit var name: String

    var st_date: String? = null
    val soundPool = SoundPool.Builder().build()

    private val handler = Handler()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //소켓통신 시작
        //잠금장치상태값받기, 이미지버튼변경
        ClientThread("home").start()

        openBtn.setOnClickListener {
            ClientThread("open").start()

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
                //Toast.makeText(getActivity(), filePath.toString(), Toast.LENGTH_LONG).show()
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
            ClientThread("clos").start()

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
                //Toast.makeText(getActivity(), filePath.toString(), Toast.LENGTH_LONG).show()
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

    inner class ClientThread(var para: String) : Thread() {
        override fun run() {
            try {
                var socket = Socket("192.168.139.128", 7777)    //소켓 연결(우분투IPv4주소, 포트번호)

                if (para === "home") {
                    var input = socket.getInputStream() //InputStream 생성
                    var output = socket.getOutputStream()   //OutputStream 생성
                    output.write(para.toByteArray(Charsets.UTF_8))  //서버에 액티비티명(home) 전달
                    output.flush()

                    var rb = input.readBytes()
                    var state=rb.toString(Charsets.UTF_8)
                    var clos="clos"

                    handler.post {
                        Toast.makeText(getActivity(), state, Toast.LENGTH_LONG).show()
                        if (state.contains(clos)) {
                            openBtn.visibility = VISIBLE
                            closeBtn.visibility = INVISIBLE
                        } else {
                            openBtn.visibility = INVISIBLE
                            closeBtn.visibility = VISIBLE
                        }
                    }

                }

                if (para == "open") {
                    var input1 = socket.getInputStream() //InputStream 생성
                    var output1 = socket.getOutputStream()   //OutputStream 생성
                    output1.write(para.toByteArray(Charsets.UTF_8))
                    output1.flush()

                    var rb1 = input1.readBytes()
                    var set=rb1.toString(Charsets.UTF_8)

                    if (set.contains("no")) {
                        handler.post {
                            val vibrator =
                                context!!.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                vibrator.vibrate(
                                    VibrationEffect.createOneShot(
                                        1000,
                                        VibrationEffect.DEFAULT_AMPLITUDE
                                    )
                                )
                                Toast.makeText(getActivity(), set, Toast.LENGTH_LONG).show()
                            } else {
                                vibrator.vibrate(1000)
                            }
                        }
                    }
                    else {
                        handler.post {
                            Toast.makeText(getActivity(), "fail", Toast.LENGTH_LONG).show()
                        }
                    }
                }

                if(para.contains("clos")) {
                    var output2 = socket.getOutputStream()   //OutputStream 생성
                    output2.write(para.toByteArray(Charsets.UTF_8))
                    output2.flush()
                }

                socket.close()  //소켓 종료

            } catch (e: Exception) {
                e.printStackTrace() //오류발생 시 로그 출력
            }
        }
    }
}