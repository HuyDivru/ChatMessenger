package com.example.chatmessager.activities

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import com.example.chatmessager.R
import com.example.chatmessager.databinding.ActivitySignUpBinding

class SignInActivity : AppCompatActivity() {
    lateinit var binding:ActivitySignUpBinding
    lateinit var pd:ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
    }
}