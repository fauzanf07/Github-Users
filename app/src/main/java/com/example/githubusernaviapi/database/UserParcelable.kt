package com.example.githubusernaviapi.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class UserParcelable(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "followers")
    var followers: Int =0,

    @ColumnInfo(name = "avatar_url")
    var avatar_url: String? = null,

    @ColumnInfo(name = "following")
    var following: Int = 0,

    @ColumnInfo(name = "name")
    var name: String? = null,

    @ColumnInfo(name = "bio")
    var bio: String?= null,

    @ColumnInfo(name = "location")
    var location: String?= null,

    @ColumnInfo(name = "login")
    var login: String?= null
) : Parcelable
