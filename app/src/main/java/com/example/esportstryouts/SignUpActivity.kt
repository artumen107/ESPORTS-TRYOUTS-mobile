package com.example.esportstryouts

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.esportstryouts.Model.DataBaseRepository
import com.example.esportstryouts.Model.DefaultGamesResults
import com.example.esportstryouts.Model.NewUserDBRow
import com.example.esportstryouts.databinding.SignupActivityBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest

class SignUpActivity : AppCompatActivity() {

    //ViewBinding
private lateinit var binding: SignupActivityBinding

    //ActionBar
    private lateinit var actionBar: ActionBar

    //ProgressDialog
    private lateinit var progressDialog: ProgressDialog

    //FirebaseAuth
    private lateinit var firebaseAuth: FirebaseAuth
    private var email = ""
    private var username = ""
    private var password = ""
    private var confirmedPassword = ""

    //Database
    val repository = DataBaseRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SignupActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Configure ActionBar
        actionBar = supportActionBar!!
        actionBar.title = "Sign Up"
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)

        //Configure progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Signing in...")
        progressDialog.setCanceledOnTouchOutside(false)

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()

        //handle click, begin signup
        binding.signUpBtn.setOnClickListener {
            //validate data
          validateData()
      }
    }

    private fun validateData() {
        //get data

        email = binding.signupEmailEt.text.toString().trim()
        username = binding.signupUsernameEt.text.toString().trim()
        password = binding.signupPwEt.text.toString().trim()
        confirmedPassword = binding.signupPwConfEt.text.toString().trim()


        //validate data
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            //invalid email format
            binding.signupEmailEt.error = "Invalid email format"
        }
        else if (username.length>10)
        {
            //username must be max 10 signs
            binding.signupUsernameEt.error = "Username must be max 10 signs"
        }
        else if ((TextUtils.isEmpty(password)) || (TextUtils.isEmpty(confirmedPassword))) {
            //password isn't entered
            binding.signupPwEt.error = "Please enter and confirm password"
        }

        else if (password.length < 6) {
            //password length is less than 6
            binding.signupPwEt.error = "Password must be at least 6 characters long"
        }
        else if (confirmedPassword != password) {
            //passwords are not the same
            binding.signupPwConfEt.error = "Password must be the same!"
        }

        else {
            firebaseSignUp()
        }

    }

    private fun firebaseSignUp() {
        //show progress
        progressDialog.show()

        //create account
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener{

                //user.updateProfile()

                //signup success
                progressDialog.dismiss()

                //get current user
                val firebaseUser = firebaseAuth.currentUser
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(username)
                    .build()
                firebaseUser?.updateProfile(profileUpdates)
                println("Firebase display name: " + firebaseUser?.displayName)
                val email = firebaseUser?.email
                Toast.makeText(this, "Account created with email $email", Toast.LENGTH_SHORT).show()

                //send to databse

                if (email != null) {
                    val emailClear = email.replace("@", "").replace(".", "")
                    val newUser = NewUserDBRow(email, username)
                    val newDefaultResult = DefaultGamesResults(9999, 9999, "00.00.00", "00:00", 0)

                    repository.databaseReferenceUsers.child(emailClear).setValue(newUser)
                    repository.databaseReferenceUsers.child(emailClear).child("Best_Results").child("Delay_Ability").setValue(newDefaultResult)
                    repository.databaseReferenceUsers.child(emailClear).child("Best_Results").child("Reaction_Time").setValue(newDefaultResult)
                    repository.databaseReferenceUsers.child(emailClear).child("Best_Results").child("Switch_Cost").setValue(newDefaultResult)
                }


                //val hashCode = repository.databaseReferenceUsers.push().key
                // Jeśli użytkownik ma wyniki w jakiejś grze, to kopiuje te wyniki z bazy danych (z Users -> Mail Frienda -> Best Results) i zapisuje je do wyników Frienda
                // czyli do Users -> Current User Email Clear -> Friends -> Mail Frienda -> Gra -> I tam tworzy setValue(BestResultDBRow(blabla, blabla, blabla...)
                // Jeśli użytkownik nie ma jeszcze wyników, tworzy wyniki defaultowe


                //open profile/main activity
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("username", username)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener{ e->
                //signup failed
                progressDialog.dismiss()
                Toast.makeText(this, "SignUp Failed due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}