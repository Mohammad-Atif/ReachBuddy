package com.example.reachbuddy.ViewModels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reachbuddy.Adapters.ProfileRecyclerViewAdapter
import com.example.reachbuddy.Daos.FirebaseDao
import com.example.reachbuddy.Daos.ProfileDao
import com.example.reachbuddy.Models.UserProfile
import com.example.reachbuddy.Models.Users
import com.example.reachbuddy.utils.Constants.Companion.ACCEPT_STRING
import com.example.reachbuddy.utils.Constants.Companion.DECLINE_STRING
import com.example.reachbuddy.utils.Constants.Companion.DEFAULT_BIO
import com.example.reachbuddy.utils.Constants.Companion.IS_FREIND_REQ_SENT
import com.example.reachbuddy.utils.Constants.Companion.IS_FRIEND
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.launch


/*
This is the viewModel used in MainMenu Activity and further(i created to used in MainMenu)

Here this ViewModel does not taking any values inside its constructor, so we does not have to
make its viewModelProvider factory class seperately.

It's instance is created just like  viewModel=ViewModelProvider(this).get(MenuViewModel::class.java)
Android will automatically provide its own viewModelProviderFactory

 */

class ProfileViewModel : ViewModel(){

    var imageurl=getuserclasshere().user_image_url
    val imagelink: MutableLiveData<String> = MutableLiveData()
    val LikedByUsers:MutableLiveData<MutableList<String>> = MutableLiveData()
    val isLikedByYou:MutableLiveData<Boolean> = MutableLiveData()
    val friendship: MutableLiveData<MutableMap<String,Boolean>> = MutableLiveData()      //this will contain two key isFriend and isFriendReuest Sent
    val RequestsCount: MutableLiveData<Int> = MutableLiveData()
    val dao=ProfileDao()

    val userprofile:MutableLiveData<UserProfile> = MutableLiveData()

    fun getuserclasshere():Users
    {
        val user= Firebase.auth.currentUser
        val user_name=user.displayName
        val user_uid=user.uid
        val user_image=user.photoUrl.toString()
        return Users(user_name,user_uid,user_image)
    }

    fun getProfile()=dao.getUserProfileInstant(getuserclasshere().user_uid.toString())

    fun writeuserprofile(username: String,piclink:String,newBio:String,likescount:String,list: MutableList<String>? = mutableListOf(), FriendsList: MutableList<String> = mutableListOf(),FriendRequestList: MutableList<String> = mutableListOf()){
        val userProfile= UserProfile(
            username,
            piclink,
            likescount.toInt(),
            newBio,
            list!!,
            FriendsList,
            FriendRequestList
        )

        var user: Users?= null

        /*
        This is to add to the specific user uid by getting the uid from getuid
         */
        val task = dao.getuid(piclink)
        task.addOnSuccessListener {
            if(it.documents.size > 0)
            {
                user=it.documents.get(0).toObject<Users>()
                viewModelScope.launch {
                    dao.addUserProfile(userProfile,user?.user_uid.toString())
                }

            }
            else
            {
                viewModelScope.launch {
                    dao.addUserProfile(userProfile)
                }
            }
        }


        userprofile.postValue(userProfile)
        LikedByUsers.postValue(userProfile.LikedBy)
    }

    /*
    creating tempoarary funtion to handl userprofilepic link becuase by defaul thte pic link is by
    fetuserclasshere() so for any other user the pic will be of current user
    So this  fuction will take username as parameter and sets the piclink of that user in imageurl
     */



    fun getcurrentuserprofile(username: String): UserProfile?
    {
        var userProfile:UserProfile? = null

        val task=dao.getProfileByName(username)
        task.addOnSuccessListener {
            if(it.documents.size > 0) {
                userProfile=it.documents.get(0).toObject<UserProfile>()
                userprofile.postValue(userProfile)
                Log.e("ch","got executed")
                val e= userProfile!!.LikedBy
                LikedByUsers.postValue(e)
                imagelink.postValue(userProfile!!.UserProfilePicLink.toString())
                checkfriendship(userProfile!!.FriendRequestList,userProfile!!.FriendsList)

            }
            else
            {
                writeuserprofile(getuserclasshere().user_name.toString(),getuserclasshere().user_image_url.toString(),DEFAULT_BIO,"0")
            }
        }


        return userProfile
    }

