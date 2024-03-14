@file:Suppress("DEPRECATION")
package com.example.chatmessager.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatmessager.R
import com.example.chatmessager.adpater.OnItemClickListener
import com.example.chatmessager.adpater.UserAdapter
import com.example.chatmessager.adpater.onChatClicked
import com.example.chatmessager.databinding.ActivitySignUpBinding
import com.example.chatmessager.databinding.FragmentChatFromBinding
import com.example.chatmessager.databinding.FragmentHomeBinding
import com.example.chatmessager.model.RecentChats
import com.example.chatmessager.model.Users
import com.example.chatmessager.mvvm.ChatAppViewModel
import com.google.firebase.firestore.FirebaseFirestore
import de.hdodenhof.circleimageview.CircleImageView


class ChatFromHome : Fragment(){
    lateinit var args:ChatFromHomeArgs
    lateinit var binding:FragmentChatFromBinding
    lateinit var viewModel: ChatAppViewModel
    lateinit var toolbar: Toolbar
    lateinit var adapter: UserAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_chat_from, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar=view.findViewById(R.id.toolBarChat)
        val circleImageView=toolbar.findViewById<CircleImageView>(R.id.chatImageViewUser)
        val textView=toolbar.findViewById<TextView>(R.id.chatUserName)

        args=ChatFromHomeArgs.fromBundle(requireArguments())
        viewModel=ViewModelProvider(this).get(ChatAppViewModel::class.java)

        binding.viewModel=viewModel
        binding.lifecycleOwner=viewLifecycleOwner

//        Glide.with(view.context).load(args.recentchats.friendsimage!!).placeholder(R.drawable.person).dontAnimate().into(circleImageView);
    }

}