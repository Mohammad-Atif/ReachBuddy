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
import com.example.reachbuddy.utils.Constants
import com.example.reachbuddy.utils.Constants.Companion.FOR_PROFILE_VIEWING
import com.example.reachbuddy.utils.Constants.Companion.FOR_REQUESTS_VIEWING
import java.lang.IllegalArgumentException

/*
16.5.21
Updating this RecyclerViewAdapter to handel multiple viewHolder(One for activity viewing and one for FriendRequest Viewing)
Sp Now this class will have another constructer variable called forwhich which will be used to know which viewHolder object
to create and call in Onbind and Oncreate method
 */
class ProfileRecyclerViewAdapter(val listener: onClicklistener,val forwhich: Int): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var listofProfiles:MutableList<UserProfile> = mutableListOf<UserProfile>()

    /*
    Everytime you create a recyclerview adapter you need to create a custom viewHolder class
    that describes what the views looking like in the recyclerview
    It just describles all the views in the recyclerview
     */
    inner class ProfileViewHolder(view: View):RecyclerView.ViewHolder(view),View.OnClickListener
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

    /*
    15.5.21
    Creating this viewholder to hold the views for showing the friend request list
     */

    inner class FriendViewHolder(view: View):RecyclerView.ViewHolder(view),View.OnClickListener
    {
        val userprofilepic : ImageView =view.findViewById(R.id.UserProfilePicImg)
        val userprofilename:TextView=view.findViewById(R.id.UserDisplayNameTxt)
        val userAcceptRequestImg : ImageView = view.findViewById(R.id.CheckImg)
        val userCancelRequestImg : ImageView = view.findViewById(R.id.CancelImg)

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

    //this funtion will not update the entire list but just add the new element to the existing list
    //to be used for friendrequest
    fun addtolist(userprof: UserProfile)
    {
        val l=listofProfiles
        l.add(userprof)
        listofProfiles=l
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
    update 16.5.21 -- now it will return 2 diff objects according to the viewtype
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            FOR_PROFILE_VIEWING -> {
                ProfileViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.profilecardview, parent, false)
            )
            }
            FOR_REQUESTS_VIEWING->{
                FriendViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.profilerequestcardview, parent, false)
                )
            }
            else -> throw IllegalArgumentException("Invalid ViewType")
        }

    }

    override fun getItemCount(): Int {
        return listofProfiles.size
    }

    /*
    This function is to bind the data to the viewholder that is currently is in view
    This binds/assigns the data to the viewHolder
    Whenenver user scrolls new viewHolder Comes and This methods is called
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder)
        {
            is ProfileViewHolder->{
                holder.profilename.text=listofProfiles.get(position).UserName
                Glide.with(holder.itemView).load(listofProfiles.get(position).UserProfilePicLink).circleCrop().into(holder.profilepic)
            }
            is FriendViewHolder->{
                holder.userprofilename.text=listofProfiles.get(position).UserName
                Glide.with(holder.itemView).load(listofProfiles.get(position).UserProfilePicLink).circleCrop()
                    .into(holder.userprofilepic)
            }
        }

    }

    interface onClicklistener{
        fun Onclick(position: Int)
    }

    /*
    This funtion will tell  the Viewtype in the OncreateView funtion
     */

    override fun getItemViewType(position: Int): Int {
        return forwhich
    }

}