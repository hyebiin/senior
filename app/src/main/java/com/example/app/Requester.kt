package com.example.app
import android.util.Log
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody

class Requester ()
{
    //잠금장치 상태값 받아오기
     fun Read(url:String):String
    {
        var client = OkHttpClient.Builder().build()
        val request = Request.Builder()
            .url(url)
            .build()
        var response1=   client.newCall(request).execute()

        var result = response1.peekBody(99999L).string()
        Log.d(url,result)

        return result
    }

    //잠금장치 상태값 전달하기
    fun Send(url2:String):String
    {
        var client = OkHttpClient.Builder().build()
        val request2 = Request.Builder()
            .url(url2)
            .build()
        var response2=   client.newCall(request2).execute()

        var result2 = response2.peekBody(99999L).string()
        Log.d(url2,result2)

        return result2
    }

    //사용자 정보 전달하기
    fun User(url3:String):String
    {
        var client = OkHttpClient.Builder().build()

        val request3 = Request.Builder()
            .url(url3)
            .build()

        var response3= client.newCall(request3).execute()
        var result3 = response3.peekBody(99999L).string()
        Log.d(url3,result3)

        return result3
    }
}