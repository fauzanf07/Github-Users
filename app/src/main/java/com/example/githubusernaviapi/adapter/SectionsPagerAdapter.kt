package com.example.githubusernaviapi.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.githubusernaviapi.fragments.FollowersFragment
import com.example.githubusernaviapi.fragments.FollowingFragment

class SectionsPagerAdapter(activity: AppCompatActivity): FragmentStateAdapter(activity) {
    var userName: String = ""
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when(position){
            0-> fragment = FollowersFragment()
            1-> fragment = FollowingFragment()
        }
        fragment?.arguments = Bundle().apply {
            putString(FollowersFragment.ARG_NAME,userName)
        }
        return fragment as Fragment
    }
}