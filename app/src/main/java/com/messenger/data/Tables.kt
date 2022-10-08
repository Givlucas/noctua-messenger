package com.messenger.data

import androidx.room.*
import java.security.PublicKey

// Table to store information about various contacts
@Entity(tableName = "contacts_table")
data class Contacts (
    @PrimaryKey(autoGenerate = false)
    val user: String,
    val key: String,
    val address: String
    )

@Entity(tableName = "primary_user_table")
data class PrimaryUser(
    @PrimaryKey()
    val id: Int,
    val userName: String,
    val address: String
    )

@Entity(
    /*
    foreignKeys = [ForeignKey(
        entity = Contacts::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("user"),
        onDelete = ForeignKey.CASCADE
    )],*/
    tableName = "conversation_table"
)
data class Conversations(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    /*
    @Relation(
        parentColumn = "id",
        entityColumn = "user"
    )
     */
    val user: String,
    val convoName: String
)

@Entity(/*
    foreignKeys = [ForeignKey(
        entity = Contacts::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("user"),
        onDelete = ForeignKey.CASCADE
    ),
            ForeignKey(
                entity = Conversations::class,
                parentColumns = arrayOf("id"),
                childColumns = arrayOf("convos"),
                onDelete = ForeignKey.CASCADE
            )],
            */
    tableName = "msg_table"
)
data class Msgs(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    /*
    @Relation(
        parentColumn = "id",
        entityColumn = "user"
    )*/
    val user: String,

    /*
    @Relation(
        parentColumn = "id",
        entityColumn = "convo"
    )
     */
    val convo: String,
    val msg: String,
    val time_stamp: String
)