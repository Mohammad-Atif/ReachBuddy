package com.example.reachbuddy.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.reachbuddy.Models.UserMessage
import com.example.reachbuddy.R

class MessageRecyclerViewAdapter : RecyclerView.Adapter<MessageRecyclerViewAdapter.ViewHolder>() {

    private var messegeslist: MutableList<UserMessage> = mutableListOf<UserMessage>()

    //this is the function which is called inside the observer of viewmodel to update the messegelist
    fun updatelist(list: MutableList<UserMessage>)
    {
        Log.e("got called","RecyclerView Funtion")
        messegeslist=list
        notifyDataSetChanged()
    }


    class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val messageview:TextView=view.findViewById(R.id.messagetxtview)
        val senderview:TextView=view.findViewById(R.id.sender_name_view)
        val dateSendedview:TextView=view.findViewById(R.id.date_sended_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.messagecardview,parent,false)
        )
    }

    override fun getItemCount(): Int {
        return messegeslist.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.messageview.text=messegeslist.get(position).user_message
        holder.senderview.text=messegeslist.get(position).user.user_name
        holder.dateSendedview.text=messegeslist.get(position).message_time
    }
}