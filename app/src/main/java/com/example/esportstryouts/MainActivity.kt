package com.example.esportstryouts

import android.app.Activity
import android.content.Intent

import android.graphics.Bitmap
import android.graphics.BitmapFactory

import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle

import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

import com.example.esportstryouts.ViewModels.MainViewModel
import com.example.esportstryouts.ViewModels.ResultsViewModel
import com.example.esportstryouts.databinding.MainActivityBinding
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext



class MainActivity : AppCompatActivity() {

    //ViewBinding
  lateinit var binding: MainActivityBinding

    //ActionBar
    private lateinit var actionBar: ActionBar

    //FirebaseAuth
    private lateinit var firebaseAuth: FirebaseAuth

    //FirebaseRepo
    lateinit var storageDatabase : FirebaseStorage
    lateinit var usersAvatarsRef : StorageReference



    //Results viewmodel
    lateinit var viewModelResults: ResultsViewModel

    //Main viewmodel
    lateinit var viewModel: MainViewModel
    private  lateinit var selectedImg : Uri
    private lateinit var downloadedImg : Bitmap
    private lateinit var avatarImage : ShapeableImageView






    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)


        setContentView(R.layout.main_activity)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setContentView(binding.root)

        //Configure ActionBar
        actionBar = supportActionBar!!
        actionBar.title = "Profile"

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        viewModelResults = ResultsViewModel()
        viewModel = MainViewModel()
        //click, logout

        //downloading avatar
       storageDatabase = FirebaseStorage.getInstance()
       usersAvatarsRef = storageDatabase.getReference("Users/${
           firebaseAuth.currentUser?.email.toString().
       replace("@","").replace(".","")}/Avatar.jpg")

            downloadAvatar()

      binding.logoutBtn.setOnClickListener {
           firebaseAuth.signOut()
          checkUser()
      }

        //click, settings
      binding.settingsBtn.setOnClickListener {

          val inflater = layoutInflater

          val inflateView = inflater.inflate(R.layout.settings_fragment, null)

          val email = inflateView.findViewById<TextView>(R.id.settingsEmail)
          val id = inflateView.findViewById<TextView>(R.id.settingsId)
          avatarImage = inflateView.findViewById(R.id.settingsAvatarImg)

          val selectBtn = inflateView.findViewById<Button>(R.id.settingsSelectImage)
          email.text = firebaseAuth.currentUser?.email
          id.text = firebaseAuth.currentUser?.displayName

          selectBtn.setOnClickListener {

              pickImageGallery()
          }



          val builder = AlertDialog.Builder(this)
          //bitmapping main activity avatar
          val mainActivityAvatarImg = binding.mainActivityAvatarImg
          mainActivityAvatarImg.invalidate()
          val mainActivityAvatarImgDrawable : Drawable = mainActivityAvatarImg.drawable
           avatarImage.setImageDrawable(mainActivityAvatarImgDrawable)
          Log.i("DOWNLOADED AVATAR: ", downloadedImg.toString())
          builder.setPositiveButton("yes") { dialog, which ->
              viewModel.uploadAvatar(selectedImg)

              binding.mainActivityAvatarImg.setImageURI(selectedImg)
              Toast.makeText(applicationContext,
                  "yes",
                  Toast.LENGTH_SHORT).show()
          }

          builder.setNegativeButton("no") { dialog, which ->
              Toast.makeText(applicationContext,
                  "no", Toast.LENGTH_SHORT).show()
          }


          builder.setView(inflateView)
          builder.show()
      }

        viewModelResults.getResponseUsingLiveData()
    }

    private fun downloadAvatar() = CoroutineScope(Dispatchers.Main).launch {

            usersAvatarsRef.downloadUrl.addOnSuccessListener {
                CoroutineScope(Dispatchers.Main).launch {
                    val bytes = usersAvatarsRef.getBytes(4000000L).await()
                    val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    withContext(Dispatchers.Main) {
                        downloadedImg = bmp
                        Log.i("DOWNLOADED IMAGE: ", downloadedImg.toString())
                        binding.mainActivityAvatarImg.setImageBitmap(downloadedImg)
                    }
                }
            }.addOnFailureListener {
                downloadedImg = Bitmap.createBitmap(100,100,Bitmap.Config.ARGB_8888);
                //"Bitmap@17eeb"
            }

    }

    val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data: Intent? = result.data
            if (data != null) {
                if (data.data != null) {
                    selectedImg = data.data!!
                    avatarImage.setImageURI(selectedImg)
                }
            }
        }
    }
    private fun pickImageGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        //intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
       resultLauncher.launch(intent)
    }



    private fun checkUser() {
       // check user is logged in or not
        val firebaseUser = firebaseAuth.currentUser
        val username = intent.extras?.getString("username")

        if (firebaseUser != null){
            //user not null, user is logged in, get user info
            val email = firebaseUser.email

            //set to text view
            //binding.emailTv.text = email
            if (username != null) {
                binding.idTv.text = username
            }
            else {
                binding.idTv.text = firebaseUser.displayName
            }

        }
        else {
            //user is null, user is not logged in
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}