package com.example.reachbuddy.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.reachbuddy.Models.UserProfile
import com.example.reachbuddy.R

class ProfileRecyclerViewAdapter : RecyclerView.Adapter<ProfileRecyclerViewAdapter.ViewHolder>() {

    private var listofProfiles:MutableList<UserProfile> = mutableListOf<UserProfile>()
    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view)
    {
        val profilepic : ImageView =view.findViewById(R.id.ProfilePicImg)
        val profilename:TextView=view.findViewById(R.id.DisplayNameTxt)
    }

    fun updatelist(list: MutableList<UserProfile>)
    {
        Log.e("got called","RecyclerView Profile Funtion")
        listofProfiles=list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.profilecardview,parent,false)
        )
    }

    override fun getItemCount(): Int {
        return listofProfiles.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.profilename.text=listofProfiles.get(position).UserName
        Glide.with(holder.itemView).load(listofProfiles.get(position).UserProfilePicLink).circleCrop().into(holder.profilepic)
    }

}