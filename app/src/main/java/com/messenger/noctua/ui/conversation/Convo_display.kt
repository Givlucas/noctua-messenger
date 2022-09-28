package com.messenger.noctua.ui.conversation

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.messenger.data.AppViewModel
import com.messenger.data.Conversations
import com.messenger.msgServer.MsgServer
import com.messenger.noctua.MainActivity
import com.messenger.noctua.R
import kotlinx.android.synthetic.main.fragment_convo_display.view.*

class Convo_display : Fragment() {

    private lateinit var appViewModel: AppViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_convo_display, container, false)
        val convo = arguments?.getInt("CONVERSATION")
        //database access
        appViewModel = ViewModelProvider(this).get(AppViewModel::class.java)

        //TEST stuff
        appViewModel.getMsgs(convo!!)
        view.testpass.text = appViewModel.getConvo.toString()

        //(activity as MainActivity?)!!.hello()



        return view
    }




}