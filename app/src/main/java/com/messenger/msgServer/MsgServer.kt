package com.messenger.msgServer

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Base64
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.messenger.data.*
import com.messenger.noctua.MainActivity
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.ContentType.Application.Json
import io.ktor.serialization.kotlinx.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import java.time.LocalDateTime
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import kotlinx.serialization.*

class MsgServer : Service() {
    private lateinit var lastmsg: JsonMsg

    companion object {
        private const val PORT = 5001
    }

    //Database access
    private val appDao = AppDatabase.getDatabase(this).dao()
    private val repository = AppRepository(appDao)


    val client = HttpClient(Android){
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
    }

    private val server by lazy {

        embeddedServer(Netty, PORT, watchPaths = emptyList()) {
            routing {
                get("/") {
                    call.respondText(
                        text = "Success",
                        contentType = ContentType.Text.Plain
                    )
                }
                post("/txt"){
                    lastmsg = call.receive()
                    CoroutineScope(Dispatchers.IO).launch {
                        val contacts = repository.instantGetContacts()
                        for(contact in contacts){
                            val convo = decrypt(lastmsg.convoEncrypt, contact.key).toString()
                            if(repository.contactExists(convo)){
                                val msg = decrypt(lastmsg.msgEncrypt, contact.key).toString()
                                repository.addMsg(Msgs(0,convo, convo, msg, LocalDateTime.now().toString()))
                            }
                        }
                    }

                    call.response.status(HttpStatusCode.OK)
                }
            }
        }
    }



    override fun onCreate() {
        CoroutineScope(Dispatchers.IO).launch {
            server.start(wait = true)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForegroundService()
        return START_STICKY
    }

    override fun onDestroy() {
        server.stop(1_000, 2_000)
        super.onDestroy()
    }


    override fun onBind(p0: Intent?): IBinder {
        return LocalBinder()
    }

    inner class LocalBinder : Binder() {
        fun getService() : MsgServer {
            return this@MsgServer
        }
    }

    private fun startForegroundService(){
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
                as NotificationManager


        createNotificationChannel(notificationManager)


        val notificationBuilder = NotificationCompat.Builder(this, "msg channel")
            .setAutoCancel(false)
            .setOngoing(true)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("MSG_SERVER")
            .setContentText("html server is running")
        startForeground(1, notificationBuilder.build())
    }


    private fun getMainActivityPendingIntent() = PendingIntent.getActivity(
        this,
        0,
        Intent(this, MainActivity::class.java),
        FLAG_UPDATE_CURRENT

    )

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager){
        val channel = NotificationChannel(
            "msg channel",
            "Messages",
            NotificationManager.IMPORTANCE_MIN
            )
        notificationManager.createNotificationChannel(channel)
    }

    suspend fun send(msg: String, convoName: String){
        val contact: Contacts = repository.getContact(convoName)
        val primaryUser: PrimaryUser = repository.getPrimaryUser()
        val convoEncrypt = encrypt(primaryUser.userName, contact.key)
        val msgEncrypt = encrypt(msg, contact.key)
        repository.addMsg(Msgs(0,"internal", convoName, msg,LocalDateTime.now().toString()))
        val Jmsg = JsonMsg(convoEncrypt.toString(), msgEncrypt.toString())
        val response = client.post {
            url(contact.address)
            contentType(ContentType.Application.Json)
            header(HttpHeaders.ContentType, Json)
            setBody(Jmsg)
        }

    }

    private fun encrypt(plain: String, keyString: String): ByteArray{
        val bytekey = keyString.decodeHex()
        val certKey = SecretKeySpec(bytekey, 0, bytekey.size, "AES" )
        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
        cipher.init(Cipher.ENCRYPT_MODE, certKey)

        return cipher.doFinal(plain.toByteArray())
    }

    private fun decrypt(coded: String, keyString: String): ByteArray{
        val bytekey = keyString.decodeHex()
        val certKey = SecretKeySpec(bytekey, 0, bytekey.size, "AES" )
        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
        cipher.init(Cipher.DECRYPT_MODE, certKey)
        return cipher.doFinal(coded.toByteArray())
    }

    private fun String.decodeHex(): ByteArray {
        check(length % 2 == 0) { "Must have an even length" }

        return chunked(2)
            .map { it.toInt(16).toByte() }
            .toByteArray()
    }

}

@Serializable
data class JsonMsg(
    val convoEncrypt: String,
    val msgEncrypt: String
)