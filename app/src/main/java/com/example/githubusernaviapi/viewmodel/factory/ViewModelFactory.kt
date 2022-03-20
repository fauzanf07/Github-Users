package com.example.githubusernaviapi.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubusernaviapi.preferences.SettingPreferences
import com.example.githubusernaviapi.viewmodel.SettingViewModel
import java.lang.IllegalArgumentException

class ViewModelFactory(private val pref: SettingPreferences): ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SettingViewModel::class.java)){
            return SettingViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class"+modelClass.name)
    }
}