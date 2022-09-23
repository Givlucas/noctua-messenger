package com.messenger.noctua

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.widget.Button
import android.widget.TextView
import com.messenger.msgServer.*



class MainActivity : AppCompatActivity() {
    private var msgservice : MsgServer? = null
    private var isBound = false

    //TEST BUTTONS!!!!
    private lateinit var stopservice : Button
    private lateinit var msgdisplay : TextView
    private lateinit var update: Button


     private val ConnectToBound = object : ServiceConnection {
         override fun onServiceConnected(p0: ComponentName?, service: IBinder?) {
             val binder = service as MsgServer.LocalBinder
             msgservice = binder.getService()
             isBound = true
         }

         override fun onServiceDisconnected(p0: ComponentName?) {
             isBound = false
         }
     }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Starts and binds a service
        val intent = Intent(this, MsgServer::class.java)
        bindService(intent, ConnectToBound, BIND_AUTO_CREATE)
        startForegroundService(intent)

        //TEST BUTTONS
        stopservice = findViewById(R.id.buttonstop)
        update = findViewById(R.id.updateButton)
        msgdisplay = findViewById(R.id.msgDisplay1)

        stopservice.setOnClickListener {
            //stops the unbound service
            unbindService(ConnectToBound)
            Intent(this, MsgServer::class.java).also { intent ->
                stopService(intent)
            }
        }

        update.setOnClickListener {
            msgdisplay.text = msgservice?.getLastMsg()
        }
    }
}