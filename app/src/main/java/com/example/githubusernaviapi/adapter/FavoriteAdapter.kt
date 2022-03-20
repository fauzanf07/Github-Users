package com.example.githubusernaviapi.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubusernaviapi.database.UserParcelable
import com.example.githubusernaviapi.databinding.ItemUsersRowBinding

class FavoriteAdapter: RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {
    private val listUsers = ArrayList<UserParcelable>()

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    fun setListUsers(listUsers: List<UserParcelable>){
        this.listUsers.clear()
        this.listUsers.addAll(listUsers)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemUsersRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(listUsers[position])
    }

    override fun getItemCount(): Int {
        return listUsers.size
    }

    inner class FavoriteViewHolder(private val binding: ItemUsersRowBinding): RecyclerView.ViewHolder(binding.root){
        @SuppressLint("SetTextI18n")
        fun bind(user: UserParcelable){
            val name = user.name ?: "No Name"
            val bio = user.bio ?: "No Bio"
            val location = user.location?: "No Location"
            with(binding){
                    Glide.with(itemView.getContext())
                        .load(user.avatar_url)
                        .into(photoProfile)
                    profileName.setText(name)
                    username.setText(user.login)
                    followers.setText("${user.followers} Followers")
                    followings.setText("${user.following} Followings")
                    binding.location.setText(location)

            }
            val detailUser = UserParcelable(user.id,user.followers,user.avatar_url,user.following,
                name, bio, location, user.login)
            itemView.setOnClickListener{ onItemClickCallback.onItemClicked(detailUser)}
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: UserParcelable)
    }
}