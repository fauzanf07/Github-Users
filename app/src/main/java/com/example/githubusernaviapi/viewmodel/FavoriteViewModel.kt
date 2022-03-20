package com.example.githubusernaviapi.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.githubusernaviapi.database.UserParcelable
import com.example.githubusernaviapi.repository.UserRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

class FavoriteViewModel(application: Application): ViewModel() {
    private val mUserRepository: UserRepository= UserRepository(application)

    fun getAllUsers() : List<UserParcelable> = runBlocking {
        val allUsers = async {  mUserRepository.getAllUsers() }
        val result = allUsers.await()
        return@runBlocking result
    }
}