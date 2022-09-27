package com.messenger.noctua.ui.conversation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.messenger.data.Conversations
import com.messenger.noctua.R
import kotlinx.android.synthetic.main.fragment_convo_display.view.*


class Convo_display : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_convo_display, container, false)
        val convo = arguments?.getInt("CONVERSATION")
        view.testpass.text = convo.toString()

        return view
    }


}