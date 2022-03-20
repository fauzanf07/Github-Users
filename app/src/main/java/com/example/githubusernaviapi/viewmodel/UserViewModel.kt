package com.example.githubusernaviapi.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubusernaviapi.database.UserParcelable
import com.example.githubusernaviapi.repository.UserRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

class UserViewModel(application: Application) : ViewModel() {
    private val mUserRepository: UserRepository = UserRepository(application)

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> = _isFavorite

    fun insert(user: UserParcelable){
        mUserRepository.insert(user)
    }

    fun delete(user: UserParcelable){
        mUserRepository.delete(user)
    }

    fun getAllUsers() : List<UserParcelable> = runBlocking {
        val allUsers = async {  mUserRepository.getAllUsers() }
        val result = allUsers.await()
        return@runBlocking result
    }

    fun isFavorite(user: UserParcelable?, click:Boolean=false): Boolean{
        val allUsers = getAllUsers()
        var isFav = false
        if(allUsers.size>0) {
            for (users in allUsers) {
                if(users.login.equals(user?.login)) {
                    isFav=true
                    break
                }
            }
        }
        if(click) _isFavorite.value = isFav
        return isFav
    }

    fun getId(user:UserParcelable?):Int{
        val allUsers = getAllUsers()
        var id =0
        for (users in allUsers) {
            if(users.login.equals(user?.login)) {
                id = users.id
                break
            }
        }
        return id
    }
}