package com.messenger.noctua.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.messenger.msgServer.MsgServer
import com.messenger.noctua.MainActivity
import com.messenger.noctua.R
import com.messenger.tor.SampleApp
import kotlinx.android.synthetic.main.fragment_settings.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class settings : Fragment() {
    private val app: SampleApp get() = requireActivity().applicationContext as SampleApp
    //private val mainActivity: MainActivity = (activity as MainActivity?)!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        // http buttons
        view.http_starten.setOnClickListener { httpStart()}
        view.http_stopen.setOnClickListener { httpStop() }
        view.http_restarten.setOnClickListener{
            httpStop()
            httpStart()
        }

        // tor buttons
        view.tor_starten.setOnClickListener { app.torOperationManager.startQuietly() }
        view.tor_stopen.setOnClickListener { app.torOperationManager.stopQuietly() }
        view.tor_restarten.setOnClickListener {
            lifecycleScope.launch(Dispatchers.Default) {
                app.torOperationManager.restartQuietly()
            }
        }

        // all buttons
        view.all_starten.setOnClickListener {
            httpStart()
            app.torOperationManager.startQuietly()
        }
        view.all_stopen.setOnClickListener {
            httpStop()
            app.torOperationManager.stopQuietly()
        }
        view.all_restarten.setOnClickListener {
            httpStop()
            httpStart()
            lifecycleScope.launch(Dispatchers.Default) {
                app.torOperationManager.restartQuietly()
            }
        }


        return view
    }

    // Http control functions

    private fun httpStart() {
        val intent = Intent(activity, MsgServer::class.java)
        activity?.bindService(intent,
            (activity as MainActivity?)!!.getConnectToBound(),
            AppCompatActivity.BIND_AUTO_CREATE)
        activity?.startForegroundService(intent)
    }
    private fun httpStop(){
        activity?.unbindService((activity as MainActivity?)!!.getConnectToBound())
        this.activity?.stopService(Intent(activity, MsgServer::class.java))
    }


}