package com.example.githubusernaviapi.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubusernaviapi.R
import com.example.githubusernaviapi.adapter.ListUserAdapter
import com.example.githubusernaviapi.responses.UserResponse
import com.example.githubusernaviapi.database.UserParcelable
import com.example.githubusernaviapi.viewmodel.MainViewModel
import com.example.githubusernaviapi.databinding.ActivityMainBinding
import com.example.githubusernaviapi.preferences.SettingPreferences
import com.example.githubusernaviapi.viewmodel.SettingViewModel
import com.example.githubusernaviapi.viewmodel.factory.ViewModelFactory
import java.util.*
import kotlin.collections.ArrayList

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
class MainActivity : AppCompatActivity() {
    private  lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var settingViewModel: SettingViewModel
    private var dark_mode = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val layoutManager = LinearLayoutManager(this)
        binding.rvUser.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUser.addItemDecoration(itemDecoration)


        setMyButtonEnable()

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        val pref = SettingPreferences.getInstance(dataStore)
        settingViewModel = ViewModelProvider(this,
            ViewModelFactory(pref)
        ).get(SettingViewModel::class.java)

        viewModel.listUser.observe(this, {listUser ->
            setAdapter(listUser)
        })

        viewModel.isLoading.observe(this,{
            showLoading(it)
        })

        viewModel.isFail.observe(this,{
            if(it){
                Toast.makeText(this@MainActivity,"Data tidak ditemukan/gagal dimuat",Toast.LENGTH_SHORT).show()
            }
        })
        binding.searchText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
                if(s.length==0){
                    val remove = ArrayList<UserResponse>()
                    val adapter = ListUserAdapter(remove)
                    binding.rvUser.adapter = adapter
                }
            }
            override fun afterTextChanged(s: Editable) {
            }
        })

        binding.cari.setOnClickListener {
            viewModel.findUser(binding.searchText.text.toString())
        }

        settingViewModel.getThemeSettings().observe(this,{
                isDarkModeActive: Boolean->
            if(isDarkModeActive){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                dark_mode = false
                Toast.makeText(this,"Dark mode is on",Toast.LENGTH_SHORT).show()
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                dark_mode = true
                Toast.makeText(this,"Dark mode is off",Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun setMyButtonEnable() {
        val result = binding.searchText.text
        binding.cari.isEnabled = result != null && result.toString().isNotEmpty()
    }

    fun setAdapter(listUser: ArrayList<UserResponse>){
        val adapter = ListUserAdapter(listUser)
        binding.rvUser.adapter = adapter
        adapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserParcelable) {
                showSelectedUser(data)
            }
        })
    }

    private fun showSelectedUser(data: UserParcelable){
        val intent = Intent(this@MainActivity,DetailActivity::class.java)
        intent.putExtra(DetailActivity.EXTRA_DATA,data)
        startActivity(intent)
    }

    fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        when(id){
            R.id.setting -> settingViewModel.saveThemeSetting(dark_mode)
            R.id.favorite ->{
                val intent = Intent(this@MainActivity, FavoriteActivity::class.java)
                startActivity(intent)
            }
        }


        return super.onOptionsItemSelected(item)
    }
}