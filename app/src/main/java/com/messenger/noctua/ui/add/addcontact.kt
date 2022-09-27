package com.messenger.noctua.ui.add

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.messenger.data.AppViewModel
import com.messenger.data.Contacts
import com.messenger.noctua.R
import kotlinx.android.synthetic.main.fragment_addcontact.*

class addcontact : Fragment() {

    private lateinit var appViewModel: AppViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_addcontact, container, false)

        //Database access
        appViewModel = ViewModelProvider(this).get(AppViewModel::class.java)

        view.findViewById<Button>(R.id.addbt).setOnClickListener{
            insertDataToDataBase()
        }

        return view
    }

    private fun insertDataToDataBase() {
        val username = nameet.text.toString()
        val address = addresset.text.toString()

        if(inputCheck(username, address)){
            //Create user
            val user = Contacts(0, username, "TEST", address)
            appViewModel.addContact(user)
            Toast.makeText(requireContext(), "Successfully added!", Toast.LENGTH_LONG)
            //Nav back
            findNavController().navigate(R.id.action_addcontact_to_contacts_nav)
        } else {
            Toast.makeText(requireContext(), "Check all fields.", Toast.LENGTH_LONG)
        }
    }

    private fun inputCheck(username: String, address: String): Boolean {
        return !(TextUtils.isEmpty(username) && TextUtils.isEmpty(address))
    }

}