package com.messenger.data

import androidx.lifecycle.LiveData

class AppRepository(private val dao: DAO) {

    val convos: LiveData<List<Conversations>> = dao.getConvos()
    val contacts: LiveData<List<Contacts>> = dao.getContacts()

    suspend fun getConvo(convo: Conversations){
        dao.getConvo(convo.id)
    }

    suspend fun addContact(user: Contacts){
        dao.addContact(user)
    }

    suspend fun addPrimaryUser(user: PrimaryUser){
        dao.addPrimaryUser(user)
    }

    suspend fun addToConversation(user: Contacts){
        dao.addToConversation(user)
    }

    suspend fun addMsg(msg: Msgs){
        dao.addMsg(msg)
    }
}