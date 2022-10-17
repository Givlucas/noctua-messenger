package com.messenger.msgServer

import android.app.Application
import java.util.Base64
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
import android.util.Log
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
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.http.ContentType.Application.Json
import io.ktor.serialization.kotlinx.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.util.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import java.time.LocalDateTime
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import kotlinx.serialization.*
import javax.crypto.spec.IvParameterSpec

class MsgServer : Service() {
    private lateinit var lastmsg: JsonMsg

    // Sets the port that the http server listens on
    companion object {
        private const val PORT = 5001
    }

    // Database access
    private val appDao = AppDatabase.getDatabase(this).dao()
    private val repository = AppRepository(appDao)

    // Initates the http cleint object
    // configuration is done here
    val client = HttpClient(Android){
        install(io.ktor.client.plugins.contentnegotiation.ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
    }

    // The Http server has two routes
    // the root route only text being "Success"
    // the /txt route is used for receiving messages
    private val server by lazy {

        embeddedServer(Netty, PORT, watchPaths = emptyList()) {
            install(ContentNegotiation){
                json(Json {
                    prettyPrint = true
                    isLenient = true
                })
            }
            routing {
                get("/") {
                    Log.v("GET", "DATA SENT")
                    call.respondText(
                        text = "Success",
                        contentType = ContentType.Text.Plain
                    )
                }
                post("/txt"){
                    lastmsg = call.receive<JsonMsg>() // the message most recently recieved
                    CoroutineScope(Dispatchers.IO).launch {
                        Log.v("TXT", "RECIEVED MESSAGE")
                        val contacts = repository.instantGetContacts() // gets an frozen instant of the contacts
                        // Goes through each contact and attempts to see if its key
                        // can decrypt the conversation name and checks
                        // to see if that contact exits. If it can and the convo exists
                        // then it decrpyts the message and exits.
                        for(contact in contacts){
                            val convo = decrypt(lastmsg.convoEncrypt, contact.key).toString()
                            Log.v("DE", convo + " " + lastmsg.convoEncrypt)
                            if(repository.contactExists(convo)){
                                val msg = decrypt(lastmsg.msgEncrypt, contact.key).toString()
                                repository.addMsg(Msgs(0,convo, convo, msg, LocalDateTime.now().toString()))
                            }
                        }
                    }
                    // return a OK status code to let the other side know the message was heard
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

    // Starting our serivce as a foreground service
    // allows for work to constantly be done in the background
    // this is important as we must always be listening for messages
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
        val primaryUser: PrimaryUser = repository.getPrimaryUserInfo()
        val convoEncrypt = encrypt(primaryUser.userName, contact.key)
        val msgEncrypt = encrypt(msg, contact.key)
        repository.addMsg(Msgs(0,"internal", convoName, msg,LocalDateTime.now().toString()))
        val Jmsg = JsonMsg(convoEncrypt.fold("", { str, it -> str + "%02x".format(it) }),
            msgEncrypt.fold("", { str, it -> str + "%02x".format(it) }))
        val response = client.post {
            url(contact.address)
            contentType(ContentType.Application.Json)
            header(HttpHeaders.ContentType, Json)
            setBody(Jmsg)
        }

        Log.v("RES", response.toString())

    }

    private fun encrypt(plain: String, keyString: String): ByteArray{
        val iv: IvParameterSpec = IvParameterSpec("c4eb12ceccf2c3058d185f9356557e96".decodeHex())
        val bytekey = keyString.decodeHex()
        val certKey = SecretKeySpec(bytekey, 0, bytekey.size, "AES" )
        val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
        cipher.init(Cipher.ENCRYPT_MODE, certKey, iv)
        return cipher.doFinal(Base64.getEncoder().encode(plain.toByteArray()))
    }

    private fun decrypt(coded: String, keyString: String): ByteArray{
        val iv: IvParameterSpec = IvParameterSpec("c4eb12ceccf2c3058d185f9356557e96".decodeHex())
        val bytekey = keyString.decodeHex()
        val certKey = SecretKeySpec(bytekey, 0, bytekey.size, "AES" )
        val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
        cipher.init(Cipher.DECRYPT_MODE, certKey, iv)
        return cipher.doFinal(Base64.getDecoder().decode(coded.decodeHex()))
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