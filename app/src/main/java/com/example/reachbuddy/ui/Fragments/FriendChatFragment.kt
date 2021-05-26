package com.example.reachbuddy.ui.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reachbuddy.Adapters.MessageRecyclerViewAdapter

import com.example.reachbuddy.R
import com.example.reachbuddy.Repository.repository
import com.example.reachbuddy.ViewModels.SocialViewModel
import com.example.reachbuddy.ViewModels.SocialViewModelProvider
import com.example.reachbuddy.databinding.FragmentFriendChatBinding
import com.example.reachbuddy.ui.PrivateChatActivity
import com.example.reachbuddy.utils.Topspacingdecoration

/**
 * A simple [Fragment] subclass.
 */
class FriendChatFragment : Fragment() {

    lateinit var binding: FragmentFriendChatBinding
    lateinit var socialViewModel: SocialViewModel
    lateinit var  messageadapter: MessageRecyclerViewAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentFriendChatBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        socialViewModel=(activity as PrivateChatActivity).socialViewModel
        socialViewModel.getInitalPrivateMsg(socialViewModel.piclink)

        socialViewModel.chats.observe(viewLifecycleOwner, Observer {
           if(it!=null)
            messageadapter.updatelist(it)
        })

        initrecyclerview()
        binding.friendBtnSend.setOnClickListener {
            socialViewModel.writeChatmessage(binding.friendTxtMessage.text.toString(),socialViewModel.piclink)
        }

    }

    private fun initrecyclerview()
    {
        binding.friendMsgRecyclerView.apply {
            layoutManager= LinearLayoutManager(activity)
            messageadapter= MessageRecyclerViewAdapter()
            val topspace= Topspacingdecoration(5)
            addItemDecoration(topspace)
            adapter= messageadapter
        }

    }
}



