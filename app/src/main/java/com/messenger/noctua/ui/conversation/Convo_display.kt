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
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.messenger.data.AppViewModel
import com.messenger.data.Conversations
import com.messenger.data.Msgs
import com.messenger.msgServer.MsgServer
import com.messenger.noctua.MainActivity
import com.messenger.noctua.R
import kotlinx.android.synthetic.main.fragment_convo_display.*
import kotlinx.android.synthetic.main.fragment_convo_display.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Convo_display : Fragment() {

    private lateinit var appViewModel: AppViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_convo_display, container, false)
        val convoName = arguments?.getString("CONVERSATION_NAME")
        //database access
        appViewModel = ViewModelProvider(this).get(AppViewModel::class.java)

        //recyclerview init
        val adapter = ConvoAdapter()
        val recyclerView = view.msgs_recylcer_view
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        appViewModel.getMsgs(convoName!!)

        appViewModel.getMsgs(convoName).observe(viewLifecycleOwner, Observer { conversation ->
            adapter.setData(conversation)
            recyclerView.scrollToPosition(adapter.itemCount - 1)
        })

        view.send_button.setOnClickListener{
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    (activity as MainActivity?)!!.send(
                        view.write_msg_et.text.toString(),
                        convoName!!
                    )

                } catch (e: Throwable) {
                    Log.v("OOP", e.toString())
                }
                view.write_msg_et.text.clear()
            }
        }

        return view
    }




}