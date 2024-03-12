package com.example.chatmessager.activities

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.chatmessager.R
import com.example.chatmessager.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding:ActivitySignUpBinding
    private lateinit var pd:ProgressDialog
    private lateinit var auth:FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var name:String
    private lateinit var email:String
    private lateinit var password:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_sign_up)

        auth= FirebaseAuth.getInstance()
        firestore= FirebaseFirestore.getInstance()

        pd= ProgressDialog(this)

        binding.signUpTextToSignIn.setOnClickListener {
            startActivity(Intent(this,SignInActivity::class.java))
        }
        binding.signUpBtn.setOnClickListener {
            name=binding.signUpName.text.toString()
            email=binding.signUpEmail.text.toString()
            password=binding.signUpPassword.text.toString()

            if(binding.signUpName.text.isEmpty()){
                Toast.makeText(this,"Enter Name",Toast.LENGTH_SHORT).show()
            }
            if(binding.signUpEmail.text.isEmpty()){
                Toast.makeText(this,"Enter Email",Toast.LENGTH_SHORT).show()
            }
            if(binding.signUpPassword.text.isEmpty()){
                Toast.makeText(this,"Enter Password",Toast.LENGTH_SHORT).show()
            }
            if(binding.signUpName.text.isNotEmpty()&& binding.signUpEmail.text.isNotEmpty()&& binding.signUpPassword.text.isNotEmpty()){
                createAnAccount(name,password,email)
            }
        }
    }

    private fun createAnAccount(name: String, password: String, email: String) {
        pd.show()
        pd.setMessage("Registering User")

        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
            task-> if(task.isSuccessful){
                val user =auth.currentUser
                val dataHashMap= hashMapOf("userid" to user!!.uid!!,"username" to name,"useremail" to email,"status" to "default","image" to "https://cdn-icons-png.flaticon.com/512/3135/3135715.png")
                firestore.collection("Users").document(user.uid).set(dataHashMap)
                pd.dismiss()
                startActivity(Intent(this,SignInActivity::class.java))
            }
        }
    }
}