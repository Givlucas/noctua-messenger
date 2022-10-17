package com.messenger.noctua.ui.conversation

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.messenger.data.Conversations
import com.messenger.data.Msgs
import com.messenger.noctua.R
import com.messenger.noctua.ui.contacts.ContactAdapter
import kotlinx.android.synthetic.main.msg_row_external.view.*
import kotlinx.android.synthetic.main.msg_row_internal.view.*

class ConvoAdapter: RecyclerView.Adapter<ConvoAdapter.ViewHolder>() {
    private var convoList = emptyList<Msgs>()
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        init {

        }
    }


    override fun getItemViewType(position: Int): Int {
        // Splits the view into 2 types
        // type 0 is messages that are sent from the host device
        // type 1 are messages that are recieved from contacts
        return if(convoList[position].user == "internal"){
            0
        } else {
            1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return if(viewType == 0){
            ConvoAdapter.ViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.msg_row_internal, parent, false)
            )
        } else {
            ConvoAdapter.ViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.msg_row_external, parent, false)
            )
        }



    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = convoList[position]

        if(holder.itemViewType == 0){
            holder.itemView.internal_text_view.text = currentItem.msg
        } else {
            holder.itemView.external_text_view.text = currentItem.msg
        }

    }

    override fun getItemCount(): Int {
        return convoList.size
    }

    fun setData(convo: List<Msgs>){
        this.convoList = convo
        notifyDataSetChanged()
    }

}