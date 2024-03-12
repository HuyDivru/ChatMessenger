package com.example.chatmessager.notifications.network

import com.example.chatmessager.notifications.Constants.Companion.CONTENT_TYPE
import com.example.chatmessager.notifications.Constants.Companion.SERVER_KEY
import com.example.chatmessager.notifications.entity.PushNotification
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationAPI {
    @Headers("Authorization:key=$SERVER_KEY","Content-Type:$CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun postNotification(
        @Body notification: PushNotification
    ):Response<ResponseBody>
}