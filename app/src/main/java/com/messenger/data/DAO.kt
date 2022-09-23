package com.messenger.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DAO {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addContact(user: Contacts)
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addPrimaryUser(user: PrimaryUser)
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addToConversation(user: Contacts)
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addMsg(msg: Msgs)

    //Selects all msgs that match the conversation ID
    @Query("Select msg FROM msg_table WHERE convo = :convo ORDER BY time_stamp ASC")
    fun getConvo(convo: Int): LiveData<List<Msgs>>

    //Gets all contacts
    @Query("Select * FROM contacts_table")
    fun getContacts(): LiveData<List<Contacts>>

    //Gets Information about primary user
    @Query("Select * FROM primary_user_table")
    fun getPrimaryUserInfo(): LiveData<List<PrimaryUser>>

    //Lists all Conversations
    @Query("Select * FROM conversation_table")
    fun getConvos(): LiveData<List<Conversations>>

}