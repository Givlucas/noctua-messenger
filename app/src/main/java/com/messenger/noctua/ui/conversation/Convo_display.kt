package com.messenger.noctua.ui.conversation

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.messenger.data.AppViewModel
import com.messenger.data.Conversations
import com.messenger.data.Msgs
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
        //HINT::::: (activity as MainActivity?)!!.hello()

        //recyclerview init
        val adapter = ConvoAdapter()
        val recyclerView = view.msgs_recylcer_view
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        appViewModel.getMsgs(convo!!)

        appViewModel.getMsgs(convo).observe(viewLifecycleOwner, Observer { conversation ->
            adapter.setData(conversation)
        })


        return view
    }




}