package com.example.reachbuddy.ui.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reachbuddy.Adapters.ProfileRecyclerViewAdapter

import com.example.reachbuddy.R
import com.example.reachbuddy.ViewModels.ProfileViewModel
import com.example.reachbuddy.ViewModels.SocialViewModel
import com.example.reachbuddy.databinding.FragmentFriendsListBinding
import com.example.reachbuddy.ui.PrivateChatActivity
import com.example.reachbuddy.utils.Constants
import com.example.reachbuddy.utils.Topspacingdecoration

/**
 * A simple [Fragment] subclass.
 */
class FriendsListFragment : Fragment(),ProfileRecyclerViewAdapter.onClicklistener{

    lateinit var binding: FragmentFriendsListBinding
    lateinit var profileadapter:ProfileRecyclerViewAdapter
    lateinit var viewModel: ProfileViewModel
    lateinit var socialViewModel: SocialViewModel
    lateinit var chatFragment: FriendChatFragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentFriendsListBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel=(activity as PrivateChatActivity).viewModel
        socialViewModel=(activity as PrivateChatActivity).socialViewModel
        initrecyclerview()
        viewModel.upadateForFriendsListRecyclerView(profileadapter)
        chatFragment= FriendChatFragment()

    }

    /*
  This will initialise the recyclerView
   */
    private fun initrecyclerview()
    {
        binding.friendsrecyclerview.apply {
            layoutManager= LinearLayoutManager(activity)
            profileadapter= ProfileRecyclerViewAdapter(this@FriendsListFragment, Constants.FOR_PROFILE_VIEWING)
            val topspace= Topspacingdecoration(5)
            addItemDecoration(topspace)
            adapter= profileadapter
        }
    }

    override fun Onclick(position: Int, what: String) {
        //Toast.makeText(activity,"Click Working", Toast.LENGTH_SHORT).show()
        socialViewModel.piclink=profileadapter.getprofileatpostion(position).UserProfilePicLink.toString()
        (activity as PrivateChatActivity).supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentholder,chatFragment)
            addToBackStack(null)
            commit()

        }
    }

}
