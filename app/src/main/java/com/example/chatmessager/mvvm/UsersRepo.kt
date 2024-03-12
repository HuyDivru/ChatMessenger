package com.example.chatmessager.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.chatmessager.Utils
import com.example.chatmessager.model.Users
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User

class UsersRepo {
    private val firestore= FirebaseFirestore.getInstance()

    fun getUsers():LiveData<List<Users>>{
        val users = MutableLiveData<List<Users>>()
        firestore.collection("Users").addSnapshotListener{
            snapshot, exception ->
                if(exception!=null){
                   return@addSnapshotListener
                }
            val usersList= mutableListOf<Users>()
            snapshot?.documents?.forEach {
                documnet ->
                val user= documnet.toObject(Users::class.java)
                if(user!!.userid!= Utils.getUidLoggedIn()){
                    user.let {
                        usersList.add(it)
                    }
                }
                users.value=usersList
            }
        }
        return users
    }
}