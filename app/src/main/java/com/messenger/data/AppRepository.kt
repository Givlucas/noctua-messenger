package com.messenger.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class AppRepository(private val dao: DAO) {

    val getConvos: LiveData<List<Conversations>> = dao.getConvos()
    val getContacts: LiveData<List<Contacts>> = dao.getContacts()
    val getConvo: LiveData<List<Msgs>> = MutableLiveData<List<Msgs>>()

    suspend fun getConvo(id: Int): LiveData<List<Msgs>>{
        return dao.getConvo(id)
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