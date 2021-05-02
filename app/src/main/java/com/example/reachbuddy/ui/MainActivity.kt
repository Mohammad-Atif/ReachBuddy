package com.example.reachbuddy.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reachbuddy.Adapters.MessageRecyclerViewAdapter
import com.example.reachbuddy.Models.UserMessage
import com.example.reachbuddy.Models.Users
import com.example.reachbuddy.Repository.repository
import com.example.reachbuddy.ViewModels.SocialViewModel
import com.example.reachbuddy.ViewModels.SocialViewModelProvider
import com.example.reachbuddy.databinding.ActivityMainBinding
import com.example.reachbuddy.utils.Constants.Companion.USER_IMAGE_KEY
import com.example.reachbuddy.utils.Constants.Companion.USER_NAME_KEY
import com.example.reachbuddy.utils.Constants.Companion.USER_UID_KEY
import com.example.reachbuddy.utils.Topspacingdecoration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    /*
    Here changing to viewbinding now because kottlin synthetics are deprecated :(
    date-5/4/21
     */

    private lateinit var viewModel: SocialViewModel
    private lateinit var binding: ActivityMainBinding      //binding object used for viewBinding
    lateinit var  messageadapter:MessageRecyclerViewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        val root=binding.root
        setContentView(root)

        val db= Firebase.firestore
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY).toString()
        val minute = c.get(Calendar.MINUTE).toString()

        val rep=repository()
        val viewmodelProvider=SocialViewModelProvider(rep)
        viewModel=ViewModelProvider(this,viewmodelProvider).get(SocialViewModel::class.java)

        initrecyclerview()

        viewModel.messegelist.observe(this, Observer {
            messageadapter.updatelist(it)
        })

        btn_send.setOnClickListener {
            viewModel.writemessage(UserMessage(binding.txtMessage.text.toString(),viewModel.getuserclass(),"$hour:$minute"))
            binding.txtMessage.text.clear()
        }

        /*
        This is the snapshot Listener used to get the instance messeges
        It is executed whenever the database changes
         */

        val msgref=db.collection("MESSAGES")
        val messages: MutableList<UserMessage> = mutableListOf()
        msgref.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w("failed", "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null) {
                lateinit var userMessage: UserMessage
                for (document in snapshot) {
                    userMessage = document.toObject<UserMessage>()
                    messages.add(userMessage)
                }
            }
            messageadapter.updatelist(messages)
            binding.msgRecyclerView.scrollToPosition(messageadapter.itemCount-1)
        }



    }

    /*
    This will initialise the recyclerView
     */
    private fun initrecyclerview()
    {
        binding.msgRecyclerView.apply {
            layoutManager= LinearLayoutManager(this@MainActivity)
            messageadapter= MessageRecyclerViewAdapter()
            val topspace= Topspacingdecoration(5)
            addItemDecoration(topspace)
            adapter= messageadapter
        }
        messageadapter.updatelist(viewModel.initialgetmsg())
    }
}
