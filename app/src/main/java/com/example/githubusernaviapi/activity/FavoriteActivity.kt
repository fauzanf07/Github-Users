package com.example.githubusernaviapi.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubusernaviapi.adapter.FavoriteAdapter
import com.example.githubusernaviapi.database.UserParcelable
import com.example.githubusernaviapi.databinding.ActivityFavoriteBinding
import com.example.githubusernaviapi.viewmodel.factory.FavoriteModelFactory
import com.example.githubusernaviapi.viewmodel.FavoriteViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

class FavoriteActivity : AppCompatActivity() {
    private var _activityFavoriteBinding: ActivityFavoriteBinding?= null
    private val binding get() = _activityFavoriteBinding
    private lateinit var favoriteViewModel: FavoriteViewModel

    private lateinit var adapter: FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        _activityFavoriteBinding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding?.root)


        favoriteViewModel = obtainViewModel(this)
        val userList = getUserlist()

        adapter = FavoriteAdapter()
        adapter.setOnItemClickCallback(object : FavoriteAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserParcelable) {
                showSelectedUser(data)
            }
        })

        adapter.setListUsers(userList)

        binding?.rvUserFavorite?.layoutManager = LinearLayoutManager(this)
        binding?.rvUserFavorite?.setHasFixedSize(true)
        binding?.rvUserFavorite?.adapter = adapter
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onResume() {
        super.onResume()
        val userList = getUserlist()
        adapter.setListUsers(userList)
        adapter.notifyDataSetChanged()
    }

    private fun getUserlist() : List<UserParcelable> = runBlocking {
        val allUser = async { favoriteViewModel.getAllUsers() }
        val result = allUser.await()
        return@runBlocking result
    }

    private fun showSelectedUser(data: UserParcelable){
        val intent = Intent(this@FavoriteActivity,DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_DATA,data)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        _activityFavoriteBinding = null
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteViewModel{
        val factory = FavoriteModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity,factory).get(FavoriteViewModel::class.java)
    }
}