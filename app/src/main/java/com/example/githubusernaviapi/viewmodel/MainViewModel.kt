package com.example.githubusernaviapi.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.githubusernaviapi.BuildConfig
import com.example.githubusernaviapi.api.ApiConfig
import com.example.githubusernaviapi.responses.ItemsItem
import com.example.githubusernaviapi.responses.SearchResponse
import com.example.githubusernaviapi.responses.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val _listUser = MutableLiveData<ArrayList<UserResponse>>()
    val listUser: LiveData<ArrayList<UserResponse>> = _listUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isFail = MutableLiveData<Boolean>()
    val isFail: LiveData<Boolean> = _isFail

    companion object{
        val TAG = "MainViewModel"
    }

    fun findUser(search: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getSearch(search, BuildConfig.GITHUB_TOKEN)
        client.enqueue(object : Callback<SearchResponse> {
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                if(response.isSuccessful){
                    val responseBody = response.body()
                    if(responseBody!=null){
                        if(responseBody.items.size==0){
                            _isFail.value = true
                            _isLoading.value =false
                        }else {
                            setUserData(responseBody.items)
                        }
                    }else{
                        _isFail.value = true
                        _isLoading.value =false
                        Log.e(TAG,"onFailure: ${response.message()}")
                    }
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                _isLoading.value = false
                _isFail.value = true
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun setUserData(item: List<ItemsItem>){
        val userList = ArrayList<UserResponse>()
        for(i in 0..item.size-1) {
            val client = ApiConfig.getApiService()
                .getUser(item.get(i).login, BuildConfig.GITHUB_TOKEN)
            client.enqueue(object : Callback<UserResponse> {
                override fun onResponse(
                    call: Call<UserResponse>,
                    response: Response<UserResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            userList.add(responseBody)
                            if(item.get(i).login.equals(item.get(item.size-1).login)) {
                                _listUser.value = userList
                                _isLoading.value = false
                            }
                        } else {
                            _isFail.value = true
                            _isLoading.value =false
                            Log.e(TAG, "onFailure: ${response.message()}")
                        }
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    _isLoading.value = false
                    _isFail.value = true
                    Log.e(TAG, "onFailure: ${t.message}")
                }
            })
        }
    }

}