    fun getisliked(username:String)
    {
        val task=dao.getProfileByName(username)
        task.addOnSuccessListener {
            if (it.documents.size > 0)
            {
                val userprof = it.documents.get(0).toObject<UserProfile>()
                val listofLikedBy = userprof?.LikedBy
                var flag=0
                if (listofLikedBy != null) {
                    for (uid in listofLikedBy) {
                        if (uid == getuserclasshere().user_uid) {
                            isLikedByYou.postValue(true)
                            flag=1
                            break
                        }
                    }
                }
                if (flag == 0)
                    isLikedByYou.postValue(false)
             }
            else
                isLikedByYou.postValue(false)
        }
    }

    /*
    This functon will handle to increase and decrease the likes
     */

    fun managelikes(username: String)
    {
        val task=dao.getProfileByName(username)
        task.addOnSuccessListener {
            val userprof=it.documents.get(0).toObject<UserProfile>()
            val listofLikedBy=userprof?.LikedBy
            var likesCount = userprof?.LikesCount!!.toInt()
            if(isLikedByYou.value==true)
            {
                listofLikedBy?.remove(getuserclasshere().user_uid.toString())
                likesCount = likesCount - 1
                isLikedByYou.postValue(false)
            }
            else
            {
                listofLikedBy?.add(getuserclasshere().user_uid.toString())
                likesCount = likesCount + 1
                isLikedByYou.postValue(true)
            }


            writeuserprofile(userprof.UserName.toString(),
                userprof.UserProfilePicLink.toString(),
                userprof.UserBio.toString(),likesCount.toString(),listofLikedBy,userprof.FriendsList,userprof.FriendRequestList)
        }
    }

    /*
    This function is to update the bio of the user
     */

    fun updateBio(userBio:String)
    {
        val task=dao.getProfileByName(getuserclasshere().user_name.toString())
        task.addOnSuccessListener {
            val userprof=it.documents.get(0).toObject<UserProfile>()
            val listofLikedBy=userprof?.LikedBy
            val likesCount = userprof?.LikesCount!!.toInt()


            writeuserprofile(userprof.UserName.toString(),userprof.UserProfilePicLink.toString(),userBio,likesCount.toString(),listofLikedBy,userprof.FriendsList,userprof.FriendRequestList)
        }
    }

    fun updaterecyclerviewlist( profileadapter: ProfileRecyclerViewAdapter)
    {
        val task=dao.getAllUserProfiles()
        task.addOnSuccessListener {
            val list: MutableList<UserProfile> = mutableListOf()
            for(document in it)
            {
                val userProfile=document.toObject<UserProfile>()
                list.add(userProfile)
            }
            profileadapter.updatelist(list)
        }
    }

    /*
    Creating new method to update the recyclerviewlist with friend request list to show in friendrequestactivity
     */

    fun upadateForFriendsRecyclerView(profileadapter: ProfileRecyclerViewAdapter)
    {
        val task=dao.getProfileByName(getuserclasshere().user_name.toString())
        task.addOnSuccessListener {
            val userprof=it.documents.get(0).toObject<UserProfile>()
            val listRequestUid= userprof?.FriendRequestList
            if (listRequestUid != null) {
                for(uid in listRequestUid) {
                    val t=dao.getUserbyUid(uid)
                    t.addOnSuccessListener {
                        val prof=it.toObject<UserProfile>()
                        profileadapter.addtolist(prof!!)

                    }
                }
            }

        }
    }

    /*
    Creating new method to update the recyclerviewlist with friends list to show in friendlist Fragment
     */

