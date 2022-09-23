package com.messenger.data

import androidx.room.*
import java.security.PublicKey

// Table to store information about various contacts
@Entity(tableName = "contacts_table")
data class Contacts (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val user: String,
    val key: String,
    val address: String
    )

@Entity(tableName = "primary_user_table")
data class PrimaryUser(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val address: String,
    val privateKey: String,
    val publicKey: String
    )

@Entity(
    foreignKeys = [ForeignKey(
        entity = Contacts::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("user"),
        onDelete = ForeignKey.CASCADE
    )],
    tableName = "conversation_table"
)
data class Conversations(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @Relation(
        parentColumn = "id",
        entityColumn = "user"
    )
    val user: Contacts
)

@Entity(
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
    tableName = "msg_table"
)
data class Msgs(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @Relation(
        parentColumn = "id",
        entityColumn = "user"
    )
    val user: Contacts,
    @Relation(
        parentColumn = "id",
        entityColumn = "convo"
    )
    val convo: Conversations,
    val msg: String,
    val time_stamp: String
)