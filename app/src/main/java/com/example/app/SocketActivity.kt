package com.example.app

import android.os.AsyncTask
import java.lang.Exception
import java.net.Socket

object SocketActivity {

    class SocketAsyncTask : AsyncTask<String, Void, String>() {

        override fun doInBackground(vararg params: String?): String {   //params는 어떤 액티비티에서 소켓통신을 요청했는지 서버에게 알려주기 위한 선언(HomeFragment는 1, UserActivity는 비밀번호)
                try {
                    var socket = Socket("192.168.139.128", 7777)    //소켓 연결(우분투IPv4주소, 포트번호)

                    var input = socket.getInputStream() //InputStream 생성
                    //val dataInputStream = DataInputStream(input)

                    var output = socket.getOutputStream()   //OutputStream 생성


                    ///////////////////////////////////////////소스코드 바꿔야함////////////////////////////////////////////////
                    if(params[0].toString()=="home ") {
                        val data = params[0].toString()  //각 액티비티에서 받은 params값을 data에 저장
                        var setBtn=""

                        output.flush()  //스트림에 남아있는 데이터(ex.공백) 버리기
                        output.write(data.toByteArray(Charsets.UTF_8))  //서버에 액티비티별 번호(home) 전달
                        output.flush()

                        input.bufferedReader (Charsets.UTF_8).forEachLine { //서버에서 잠금장치 상태값 받아오기
                            setBtn = "$it"
                        }
                        return setBtn
                    }

                    else {
                        val data = params[0].toString()  //각 액티비티에서 받은 params값을 data에 저장
                        output.flush()
                        output.write(data.toByteArray(Charsets.UTF_8))  //서버에 비밀번호 전달
                        output.flush()  //스트림에 남아있는 데이터(ex.공백) 버리기
                        return ""
                    }
                    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////


                    socket.close()  //소켓 종료

                } catch (e: Exception) {
                    e.printStackTrace() //오류발생 시 로그 출력
                }
            return ""
        }
    }
}