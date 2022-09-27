package com.messenger.noctua.ui.add

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ListAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.messenger.data.AppViewModel
import com.messenger.data.Contacts
import com.messenger.data.Conversations
import com.messenger.noctua.R
import kotlinx.android.synthetic.main.fragment_add_convo.*
import kotlinx.android.synthetic.main.fragment_addcontact.*

class addConvo : Fragment() {
    private lateinit var appViewModel: AppViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_convo, container, false)

        //Database access
        appViewModel = ViewModelProvider(this).get(AppViewModel::class.java)

        //adapters for live data

        view.findViewById<Button>(R.id.addConvoen).setOnClickListener {
            insertDataToDataBase()
        }

        return view
    }

    private fun insertDataToDataBase() {
        val username = addUserConvoet.text.toString()
        if(inputCheck(username)){
            val convo = Conversations(0, username, username)
            appViewModel.addConvo(convo)
            Toast.makeText(requireContext(), "Successfully added!", Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_addConvo_to_contacts_nav)
        } else {
            Toast.makeText(requireContext(), "Check all fields.", Toast.LENGTH_LONG).show()
        }
    }

    private fun inputCheck(username: String): Boolean{
        var flag = false
        if(TextUtils.isEmpty(username)){
            return flag
        }
        appViewModel.getContacts.observe(viewLifecycleOwner, Observer {
            for(contact in it){
                if(contact.user == username){
                    flag = true
                }
            }
        })
        return flag
    }
}