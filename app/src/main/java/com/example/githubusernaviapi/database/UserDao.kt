package com.example.githubusernaviapi.database

import androidx.room.*

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(user: UserParcelable)

    @Update
    fun update(user: UserParcelable)

    @Delete
    fun delete(user: UserParcelable)

    @Query("SELECT * FROM userparcelable ORDER BY id ASC")
    suspend fun getAllUsers(): List<UserParcelable>
}