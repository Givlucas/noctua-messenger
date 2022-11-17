package com.messenger.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class AppRepository(private val dao: DAO) {
    
    //Inits variables that can me found on start
    val getConvos: LiveData<List<Conversations>> = dao.getConvos()
    val getContacts: LiveData<List<Contacts>> = dao.getContacts()
    val primaryUser: LiveData<PrimaryUser> = dao.primaryUserInfo()

    // returns all the messages that have the same 
    // message ID orderd by time stamp
    fun getConvo(id: String): LiveData<List<Msgs>>{
        return dao.getConvo(id)
    }

    // Gets primary user information returns instantaneous
    fun getPrimaryUserInfo(): PrimaryUser{
        return dao.getPrimaryUserInfo()
    }
    
    // Gets all contacts as a list
    fun instantGetContacts(): List<Contacts>{
        return dao.instantGetContacts()
    }
    
    // Adds a contact to the contact table
    // Uses the suspend keyword to be used in a corutine
    suspend fun addContact(user: Contacts){
        dao.addContact(user)
    }
    
    // Adds a primary user to the primary user table
    suspend fun addPrimaryUser(user: PrimaryUser){
        dao.addPrimaryUser(user)
    }

    // Adds a contact to a conversation table
    suspend fun addToConversation(convo: Conversations){
        dao.addToConversation(convo)
    }

    // Adds a message to message table
    suspend fun addMsg(msg: Msgs){
        dao.addMsg(msg)
    }

    // Checks to see if a contact is present
    // in the contacts table
    fun contactExists(user: String): Boolean{
        return dao.contactExists(user)
    }

    // Checks to see if a convo exists in the convo table
    suspend fun convoExists(convo: String): Boolean{
        return dao.convoExists(convo)
    }

    // Gets a contact, return instatneous
    fun getContact(userName: String): Contacts{
        return dao.getContact(userName)
    }

    // Updates the address of the primary user
    suspend fun updateAddress(addr: String){
        dao.updateAddress(addr)
    }

    suspend fun deleteChat(convoName: String){
        dao.deleteChat(convoName)
        dao.deleteContact(convoName)
        dao.deleteMsgs(convoName)
    }


}
