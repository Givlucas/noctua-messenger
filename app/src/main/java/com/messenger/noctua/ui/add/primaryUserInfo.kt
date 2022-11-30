package com.messenger.noctua.ui.add

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.messenger.data.AppViewModel
import com.messenger.data.PrimaryUser
import com.messenger.noctua.R
import kotlinx.android.synthetic.main.fragment_primary_user_info.*
import kotlinx.android.synthetic.main.fragment_primary_user_info.view.*


class primaryUserInfo : Fragment() {
    private lateinit var appViewModel: AppViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        appViewModel = ViewModelProvider(this).get(AppViewModel::class.java)
        val primary = appViewModel.primaryUser
        val view = inflater.inflate(R.layout.fragment_primary_user_info, container, false)

        primary.observe(viewLifecycleOwner, Observer { primary ->
            if(primary == null){
                view.userNametv.text = "No user"
                view.addresstv.text = "No user"
            } else {
                view.userNametv.text = primary.userName
                view.addresstv.text = primary.address
            }
        })


        view.changeen.setOnClickListener{
            val newName = newNameet.text.toString()
            if(inputCheck(newName)) {
                val primaryUser = PrimaryUser(0 ,newName, "")
                appViewModel.addPrimaryUser(primaryUser)
                Toast.makeText(requireContext(), "Success!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Check all fields", Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }

    private fun inputCheck(name: String): Boolean{
        return (!TextUtils.isEmpty(name))
    }
}