package com.messenger.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DAO {
    // Adds a contact to the contacts table, takes the contact data class from
    // the Table definition as input and returns nothing.
    // If there is a conflict like a non-unique 
    // primary key the function will call a fail exception
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addContact(user: Contacts)
    
    // Adds a primary user to the primary user table.
    // This is where data like username and current hidden address are stored.
    // On conflict the insertion fails and thows a fail exception
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPrimaryUser(user: PrimaryUser)

    // Adds a contact to a coversation by 
    // making an entry in the conversations table.
    // This is how contacts are grouped into conversations.
    // Setup in a way to allow for group 
    // conversations in the future but currently this is 
    // missing feature.
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addToConversation(convo: Conversations)
    
    // Inserts a message into the message database.
    // Messages from all users are 
    // stored here even messages from the primary user.
    // They are associated to conversations by the convo ID.
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun addMsg(msg: Msgs)

    // Selects all msgs that match the conversation` ID
    // and returns them ordered by their timestamp.
    // This is used in the UI to 
    // show the message history of a coverstation.
    @Query("Select * FROM msg_table WHERE convo = :convo ORDER BY time_stamp ASC")
    fun getConvo(convo: String): LiveData<List<Msgs>>
    
    // Gets all contacts and returns them as a livedata object.
    // Live data objects are update when new data 
    // is observed and must be accessed using an observer
    @Query("Select * FROM contacts_table")
    fun getContacts(): LiveData<List<Contacts>>

    // gets a singular contact
    @Query("Select * FROM contacts_table WHERE user=:userName")
    fun getContact(userName: String): Contacts

    // Gets all contacts and returns them as a regular list
    // This is necessary for when contacts must be
    // checked for validity an observer object would be unnecessary.
    @Query("Select * FROM contacts_table")
    fun instantGetContacts(): List<Contacts>

    // Gets Information about primary user
    @Query("Select * FROM primary_user_table")
    fun primaryUserInfo(): LiveData<PrimaryUser>

    //gets non live
    @Query("Select * FROM primary_user_table")
    fun getPrimaryUserInfo(): PrimaryUser

    // Lists all Conversations is used in UI 
    // for listing all availble messages
    @Query("Select * FROM conversation_table")
    fun getConvos(): LiveData<List<Conversations>>

    // Checks to see if a username exists or not
    @Query("SELECT EXISTS(SELECT * FROM contacts_table where user=:user)")
    fun contactExists(user: String): Boolean

    // Check to see if a conversation exists
    @Query("SELECT EXISTS(SELECT * FROM conversation_table where convoName=:convoName)")
    fun convoExists(convoName: String): Boolean

    // Update primary user address
    @Query("UPDATE primary_user_table SET address=:addr WHERE id=0")
    fun updateAddress(addr: String)


}
