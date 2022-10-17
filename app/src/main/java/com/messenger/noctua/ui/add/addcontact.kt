package com.messenger.noctua.ui.add

import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.system.Os.bind
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
import com.messenger.data.Conversations
import com.messenger.noctua.R
import kotlinx.android.synthetic.main.fragment_addcontact.*
import java.security.MessageDigest
import kotlin.contracts.contract


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
        var key = keyet.text.toString()

        if(inputCheck(username, address, key) && checkAvailable(username)){
            //Create user
            key = hash(key) // Hash the key so it can be used in AES256
            val user = Contacts(username, key, address)
            val convo = Conversations(0, username, username)
            appViewModel.addConvo(convo)
            appViewModel.addContact(user)
            Toast.makeText(requireContext(), "Successfully added!", Toast.LENGTH_LONG).show()

            //Nav back
            findNavController().navigate(R.id.action_addcontact_to_contacts_nav)
        } else if(!checkAvailable(username)){
            Toast.makeText(requireContext(), "Username taken", Toast.LENGTH_SHORT).show()
        } else if(!inputCheck(username, address, key)){
            Toast.makeText(requireContext(), "Check all fields.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "ERROR", Toast.LENGTH_SHORT).show()
        }
    }

    // Cheks to ensure no field is left empty
    private fun inputCheck(username: String, address: String, key: String): Boolean {
        return !(TextUtils.isEmpty(username) || TextUtils.isEmpty(address) || TextUtils.isEmpty(key))
    }

    // Checks if username is taken
    private fun checkAvailable(username: String): Boolean{
        return !appViewModel.contactExists(username)
    }

    private fun hash(key: String): String {
        val bytes = key.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("", { str, it -> str + "%02x".format(it) })
    }
}