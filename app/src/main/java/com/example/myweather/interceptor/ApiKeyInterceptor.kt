package com.example.myweather.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor : Interceptor{

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val url = request.url
            .newBuilder()
<<<<<<< HEAD
            .addQueryParameter(APPID, "{APPID}")
=======
            .addQueryParameter(APPID, "803396a4ef9eb53bf7eced4afc27d7e9")
>>>>>>> c99a2ed (-UI page added.)
            .build()

        val requestBuilder = request.newBuilder()
            .url(url)

        return chain.proceed(requestBuilder.build())

    }

    companion object {
        private const val APPID = "appid"
    }
}