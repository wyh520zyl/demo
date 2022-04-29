package wuge.social.com.http

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody

import okio.Buffer

class LoggingInterceptor : Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val time_start = System.nanoTime()
        val request = chain.request()
        val response = chain.proceed(request)
        val buffer= Buffer()
        request.body?.writeTo(buffer)
        val  requestBodyStr = buffer.readUtf8()
        val bussinessData = response.body?.string()?:"response body null"
        val mediaType = response.body?.contentType()
        val newBody = ResponseBody.create(mediaType,bussinessData)
        val newResponse = response.newBuilder().body(newBody).build()
        val time_end = System.nanoTime()
        Log.d("LoggingInterceptor->OKHTTP",String.format("Received response for %s in %.1fms >>> %s",request.url,(time_end-time_start)/1e6,bussinessData))
        return newResponse
    }
}