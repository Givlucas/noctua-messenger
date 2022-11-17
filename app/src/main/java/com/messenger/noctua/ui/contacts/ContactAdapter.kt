package com.messenger.noctua.ui.contacts

import android.text.BoringLayout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.ExpandableListView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.messenger.data.*
import com.messenger.noctua.R
import kotlinx.android.synthetic.main.contacts_row.view.*
import java.text.FieldPosition

class ContactAdapter(private val clickListener: (Conversations) -> Unit,
                     private val longClickListener: (Conversations, View) -> Unit): RecyclerView.Adapter<ContactAdapter.ViewHolder>() {

    private var contactList = emptyList<Conversations>()

    class ViewHolder(itemView: View, clickAtPosition: (Int) -> Unit, longClickAtPosition: (Int, itemView: View) -> Unit): RecyclerView.ViewHolder(itemView) {

        init{
            itemView.setOnClickListener{
                clickAtPosition(adapterPosition)
            }
            itemView.setOnLongClickListener{
                longClickAtPosition(adapterPosition, it)
                true
            }
        }
    }

    private fun clickAt(it: Int){
        clickListener(contactList[it])
    }

    private fun longClickAt(it: Int, itemView: View){
        longClickListener(contactList[it], itemView)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.contacts_row, parent, false), ::clickAt, ::longClickAt)
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