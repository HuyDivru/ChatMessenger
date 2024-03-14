@file:Suppress("DEPRECATION")
package com.example.chatmessager.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.chatmessager.R
import com.example.chatmessager.Utils
import com.example.chatmessager.databinding.FragmentSettingBinding
import com.example.chatmessager.mvvm.ChatAppViewModel
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream
import java.util.UUID


class SettingFragment : Fragment() {

    lateinit var viewModel:ChatAppViewModel
    lateinit var binding:FragmentSettingBinding
    lateinit var storageRef:StorageReference
    lateinit var storage:FirebaseStorage
    var uri: Uri?=null
    lateinit var bitmap:Bitmap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
      binding= DataBindingUtil.inflate(inflater,R.layout.fragment_setting, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel=ViewModelProvider(this).get(ChatAppViewModel::class.java)
        binding.lifecycleOwner=viewLifecycleOwner
        binding.viewModel=viewModel

        storage=FirebaseStorage.getInstance()
        storageRef=storage.reference

        viewModel.imageUrl.observe(viewLifecycleOwner, Observer {
            loadImage(it)
        })

        binding.settingBackBtn.setOnClickListener {
            view.findNavController().navigate(R.id.action_settingFragment_to_homeFragment)
        }

        binding.settingUpdateButton.setOnClickListener {
            viewModel.updateProfile()
        }

        binding.settingUpdateImage.setOnClickListener {
            val options= arrayOf<CharSequence>("Take Photo","Choose From Gallery","Cancel")
            val builder=AlertDialog.Builder(requireContext())
            builder.setTitle("Choose your profile picture")
            builder.setItems(options){
                dialog, item ->
                when{
                    options[item]=="Take Photo" -> {
                        takePhotoWithCamera()
                    }
                    options[item]=="Choose From Gallery" ->{
                        pickImageFromGallery()
                    }
                    options[item]=="Cancel" ->dialog.cancel()
                }
            }
            builder.show()
        }
    }
    @SuppressLint("QueryPermissionsNeeded")
    private fun pickImageFromGallery() {
        val pickPictureIntent=Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        if(pickPictureIntent.resolveActivity(requireActivity().packageManager)!=null){
            startActivityForResult(pickPictureIntent, Utils.REQUEST_IMAGE_PICK)
        }
    }
    // to take a photo with the camera
    @SuppressLint("QueryPermissionsNeeded")
    private fun takePhotoWithCamera() {
        val takePictureIntent=Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(takePictureIntent,Utils.REQUEST_IMAGE_CAPTURE)
    }

    private fun loadImage(it: String?) {
        Glide.with(requireContext()).load(it).placeholder(R.drawable.person)
            .into(binding.settingUpdateImage)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode==AppCompatActivity.RESULT_OK){
            when(requestCode){
              Utils.REQUEST_IMAGE_CAPTURE ->{
                  val imageBitmap=data?.extras?.get("data") as Bitmap
                  uploadImageToFirebaseStorage(imageBitmap)
              }
                Utils.REQUEST_IMAGE_PICK ->{
                    val imageUri= data?.data
                    val imageBitmap= MediaStore.Images.Media.getBitmap(context?.contentResolver, imageUri)
                    uploadImageToFirebaseStorage(imageBitmap)
                }
            }
        }
    }

    private fun uploadImageToFirebaseStorage(imageBitmap: Bitmap) {
        val bos=ByteArrayOutputStream()

        imageBitmap?.compress(Bitmap.CompressFormat.JPEG,100,bos)
        val data=bos.toByteArray()
        bitmap=imageBitmap!!

        binding.settingUpdateImage.setImageBitmap(imageBitmap)
        val storagePath=storageRef.child("Photos/${UUID.randomUUID()}.jpg")
        val uploadTask=storagePath.putBytes(data)

        uploadTask.addOnSuccessListener {
            val task=it.metadata?.reference?.downloadUrl

            task?.addOnSuccessListener {
                uri=it
                viewModel.imageUrl.value=uri.toString()
            }
            Toast.makeText(context,"Image uploaded succefully!",Toast.LENGTH_SHORT).show()
        }.addOnFailureListener{
            Toast.makeText(context,"Failed to upload image! ", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onResume() {
        super.onResume()

        viewModel.imageUrl.observe(viewLifecycleOwner, Observer {
            loadImage(it)
        })
    }
}