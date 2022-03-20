package com.example.githubusernaviapi.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubusernaviapi.adapter.ListUserAdapter
import com.example.githubusernaviapi.responses.UserResponse
import com.example.githubusernaviapi.database.UserParcelable
import com.example.githubusernaviapi.viewmodel.FollowersViewModel
import com.example.githubusernaviapi.databinding.FragmentFollowersBinding


class FollowersFragment : Fragment() {
    private var _binding: FragmentFollowersBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FollowersViewModel

    companion object {
        const val ARG_NAME = "userName"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowersBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username = arguments?.getString(ARG_NAME)

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvFollowers.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding.rvFollowers.addItemDecoration(itemDecoration)

        viewModel = ViewModelProvider(requireActivity()).get(FollowersViewModel::class.java)

        viewModel.listUser.observe(requireActivity(), {listUser ->
            setAdapter(listUser)
        })

        viewModel.isLoading.observe(requireActivity(),{
            showLoading(it)
        })

        if (username != null) {
            viewModel.getFollowers(username)
        }
    }

    fun setAdapter(listUser: ArrayList<UserResponse>){
        val adapter = ListUserAdapter(listUser)
        binding.rvFollowers.adapter = adapter
        adapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserParcelable) {
            }
        })
    }


    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBarFollowers.visibility = View.VISIBLE
        } else {
            binding.progressBarFollowers.visibility = View.GONE
        }
    }
}