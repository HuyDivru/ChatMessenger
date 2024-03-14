@file:Suppress("DEPRECATION")
package com.example.chatmessager.notifications
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.app.PendingIntent
import android.text.Html
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput
import com.example.chatmessager.MainActivity
import com.example.chatmessager.R
import com.example.chatmessager.SharedPrefs
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import okhttp3.internal.notify
import okhttp3.internal.notifyAll
import kotlin.random.Random

private const val CHANNEL_ID="my_channel"
class FirebaseService:FirebaseMessagingService() {

    companion object{
        private const val  REPLY_ACTION_ID="REPLY_ACTION_ID"
        private const val KEY_REPLY_TEXT="KEY_REPLY_TEXT"

        var sharedPref: SharedPreferences ?=null

        var token:String?
        get() {
            return sharedPref?.getString("token","")
        }
        set(value) {
            sharedPref?.edit()?.putString("token",value)?.apply()
        }
    }

    override fun onNewToken(newToken: String) {
        super.onNewToken(newToken)
        token=newToken
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val intent= Intent(this,MainActivity::class.java)
        val notificationManger=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationID=Random.nextInt()

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            createNotificationChannel(notificationManger)
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_MUTABLE)

        //for replies on notification
        val remoteInput= RemoteInput.Builder(KEY_REPLY_TEXT)
            .setLabel("Reply")
            .build()
        //create a pendingintent for the reply action
        val replyIntent=Intent(this,NotificationReply::class.java)

        val replyPendingIntent=PendingIntent.getBroadcast(this,0,replyIntent,PendingIntent.FLAG_MUTABLE)

        //create a notificationcompat. action object for the reply action

        // Create a NotificationCompat.Action object for the reply action
        val replyAction = NotificationCompat.Action.Builder(
            R.drawable.reply,
            "Reply",
            replyPendingIntent
        ).addRemoteInput(remoteInput).build()

        val sharedCustomPref=SharedPrefs(applicationContext)
        sharedCustomPref.setIntValue("values",notificationID)

        val notification =NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentText(Html.fromHtml("<b>${message.data["title"]}</b>: ${message.data["message"]}"))
            .setSmallIcon(R.drawable.chat_app)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .addAction(replyAction)
            .build()

        notificationManger.notify(notificationID,notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManger: NotificationManager) {
        val channelName="channelName"
        val channel=NotificationChannel(CHANNEL_ID,channelName,NotificationManager.IMPORTANCE_HIGH).apply {
            description="My channel description"
            enableLights(true)
            lightColor= Color.GREEN
        }
        notificationManger.createNotificationChannel(channel)
    }

}