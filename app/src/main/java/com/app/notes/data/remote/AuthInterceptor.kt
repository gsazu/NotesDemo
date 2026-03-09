package com.app.notes.data.remote

import android.content.SharedPreferences
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestBuilder = request.newBuilder()

        // Check if the endpoint requires Authentication
        // By default, we assume it DOES NOT unless we add @Headers("Requires-Auth: true")
        val requiresAuth = request.header("Requires-Auth") == "true"

        if (requiresAuth) {
            val token = sharedPreferences.getString("auth_token", null)
            if (!token.isNullOrEmpty()) {
                requestBuilder.addHeader("Authorization", "Bearer $token")
            }
            // Remove the dummy header so it doesn't get sent to the server
            requestBuilder.removeHeader("Requires-Auth")
        }

        return chain.proceed(requestBuilder.build())
    }
}
