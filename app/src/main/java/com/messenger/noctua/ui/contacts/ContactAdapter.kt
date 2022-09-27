package com.messenger.noctua.ui.contacts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import androidx.recyclerview.widget.RecyclerView
import com.messenger.data.Contacts
import com.messenger.data.Conversations
import com.messenger.noctua.R
import kotlinx.android.synthetic.main.contacts_row.view.*

class ContactAdapter(private val clickListener: (Conversations) -> Unit): RecyclerView.Adapter<ContactAdapter.ViewHolder>() {

    private var contactList = emptyList<Conversations>()

    class ViewHolder(itemView: View, clickAtPosition: (Int) -> Unit): RecyclerView.ViewHolder(itemView) {
        init{
            itemView.setOnClickListener{
                clickAtPosition(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.contacts_row, parent, false)) {
            clickListener(contactList[it])
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = contactList[position]
        holder.itemView.contactNameet.text = currentItem.user
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    fun setData(convos: List<Conversations>){
        this.contactList = convos
        notifyDataSetChanged()
    }

}