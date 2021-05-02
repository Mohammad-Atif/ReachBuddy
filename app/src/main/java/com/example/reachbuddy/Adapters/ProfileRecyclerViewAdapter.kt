package com.example.reachbuddy.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.reachbuddy.Models.UserProfile
import com.example.reachbuddy.R

class ProfileRecyclerViewAdapter(val listener: onClicklistener): RecyclerView.Adapter<ProfileRecyclerViewAdapter.ViewHolder>() {

    private var listofProfiles:MutableList<UserProfile> = mutableListOf<UserProfile>()

    /*
    Everytime you create a recyclerview adapter you need to create a custom viewHolder class
    that describes what the views looking like in the recyclerview
    It just describles all the views in the recyclerview
     */
    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view),View.OnClickListener
    {
        val profilepic : ImageView =view.findViewById(R.id.ProfilePicImg)
        val profilename:TextView=view.findViewById(R.id.DisplayNameTxt)

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val p = adapterPosition
            if (p != RecyclerView.NO_POSITION) {
                listener.Onclick(p)
            }
        }


    }

    //this fucntion is to update the list when new pofile are added or profiles are deleted
    fun updatelist(list: MutableList<UserProfile>)
    {
        Log.e("got called","RecyclerView Profile Funtion")
        listofProfiles=list
        notifyDataSetChanged()
    }

    /*
    This funtion will return the profile at given postion from listofprofile which is then used in all profiles activity
    Since we cant make listofprofile public it is not a good practice
     */

    fun getprofileatpostion(position: Int): UserProfile {
        return listofProfiles[position]
    }

    /*
    This method is responsible for creating each on of viewholders which are displayed inside the recyclerview to user
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.profilecardview,parent,false)
        )
    }

    override fun getItemCount(): Int {
        return listofProfiles.size
    }

    /*
    This function is to bind the data to the viewholder that is currently is in view
    This binds/assigns the data to the viewHolder
    Whenenver user scrolls new viewHolder Comes and This methods is called
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.profilename.text=listofProfiles.get(position).UserName
        Glide.with(holder.itemView).load(listofProfiles.get(position).UserProfilePicLink).circleCrop().into(holder.profilepic)
    }

    interface onClicklistener{
        fun Onclick(position: Int)
    }

}