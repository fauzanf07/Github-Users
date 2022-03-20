package com.example.githubusernaviapi.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubusernaviapi.BuildConfig
import com.example.githubusernaviapi.api.ApiConfig
import com.example.githubusernaviapi.responses.FollowsResponseItem
import com.example.githubusernaviapi.responses.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowersViewModel :ViewModel() {
    private val _listUser = MutableLiveData<ArrayList<UserResponse>>()
    val listUser: LiveData<ArrayList<UserResponse>> = _listUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object{
        val TAG = "FollowersViewModel"
    }

    fun getFollowers(username: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowers(username, BuildConfig.GITHUB_TOKEN)
        client.enqueue(object : Callback<List<FollowsResponseItem>> {
            override fun onResponse(
                call: Call<List<FollowsResponseItem>>,
                response: Response<List<FollowsResponseItem>>
            ) {
                if(response.isSuccessful){
                    val responseBody = response.body()
                    if(responseBody!=null){
                        if(responseBody.size==0) {
                            _isLoading.value = false
                        }else{
                            setUserDataFollows(responseBody)
                        }
                    }else{
                        _isLoading.value = false
                        Log.e(TAG,"onFailure: ${response.message()}")
                    }
                }
            }

            override fun onFailure(call: Call<List<FollowsResponseItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun setUserDataFollows(item: List<FollowsResponseItem>){
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
                            _isLoading.value =false
                            Log.e(TAG, "onFailure: ${response.message()}")
                        }
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    _isLoading.value = false
                    Log.e(TAG, "onFailure: ${t.message}")
                }
            })
        }
    }
}