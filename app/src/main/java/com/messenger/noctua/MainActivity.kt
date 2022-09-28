package com.messenger.noctua

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.view.ActionMode
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.messenger.data.AppViewModel
import com.messenger.data.Contacts
import com.messenger.msgServer.*



class MainActivity : AppCompatActivity() {
    private var msgservice : MsgServer? = null
    private var isBound = false

    private lateinit var appViewModel: AppViewModel

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

        //Dynamic action bar
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView)
                as NavHostFragment
        val navController: NavController = navHostFragment.navController
        setupActionBarWithNavController(navController)

    /*
        stopservice.setOnClickListener {
            //stops the unbound service
            unbindService(ConnectToBound)
            Intent(this, MsgServer::class.java).also { intent ->
                stopService(intent)
            }
        }
        */

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragmentContainerView)
        return super.onSupportNavigateUp() || navController.navigateUp()
    }

    internal fun hello(){

    }
}