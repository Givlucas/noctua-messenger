package com.messenger.data

import androidx.lifecycle.LiveData

class AppRepository(private val dao: DAO) {

    val getConvos: LiveData<List<Conversations>> = dao.getConvos()
    val getContacts: LiveData<List<Contacts>> = dao.getContacts()

    suspend fun getConvo(id: Int){
        dao.getConvo(id)
    }

    suspend fun addContact(user: Contacts){
        dao.addContact(user)
    }

    suspend fun addPrimaryUser(user: PrimaryUser){
        dao.addPrimaryUser(user)
    }

    suspend fun addToConversation(convo: Conversations){
        dao.addToConversation(convo)
    }

    suspend fun addMsg(msg: Msgs){
        dao.addMsg(msg)
    }


}