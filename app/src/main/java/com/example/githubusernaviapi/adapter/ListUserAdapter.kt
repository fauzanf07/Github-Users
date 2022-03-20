package com.example.githubusernaviapi.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubusernaviapi.responses.UserResponse
import com.example.githubusernaviapi.database.UserParcelable
import com.example.githubusernaviapi.databinding.ItemUsersRowBinding

class ListUserAdapter (private val listUser: ArrayList<UserResponse>) : RecyclerView.Adapter<ListUserAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    class ListViewHolder(var binding: ItemUsersRowBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemUsersRowBinding.inflate(LayoutInflater.from(viewGroup.context),viewGroup,false)
        return ListViewHolder(binding)
    }

    @SuppressLint("SetTextI18n", "ResourceAsColor")
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        var (followers,avatar_url,following, name,bio, location, login) = listUser[position]
        name = name ?: "No Name"
        bio = bio ?: "No Bio"
        location = location?: "No Location"
        holder.apply {
            Glide.with(itemView.getContext())
                .load(avatar_url)
                .into(binding.photoProfile)
            binding.profileName.setText(name.toString())
            binding.username.setText(login)
            binding.followers.setText("$followers Followers")
            binding.followings.setText("$following Followings")
            binding.location.setText(location.toString())

            val detailUser = UserParcelable(0,followers,avatar_url,following,
                name.toString(), bio.toString(), location.toString(), login)
            itemView.setOnClickListener{ onItemClickCallback.onItemClicked(detailUser)}
        }

    }

    override fun getItemCount(): Int {
        Log.d("Adapter",listUser.size.toString())
        return listUser.size
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: UserParcelable)
    }
}