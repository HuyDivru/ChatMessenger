package com.example.chatmessager.activities

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.chatmessager.MainActivity
import com.example.chatmessager.R
import com.example.chatmessager.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException

class SignInActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignInBinding
    lateinit var pd:ProgressDialog
    private lateinit var  auth:FirebaseAuth
    private lateinit var email:String
    private lateinit var password:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_sign_in)

        auth= FirebaseAuth.getInstance()

        if(auth.currentUser!=null){
            startActivity(Intent(this,MainActivity::class.java))
        }

        pd= ProgressDialog(this)

        binding.signInTextToSignUp.setOnClickListener {
            startActivity(Intent(this,SignUpActivity::class.java))
        }
        binding.loginBtn.setOnClickListener {
            email=binding.loginEmail.text.toString()
            password=binding.loginPassword.text.toString()

            if(email.isEmpty()){
                Toast.makeText(this,"Enter Email",Toast.LENGTH_SHORT).show()
            }
            if(password.isEmpty()){
                Toast.makeText(this,"Enter Password",Toast.LENGTH_SHORT).show()
            }
            if(email.isNotEmpty()&&password.isNotEmpty()){
                signIn(email,password)
            }
        }
    }

    private fun signIn(email: String, password: String) {
        pd.show()
        pd.setMessage("Signing In")

        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
            if(it.isSuccessful){
                pd.dismiss()
                startActivity(Intent(this,MainActivity::class.java))
            }
            else{
                pd.dismiss()
                Toast.makeText(applicationContext,"Invalid Credentials",Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            exception ->
            when(exception){
                is FirebaseAuthInvalidCredentialsException ->{
                    Toast.makeText(applicationContext,"Invalid Credentials",Toast.LENGTH_SHORT).show()
                }
                else ->{
                    Toast.makeText(applicationContext,"Auth Failed",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        pd.dismiss()
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        pd.dismiss()
    }
}