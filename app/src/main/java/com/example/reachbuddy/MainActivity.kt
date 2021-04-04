package com.example.reachbuddy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reachbuddy.Adapters.MessageRecyclerViewAdapter
import com.example.reachbuddy.Models.UserMessage
import com.example.reachbuddy.Models.Users
import com.example.reachbuddy.Repository.repository
import com.example.reachbuddy.ViewModels.SocialViewModel
import com.example.reachbuddy.ViewModels.SocialViewModelProvider
import com.example.reachbuddy.utils.Constants
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

    private lateinit var viewModel: SocialViewModel
    lateinit var  messageadapter:MessageRecyclerViewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db= Firebase.firestore
        //generating the user instance from the data from login activity
        val user=getuserclass()

        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY).toString()
        val minute = c.get(Calendar.MINUTE).toString()


        val rep=repository()
        val viewmodelProvider=SocialViewModelProvider(rep)
        viewModel=ViewModelProvider(this,viewmodelProvider).get(SocialViewModel::class.java)
        viewModel.writeuserinfo(user)

        initrecyclerview()



        viewModel.messegelist.observe(this, Observer {
            messageadapter.updatelist(it)
        })


        btn_send.setOnClickListener {
            viewModel.writemessage(UserMessage(txt_message.text.toString(),user,"$hour:$minute"))
            txt_message.text.clear()
        }

       // viewModel.getmessages()

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
            msg_recyclerView.scrollToPosition(messageadapter.itemCount-1)
        }


    }




    //function to get all the data from the login activity and creating and returning
    //the user instance from the data to be used in write function
    fun getuserclass():Users
    {
        val bundle=intent.extras
        val user_name=bundle?.getString(USER_NAME_KEY)
        val user_uid=bundle?.getString(USER_UID_KEY)
        val user_image=bundle?.getString(USER_IMAGE_KEY)
        return Users(user_name,user_uid,user_image)
    }

    private fun initrecyclerview()
    {
        msg_recyclerView.apply {
            layoutManager= LinearLayoutManager(this@MainActivity)
            messageadapter= MessageRecyclerViewAdapter()
            val topspace= Topspacingdecoration(5)
            addItemDecoration(topspace)
            adapter= messageadapter
        }
        messageadapter.updatelist(viewModel.initialgetmsg())
    }
}
