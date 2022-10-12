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

    val getConvos: LiveData<List<Conversations>>
    val getContacts: LiveData<List<Contacts>>

    init {
        val appDao = AppDatabase.getDatabase(application).dao()
        repository = AppRepository(appDao)
        getConvos = repository.getConvos
        getContacts = repository.getContacts
    }
    fun addPrimaryUser(user: PrimaryUser){
        viewModelScope.launch(Dispatchers.IO){
            repository.addPrimaryUser(user)
        }
    }
    fun addContact(user: Contacts){
        viewModelScope.launch(Dispatchers.IO){
            repository.addContact(user)
        }
    }

    fun addConvo(convo: Conversations){
        viewModelScope.launch(Dispatchers.IO){
            repository.addToConversation(convo)
        }
    }

    fun updateAddress(address: String){
        viewModelScope.launch(Dispatchers.IO){
            repository.updateAddress(address)
        }
    }

    fun getMsgs(id: String): LiveData<List<Msgs>>{
        return repository.getConvo(id)
    }

    fun instanctGetContacts(): List<Contacts>{
        return repository.instantGetContacts()
    }

    fun contactExists(user: String ): Boolean{
        return repository.contactExists(user)
    }

    fun getPrimaryUser(): PrimaryUser{
        return repository.getPrimaryUser()
    }

}