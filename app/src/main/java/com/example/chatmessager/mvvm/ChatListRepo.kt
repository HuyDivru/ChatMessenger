package com.example.chatmessager.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.chatmessager.Utils
import com.example.chatmessager.model.RecentChats
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class ChatListRepo {

    val firestore=FirebaseFirestore.getInstance()

    fun getAllChatList() : LiveData<List<RecentChats>>{
        val mainChatList = MutableLiveData<List<RecentChats>>()

        firestore.collection("Conversation${Utils.getUidLoggedIn()}").orderBy("time",Query.Direction.DESCENDING)
            .addSnapshotListener{
                snapshot, exception ->
                if(exception!=null){
                    return@addSnapshotListener
                }
                val chatlist= mutableListOf<RecentChats>()

                snapshot?.forEach{
                    documnet ->
                    val chatlistmodel =documnet.toObject(RecentChats::class.java)

                    if(chatlistmodel.sender.equals(Utils.getUidLoggedIn())){
                        chatlistmodel.let {
                            chatlist.add(it)
                        }
                    }
                }
                mainChatList.value=chatlist
            }
        return mainChatList
    }
}