    fun upadateForFriendsListRecyclerView(profileadapter: ProfileRecyclerViewAdapter)
    {
        val task=dao.getProfileByName(getuserclasshere().user_name.toString())
        task.addOnSuccessListener {
            val userprof=it.documents.get(0).toObject<UserProfile>()
            val listRequestUid= userprof?.FriendsList
            if (listRequestUid != null) {
                for(uid in listRequestUid) {
                    val t=dao.getUserbyUid(uid)
                    t.addOnSuccessListener {
                        val prof=it.toObject<UserProfile>()
                        profileadapter.addtolist(prof!!)

                    }
                }
            }

        }
    }


    fun writeuserinfo(){
        val users=getuserclasshere()

        viewModelScope.launch {
            FirebaseDao.writeuser(users)
        }
    }

    /*
    This function is to check wheather the user is friend or has sent/not sent friend request to the
    user of which he is viewing profile.
    It will update friendship Livedata .
    We call this function is getcurrentuserprofile thats why we made is private because we are not calling
    it from outsde the class
     */
    private fun checkfriendship(requestlist:MutableList<String>,friendlist:MutableList<String>)
    {
        val map = mutableMapOf<String,Boolean>()
        if(requestlist.find { it==getuserclasshere().user_uid.toString() }!=null)
        {
            map.put(IS_FREIND_REQ_SENT,true)
            map.put(IS_FRIEND,false)
        }
        else if(friendlist.find { it==getuserclasshere().user_uid.toString() }!=null)
        {
            map.put(IS_FREIND_REQ_SENT,false)
            map.put(IS_FRIEND,true)
        }
        else
        {
            map.put(IS_FREIND_REQ_SENT,false)
            map.put(IS_FRIEND,false)
        }
        friendship.postValue(map)

    }

     fun managefriendship(username: String)
     {
         val task=dao.getProfileByName(username)
         task.addOnSuccessListener {
             val userprof=it.documents.get(0).toObject<UserProfile>()
             val friendrequeslist=userprof?.FriendRequestList
             val currentfriends=userprof?.FriendsList
             val f= mutableMapOf<String,Boolean>()
             if(friendship.value?.get(IS_FRIEND)!!)
             {
                 currentfriends?.remove(getuserclasshere().user_uid.toString())

                 val getminetask=dao.getUserbyUid(getuserclasshere().user_uid.toString())
                 getminetask.addOnSuccessListener {profil->
                     val mineprofile=profil.toObject<UserProfile>()
                     val minefriends=mineprofile?.FriendsList
                     val getuid=dao.getuid(userprof?.UserProfilePicLink.toString())
                     getuid.addOnSuccessListener {doc->
                         val user=doc.documents.get(0).toObject<Users>()
                         val requestUid= user?.user_uid.toString()
                         minefriends?.remove(requestUid)
                         writeuserprofile(
                             mineprofile?.UserName.toString(),
                             mineprofile?.UserProfilePicLink.toString(), mineprofile?.UserBio.toString(),
                             mineprofile?.LikesCount.toString(),
                             mineprofile?.LikedBy, minefriends!!, mineprofile?.FriendRequestList
                         )
                     }
                 }

                 f[IS_FRIEND] = false
                 f[IS_FREIND_REQ_SENT] = false

             }
             else if(!friendship.value?.get(IS_FRIEND)!! && friendship.value?.get(IS_FREIND_REQ_SENT)!!)
             {
                 friendrequeslist?.remove(getuserclasshere().user_uid.toString())
                 f[IS_FRIEND] = false
                 f[IS_FREIND_REQ_SENT] = false
             }
             else if(!friendship.value?.get(IS_FRIEND)!! && !friendship.value?.get(IS_FREIND_REQ_SENT)!!)
             {
                 friendrequeslist?.add(getuserclasshere().user_uid.toString())
                 Log.e("checking bug","add executed")
                 f[IS_FRIEND] = false
                 f[IS_FREIND_REQ_SENT] = false
             }
             checkfriendship(friendrequeslist!!, currentfriends!!)
             writeuserprofile(
                 userprof.UserName.toString(),
                 userprof.UserProfilePicLink.toString(), userprof.UserBio.toString(),
                 userprof.LikesCount.toString(),
                 userprof.LikedBy, currentfriends, friendrequeslist                //changing it from userprof.friendist to currentfriends
             )
         }
     }


