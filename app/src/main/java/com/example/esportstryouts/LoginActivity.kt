package com.example.esportstryouts

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.esportstryouts.MainActivity
import com.example.esportstryouts.databinding.LoginActivityBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    //viewBinding
   private lateinit var binding: LoginActivityBinding

    //ActionBar
    private lateinit var actionBar: ActionBar

    //ProgressDialog
    private lateinit var progressDialog: ProgressDialog

    //FirebaseAuth

    private lateinit var firebaseAuth: FirebaseAuth
    private var email = ""
    private var password = ""
    private var username = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
     binding = LoginActivityBinding.inflate(layoutInflater)
      setContentView(binding.root)

        //configure actionbar
        actionBar = supportActionBar!!
        actionBar.title = "Login"

        //configure progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Logging in...")
        progressDialog.setCanceledOnTouchOutside(false)

        //init firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        //handle click, open register activity
        binding.noAccountTv.setOnClickListener{
            startActivity(Intent(this, SignUpActivity::class.java))
        }
        //handle click, begin login
        binding.loginBtn.setOnClickListener{
            //before logging in, validate data
            validateData()
        }

    }

    private fun validateData() {
       // get data
        email = binding.emailEt.text.toString().trim()
        password = binding.passwordEt.text.toString().trim()

        //validate data
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            //invalid email format
            binding.emailEt.setError("Invalid email format")
        }
        else if (TextUtils.isEmpty(password)){
            //no password entered
    binding.passwordEt.error = "Please enter password"
        }
        else {
            firebaseLogin()
        }
    }

    private fun firebaseLogin() {
        //show progress
        progressDialog.show()
        firebaseAuth.signInWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                //login success
                progressDialog.dismiss()
                //get user info
                val firebaseUser = firebaseAuth.currentUser
                val email = firebaseUser!!.email
                Toast.makeText(this, "Logged in as: $email", Toast.LENGTH_SHORT).show()

                //open main activity with profile
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            .addOnFailureListener { e->
                //login failed
                progressDialog.dismiss()
                Toast.makeText(this, "Login failed due to ${e.message}", Toast.LENGTH_SHORT).show()

            }

    }

    private fun checkUser() {
        //if user is already logged in go to profile/main activity
        //get current user

        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null) {
            //user is logged in

            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

    }

}