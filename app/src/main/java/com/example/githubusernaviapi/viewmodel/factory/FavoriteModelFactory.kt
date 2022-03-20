package com.example.githubusernaviapi.viewmodel.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubusernaviapi.viewmodel.FavoriteViewModel
import com.example.githubusernaviapi.viewmodel.UserViewModel
import java.lang.IllegalArgumentException

class FavoriteModelFactory private constructor(private val mApplication: Application) : ViewModelProvider.NewInstanceFactory(){
    companion object{
        @Volatile
        private var INSTANCE: FavoriteModelFactory?= null

        @JvmStatic
        fun getInstance(application: Application): FavoriteModelFactory {
            if(INSTANCE == null){
                synchronized(FavoriteModelFactory::class.java){
                    INSTANCE = FavoriteModelFactory(application)
                }
            }
            return INSTANCE as FavoriteModelFactory
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
       if(modelClass.isAssignableFrom(FavoriteViewModel::class.java)){
           return FavoriteViewModel(mApplication) as T
       }else if(modelClass.isAssignableFrom(UserViewModel::class.java)){
           return UserViewModel(mApplication) as T
       }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}