    /*
    This function is to check whether the current user got any friend request or not
    And if got then how many by changing the RequestCount Livedata
     */

    fun getRequestsCount()
    {
        val task=dao.getProfileByName(getuserclasshere().user_name.toString())
        task.addOnSuccessListener {
            val userprof=it.documents.get(0).toObject<UserProfile>()
            val requestslist= userprof?.FriendRequestList
            RequestsCount.postValue(requestslist?.size)
        }
    }

    /*
    Funtion to accept or decline the friendRequest
    21.5.21  this function is currently wrong because it is not updating the database of both the users
    Now changing it to update the database of both users
     */

    fun manageReqeust(perform:String, profileadapter:ProfileRecyclerViewAdapter,position:Int)
    {
        val userpiclink=profileadapter.getprofileatpostion(position).UserProfilePicLink.toString()
        val uidtask=dao.getuid(userpiclink)
        uidtask.addOnSuccessListener {
            val user=it.documents.get(0).toObject<Users>()
            val requestUid= user?.user_uid.toString()
            val task=dao.getUserbyUid(getuserclasshere().user_uid.toString())
            task.addOnSuccessListener {
                val currentProfile=it.toObject<UserProfile>()
                val currentFriends= currentProfile?.FriendsList
                val currentRequests= currentProfile?.FriendRequestList
                val othertask=dao.getUserbyUid(requestUid)
                othertask.addOnSuccessListener {
                    val secondprofile=it.toObject<UserProfile>()
                    val secondFriends=secondprofile?.FriendsList
                    val secondRequests=secondprofile?.FriendRequestList
                    if(perform== ACCEPT_STRING)
                    {
                        currentFriends?.add(requestUid)
                        secondFriends?.add(getuserclasshere().user_uid.toString())
                        currentRequests?.remove(requestUid)
                        secondRequests?.remove(getuserclasshere().user_uid.toString())
                    }
                    else if(perform== DECLINE_STRING)
                    {
                        currentRequests?.remove(requestUid)
                    }
                    writeuserprofile(
                        currentProfile?.UserName.toString(),
                        currentProfile?.UserProfilePicLink.toString(), currentProfile?.UserBio.toString(),
                        currentProfile?.LikesCount.toString(),
                        currentProfile?.LikedBy, currentFriends!!, currentRequests!!
                    )
                    writeuserprofile(
                        secondprofile?.UserName.toString(),
                        secondprofile?.UserProfilePicLink.toString(), secondprofile?.UserBio.toString(),
                        secondprofile?.LikesCount.toString(),
                        secondprofile?.LikedBy, secondFriends!!, secondRequests!!
                    )

                }

                profileadapter.deleteatpos(position)

            }
        }

    }


    //funtion to hadle the upload of the image in the storage and the change the userpiclink to
    //newly uplaoded image

    fun handlepicupload(imageUri: Uri)
    {
        val userUid=getuserclasshere().user_uid.toString()
        viewModelScope.launch {
            val uploadTask=dao.uploadImage(userUid,imageUri)
            val ref=Firebase.storage.reference.child("images/$userUid")

            val urlTask = uploadTask.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                ref.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    //here is the download url
                    val newPicUrl = task.result.toString()
                    val taskprofile=dao.getUserbyUid(userUid)
                    taskprofile.addOnSuccessListener {
                        val currentProfile=it.toObject<UserProfile>()
                        writeuserprofile(
                            currentProfile?.UserName.toString(),
                            newPicUrl, currentProfile?.UserBio.toString(),
                            currentProfile?.LikesCount.toString(),
                            currentProfile?.LikedBy, currentProfile!!.FriendsList, currentProfile!!.FriendRequestList
                        )
                        imagelink.postValue(newPicUrl)

                    }
                }
            }

        }
    }


    //temporary function to handle main nemu pic
    //basically update userpic livedata and fetch from userProfile

    fun updatepicTemporary()
    {
        val task=dao.getUserbyUid(getuserclasshere().user_uid.toString())
        task.addOnSuccessListener {
            val prof=it.toObject<UserProfile>()
            imagelink.postValue(prof?.UserProfilePicLink.toString())
        }
    }






}