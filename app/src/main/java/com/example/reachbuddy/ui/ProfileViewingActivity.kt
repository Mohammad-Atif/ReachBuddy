package com.example.reachbuddy.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.reachbuddy.R
import com.example.reachbuddy.ViewModels.ProfileViewModel
import com.example.reachbuddy.databinding.ActivityProfileViewingBinding
import com.example.reachbuddy.utils.Constants
import com.example.reachbuddy.utils.Constants.Companion.EXTRA_NAME
import com.example.reachbuddy.utils.Constants.Companion.IS_FREIND_REQ_SENT
import com.example.reachbuddy.utils.Constants.Companion.IS_FRIEND
import com.example.reachbuddy.utils.Constants.Companion.REQUEST_CODE_IMAGE_PICK

class ProfileViewingActivity : AppCompatActivity() {

    lateinit var binding: ActivityProfileViewingBinding
    lateinit var viewModel: ProfileViewModel
    var curFile: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityProfileViewingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username=intent.getStringExtra(EXTRA_NAME).toString()

        viewModel=ViewModelProvider(this).get(ProfileViewModel::class.java)
        viewModel.getisliked(username)

        viewModel.getcurrentuserprofile(username)

        viewModel.imagelink.observe(this, Observer {link->
            //Glide library to load image from a url into a imageView
            Glide.with(this).load(link).circleCrop().into(binding.ImgProfilePic)
        })


        binding.TxtProfileBio.isEnabled=false

        viewModel.userprofile.observe(this, Observer {userprofile->
            binding.TxtProfileName.text=userprofile.UserName.toString()
            binding.TxtLikesCount.text=userprofile.LikesCount.toString()
            binding.TxtProfileBio.isEnabled=true
            binding.TxtProfileBio.setText(userprofile.UserBio.toString())
            binding.TxtProfileBio.isEnabled=false
            binding.TxtLikesCount.text=userprofile.LikesCount.toString()

        })

        viewModel.isLikedByYou.observe(this, Observer {
            if(it==true)
                binding.ImgLike.setImageResource(R.drawable.ic_favorite_black_24dp)
            else
                binding.ImgLike.setImageResource(R.drawable.ic_favorite_border_black_24dp)
        })



        binding.ImgEditBio.setOnClickListener {
            if(username==viewModel.getuserclasshere().user_name.toString())
            {
                if(binding.TxtProfileBio.isEnabled==true)
                {
                    viewModel.updateBio(binding.TxtProfileBio.text.toString())
                    binding.TxtProfileBio.isEnabled=false
                }
                else
                {
                    binding.TxtProfileBio.isEnabled=true
                    binding.TxtProfileBio.requestFocus()
                }
            }

        }

        binding.ImgLike.setOnClickListener {
            viewModel.managelikes(username)
        }

        if(username==viewModel.getuserclasshere().user_name.toString())
            binding.btnMakeBuddy.visibility = View.INVISIBLE


        viewModel.friendship.observe(this, Observer {
            if(it[IS_FRIEND]!!)
            {
                binding.btnMakeBuddy.text="UNFRIEND"
            }
            else if(it[IS_FREIND_REQ_SENT]!!)
            {
                binding.btnMakeBuddy.text="UNSEND REQUEST"
            }
            else if(!it[IS_FREIND_REQ_SENT]!!)
            {
                binding.btnMakeBuddy.text="MAKE BUDDY"
            }
        })

        binding.btnMakeBuddy.setOnClickListener {
            viewModel.managefriendship(username)
        }

        val popup=PopupMenu(this,binding.ImgProfilePic)
        popup.setOnMenuItemClickListener {
            if(it.itemId==R.id.changeImageMenu)
            {
                starttheoperation()
            }
                return@setOnMenuItemClickListener true
        }

        binding.ImgProfilePic.setOnLongClickListener {

            popup.inflate(R.menu.uploadpopup)
            popup.show()

            return@setOnLongClickListener true
        }

//        binding.ImgProfilePic.setOnClickListener {
//            val popup=PopupMenu(this,binding.ImgProfilePic)
//            popup.inflate(R.menu.uploadpopup)
//            popup.show()
//        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_IMAGE_PICK) {
            data?.data?.let {
                curFile = it
                viewModel.handlepicupload(it)
            }
        }
    }

    private fun starttheoperation()
    {
        Intent(Intent.ACTION_GET_CONTENT).also {
            it.type = "image/*"
            startActivityForResult(it, REQUEST_CODE_IMAGE_PICK)
        }
    }
}
