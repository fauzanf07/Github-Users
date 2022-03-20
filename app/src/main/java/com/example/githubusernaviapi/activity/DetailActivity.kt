package com.example.githubusernaviapi.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubusernaviapi.adapter.SectionsPagerAdapter
import com.example.githubusernaviapi.R
import com.example.githubusernaviapi.database.UserParcelable
import com.example.githubusernaviapi.databinding.ActivityDetailBinding
import com.example.githubusernaviapi.viewmodel.factory.FavoriteModelFactory
import com.example.githubusernaviapi.viewmodel.UserViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {
    private  lateinit var binding: ActivityDetailBinding
    private var getUserName: String ="sidiqpermana"
    private var favUser: UserParcelable?= null
    private lateinit var userViewModel: UserViewModel
    companion object{
        const val EXTRA_DATA = "extra_data"
        const val url_github = "https://github.com/"
        @StringRes
        private  val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = intent.getParcelableExtra<UserParcelable>(EXTRA_DATA) as UserParcelable
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        sectionsPagerAdapter.userName = data.login.toString()

        TabLayoutMediator(tabs,viewPager){ tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        getUserName = data.login.toString()

        showInfo(data)

        binding.kunjungi.setOnClickListener {
            val url = url_github+ data.login
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }

        userViewModel = obtainViewModel(this)

        favUser = UserParcelable()
        favUser.let{user->
            user?.followers = data.followers
            user?.avatar_url = data.avatar_url
            user?.following = data.following
            user?.name = data.name
            user?.bio = data.bio
            user?.location = data.location
            user?.login = data.login
        }

        val fav = userViewModel.isFavorite(favUser)
        if(fav){
            binding.favorite.setText(R.string.hapus_favorit)
        }

        userViewModel.isFavorite.observe(this,{ isFavorite->
            isFavorite(isFavorite,data)

        })

        binding.favorite.setOnClickListener{
            userViewModel.isFavorite(favUser,true)
        }

    }

    private fun isFavorite(it: Boolean?, data: UserParcelable) {
        val id = userViewModel.getId(favUser)
        if(!it!!){
            userViewModel.insert(favUser as UserParcelable)
            Toast.makeText(this, "User telah ditambahkan kedalam favorit", Toast.LENGTH_SHORT)
                    .show()
            binding.favorite.setText(R.string.hapus_favorit)
            favUser?.id = id
        }else{
            if(data.id!=0) favUser?.id = data.id else favUser?.id = id
            userViewModel.delete(favUser as UserParcelable)
            Toast.makeText(this, "User telah dihapus dari favorit", Toast.LENGTH_SHORT)
                .show()
            binding.favorite.setText(R.string.tambah_favorit)
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): UserViewModel {
        val factory = FavoriteModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity,factory).get(UserViewModel::class.java)
    }

    @SuppressLint("SetTextI18n")
    private fun showInfo(data: UserParcelable){
        Glide.with(this@DetailActivity)
            .load(data.avatar_url)
            .into(binding.detailPp)
        if(data.name.equals("null")) binding.detailName.setText("No Name") else binding.detailName.setText(data.name)
        binding.detailUsername.setText(data.login)
        if(data.bio.equals("null")) binding.detailBio.setText("No Bio") else binding.detailBio.setText(data.bio)
        binding.detailFollowers.setText("${data.followers} Followers")
        binding.detailFollowings.setText("${data.following} Following")
        if(data.location.equals("null")) binding.detailLocation.setText("No Location") else binding.detailLocation.setText(data.location)
    }
}