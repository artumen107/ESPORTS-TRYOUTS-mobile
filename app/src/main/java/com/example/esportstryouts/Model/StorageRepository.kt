package com.example.esportstryouts.Model

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.annotation.MainThread
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.esportstryouts.MainActivity
import com.example.esportstryouts.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.net.URI

class StorageRepository {

    val firebaseAuth = FirebaseAuth.getInstance()
    val currentEmailClear = firebaseAuth.currentUser?.email.toString().replace("@","").
    replace(".", "").trim()
    val storageDatabase = FirebaseStorage.getInstance()
    val usersAvatars = storageDatabase.getReference("Users/$currentEmailClear/Avatar.jpg")
    lateinit var imageUri: Uri

    fun uploadAvatar(data: Uri) = CoroutineScope(Dispatchers.IO).launch{
    usersAvatars.putFile(data)
    }



}