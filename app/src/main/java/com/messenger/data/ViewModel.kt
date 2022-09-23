package com.messenger.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.nio.file.attribute.UserPrincipal

class ViewModel(application: Application): AndroidViewModel(application) {
    private val convos: LiveData<List<Conversations>>
    private val contacts: LiveData<List<Contacts>>
    private val repository: AppRepository

    init {
        val appDao = AppDatabase.getDatabase(application).dao()
        repository = AppRepository(appDao)
    }

    fun addContact(user: Contacts){
        viewModelScope.launch(Dispatchers.IO){
            repository.addContact(user)
        }
    }
}