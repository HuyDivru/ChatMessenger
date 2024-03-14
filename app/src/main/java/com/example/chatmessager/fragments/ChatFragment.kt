package com.example.chatmessager.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Message
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.chatmessager.R
import com.example.chatmessager.Utils
import com.example.chatmessager.adpater.MessageAdapter
import com.example.chatmessager.databinding.FragmentChatBinding
import com.example.chatmessager.model.Messages
import com.example.chatmessager.mvvm.ChatAppViewModel
import de.hdodenhof.circleimageview.CircleImageView


class ChatFragment : Fragment() {

    lateinit var args:ChatFragmentArgs
    lateinit var binding: FragmentChatBinding
    lateinit var viewModel:ChatAppViewModel
    lateinit var adapter: MessageAdapter
    lateinit var toolbar: Toolbar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_chat, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar=view.findViewById(R.id.toolBarChat)

        val circleImageView=toolbar.findViewById<CircleImageView>(R.id.chatImageViewUser)
        val textViewName=toolbar.findViewById<TextView>(R.id.chatUserName)
        val textViewStatus= view.findViewById<TextView>(R.id.chatUserStatus)
        val chatBackBtn=toolbar.findViewById<ImageView>(R.id.chatBackBtn)

        viewModel=ViewModelProvider(this).get(ChatAppViewModel::class.java)

        args=ChatFragmentArgs.fromBundle(requireArguments())
        binding.viewModel=viewModel
        binding.lifecycleOwner=viewLifecycleOwner

        Glide.with(view.context).load(args.users.imageUrl!!).placeholder(R.drawable.person).dontAnimate().into(circleImageView)
        textViewName.setText(args.users.username)
        textViewStatus.setText(args.users.status)

        chatBackBtn.setOnClickListener {
            view.findNavController().navigate(R.id.action_chatFragment_to_homeFragment)
        }

        binding.sendBtn.setOnClickListener {
            viewModel.sendMessage(Utils.getUidLoggedIn(),args.users.userid!!,args.users.username!!,args.users.imageUrl!!)
        }
        viewModel.getMessages(args.users.userid!!).observe(viewLifecycleOwner, Observer { 
            initRecylerView(it)
        })
    }

    private fun initRecylerView(list: List<Messages>) {
        adapter= MessageAdapter()
        val layoutManager=LinearLayoutManager(context)
        binding.messagesRecylerView.layoutManager=layoutManager
        layoutManager.stackFromEnd=true
        adapter.setList(list)
        adapter.notifyDataSetChanged()
        binding.messagesRecylerView.adapter=adapter
    }
}