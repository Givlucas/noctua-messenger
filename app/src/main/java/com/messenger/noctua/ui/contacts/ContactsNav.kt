package com.messenger.noctua.ui.contacts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.messenger.noctua.R

class ContactsNav : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_contacts_nav, container, false)

        view.findViewById<FloatingActionButton>(R.id.addContactfbt).setOnClickListener{
            findNavController().navigate(R.id.action_contacts_nav_to_addcontact)
        }

        view.findViewById<FloatingActionButton>(R.id.addConvofbt).setOnClickListener {
            findNavController().navigate(R.id.action_contacts_nav_to_addConvo)
        }


        return view
    }


}