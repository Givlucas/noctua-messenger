package com.messenger.noctua.ui.contacts

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.messenger.data.AppViewModel
import com.messenger.data.Conversations
import com.messenger.noctua.R
import kotlinx.android.synthetic.main.fragment_contacts_nav.view.*

class ContactsNav() : Fragment() {

    private lateinit var appViewModel: AppViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_contacts_nav, container, false)

        //RecyclerView
        val adapter = ContactAdapter(::onClick, ::onLongClick)

        val recyclerView = view.contactRecyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        //Init view model
        appViewModel = ViewModelProvider(this).get(AppViewModel::class.java)
        appViewModel.getConvos.observe(viewLifecycleOwner, Observer { conversations ->
            adapter.setData(conversations)
        })

        view.findViewById<FloatingActionButton>(R.id.addContactfbt).setOnClickListener{
            findNavController().navigate(R.id.action_contacts_nav_to_addcontact)
        }

        view.findViewById<FloatingActionButton>(R.id.addConvofbt).setOnClickListener {
            findNavController().navigate(R.id.action_contacts_nav_to_primaryUserInfo)
        }


        return view
    }

    private fun onClick(conversations: Conversations) {
        findNavController().navigate(R.id.action_contacts_nav_to_convo_display,
            bundleOf(Pair("CONVERSATION_NAME", conversations.convoName)))
    }

    private fun onLongClick(conversations: Conversations, itemView: View) {
        val pop = PopupMenu(itemView.context, itemView)
        pop.inflate(R.menu.context_menu)

        pop.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.menuDelete -> {
                    appViewModel.deleteChat(conversations.convoName)
                }
            }
            true
        }
        pop.show()
        true
    }



}