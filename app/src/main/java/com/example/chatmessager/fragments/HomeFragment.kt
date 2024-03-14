@file:Suppress("DEPRECATION")
package com.example.chatmessager.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatmessager.R
import com.example.chatmessager.activities.SignInActivity
import com.example.chatmessager.adpater.OnItemClickListener
import com.example.chatmessager.adpater.RecentChatAdapter
import com.example.chatmessager.adpater.UserAdapter
import com.example.chatmessager.adpater.onChatClicked
import com.example.chatmessager.databinding.FragmentHomeBinding
import com.example.chatmessager.model.RecentChats
import com.example.chatmessager.model.Users
import com.example.chatmessager.mvvm.ChatAppViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import de.hdodenhof.circleimageview.CircleImageView

class HomeFragment : Fragment(), OnItemClickListener, onChatClicked {

    lateinit var rvUsers : RecyclerView
    lateinit var rvRecentChats : RecyclerView
    lateinit var adapter : UserAdapter
    lateinit var viewModel : ChatAppViewModel
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var circleImageView: CircleImageView
    lateinit var recentadapter : RecentChatAdapter
    lateinit var firestore : FirebaseFirestore
    lateinit var binding: FragmentHomeBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel = ViewModelProvider(this).get(ChatAppViewModel::class.java)
        toolbar = view.findViewById(R.id.toolbarMain)
        val logoutimage = toolbar.findViewById<ImageView>(R.id.logOut)
        circleImageView = toolbar.findViewById(R.id.account_Image)



        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.imageUrl.observe(viewLifecycleOwner, Observer {


            Glide.with(requireContext()).load(it).into(circleImageView)
        })


        firestore = FirebaseFirestore.getInstance()


        val firebaseAuth = FirebaseAuth.getInstance()

        logoutimage.setOnClickListener {


            firebaseAuth.signOut()

            startActivity(Intent(requireContext(), SignInActivity::class.java))


        }
        rvUsers = view.findViewById(R.id.rvUsers)
        rvRecentChats = view.findViewById(R.id.rvRecentChats)
        adapter = UserAdapter()
        recentadapter = RecentChatAdapter()


        val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        val layoutManager2 = LinearLayoutManager(activity)

        rvUsers.layoutManager = layoutManager
        rvRecentChats.layoutManager= layoutManager2

        viewModel.getUsers().observe(viewLifecycleOwner, Observer {

            adapter.setList(it)
            rvUsers.adapter = adapter


        })


        circleImageView.setOnClickListener {


            view.findNavController().navigate(R.id.action_homeFragment_to_settingFragment)


        }


        adapter.setOnClickListener(this)

        viewModel.getRecentUsers().observe(viewLifecycleOwner, Observer {


            recentadapter.setList(it)
            rvRecentChats.adapter = recentadapter

        })

        recentadapter.setOnChatClickListener(this)

    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }
    override fun onUserSelected(position: Int, users: Users) {

        val action = HomeFragmentDirections.actionHomeFragmentToChatFragment(users)
        view?.findNavController()?.navigate(action)
    }
    override fun getOnChatCLickedItem(position: Int, chatList: RecentChats) {

        val action = HomeFragmentDirections.actionHomeFragmentToChatfromHome(chatList)
        view?.findNavController()?.navigate(action)

    }

}