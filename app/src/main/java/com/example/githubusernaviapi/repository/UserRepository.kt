package com.example.githubusernaviapi.repository

import android.app.Application
import com.example.githubusernaviapi.database.UserDao
import com.example.githubusernaviapi.database.UserParcelable
import com.example.githubusernaviapi.database.UserRoomDatabase
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class UserRepository(application: Application) {
    private val mUserDao: UserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = UserRoomDatabase.getDatabase(application)
        mUserDao = db.userDao()
    }

    fun getAllUsers(): List<UserParcelable> = runBlocking {
        val allUsers = async { mUserDao.getAllUsers() }
        val result = allUsers.await()
        return@runBlocking result
    }

    fun insert(user: UserParcelable){
        executorService.execute { mUserDao.insert(user) }
    }

    fun delete(user: UserParcelable){
        executorService.execute { mUserDao.delete(user) }
    }

    fun update(user: UserParcelable){
        executorService.execute { mUserDao.update(user) }
    }
}