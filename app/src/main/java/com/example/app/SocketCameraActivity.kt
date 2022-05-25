package com.example.app

import android.app.Activity
import android.content.Context
import android.os.AsyncTask
import android.view.View
import android.widget.ImageButton
import java.lang.Exception
import java.net.Socket
import android.os.Build
import android.os.VibrationEffect
import androidx.core.content.ContextCompat.getSystemService
import android.os.Vibrator

class SocketCameraActivity(var page: String, var oBtn: ImageButton, var cBtn: ImageButton) :
    AsyncTask<Void, Void, String>() {
    override fun doInBackground(vararg params: Void?): String? {   //params는 어떤 액티비티에서 소켓통신을 요청했는지 서버에게 알려주기 위한 선언(HomeFragment는 1, UserActivity는 비밀번호)
        try {
            var socket = Socket("192.168.139.128", 7778)    //소켓 연결(우분투IPv4주소, 포트번호)

            var input = socket.getInputStream() //InputStream 생성

            //각 액티비티에서 받은 params값("home" or "비밀번호 네자리")을 activityData에 저장
            var activityData = page

            var setBtn: String=""
            input.bufferedReader(Charsets.UTF_8).forEachLine {
                //사람수 받아오기
                setBtn = "$it"
            }
            oBtn.visibility = View.INVISIBLE
            cBtn.visibility = View.VISIBLE
            if (setBtn =="1") {
            }
            else {
                //진동알림
                /*
                val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(
                        VibrationEffect.createOneShot(
                            1000,
                            VibrationEffect.DEFAULT_AMPLITUDE
                        )
                    )
                } else {
                    vibrator.vibrate(1000)
                }
                 */
            }
            socket.close()  //소켓 종료
        } catch (e: Exception) {
            e.printStackTrace() //오류발생 시 로그 출력
        }
        return ""
    }
}