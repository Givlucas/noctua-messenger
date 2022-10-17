package com.messenger.data

import android.app.Application
import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.nio.file.attribute.UserPrincipal

class AppViewModel(application: Application): AndroidViewModel(application) {
    private val repository: AppRepository

    // live objects that can be initialized on start
    val getConvos: LiveData<List<Conversations>>
    val getContacts: LiveData<List<Contacts>>
    val primaryUser: LiveData<PrimaryUser>

    init {
        val appDao = AppDatabase.getDatabase(application).dao()
        repository = AppRepository(appDao)
        getConvos = repository.getConvos
        getContacts = repository.getContacts
        primaryUser = repository.primaryUser
    }

    // Adds primary user
    fun addPrimaryUser(user: PrimaryUser){
        viewModelScope.launch(Dispatchers.IO){
            repository.addPrimaryUser(user)
        }
    }
    // Adds a contact
    fun addContact(user: Contacts){
        viewModelScope.launch(Dispatchers.IO){
            repository.addContact(user)
        }
    }

    //Adds a coversation
    fun addConvo(convo: Conversations){
        viewModelScope.launch(Dispatchers.IO){
            repository.addToConversation(convo)
        }
    }

    // Updates the primary users address
    fun updateAddress(address: String){
        viewModelScope.launch(Dispatchers.IO){
            repository.updateAddress(address)
        }
    }

    // returns the messages of all users involved in coversation
    fun getMsgs(id: String): LiveData<List<Msgs>>{
        return repository.getConvo(id)
    }

    // Gets contacts list
    fun GetContacts(): List<Contacts>{
        return repository.instantGetContacts()
    }

    // Checks to see if a contact exists
    fun contactExists(user: String ): Boolean{
        return repository.contactExists(user)
    }


}