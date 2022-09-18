package com.messenger.msgServer

import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.websocket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MsgServer : Service() {
    private var notificationManager : NotificationManager? = null

    companion object {
        private const val PORT = 5001
    }

    private val server by lazy {
        embeddedServer(Netty, PORT, watchPaths = emptyList()) {
            install(WebSockets)
            install(CallLogging)
            routing {
                get("/") {
                    call.respondText(
                        text = "Success",
                        contentType = ContentType.Text.Plain
                    )
                }
                post("/txt"){
                    var receivedmsg = call.receiveText()
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


}