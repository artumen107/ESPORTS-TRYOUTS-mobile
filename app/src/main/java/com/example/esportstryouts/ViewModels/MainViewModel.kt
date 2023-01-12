package com.example.esportstryouts.ViewModels

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.esportstryouts.Model.*
import com.google.firebase.auth.FirebaseAuth

class MainViewModel : ViewModel() {

    val storageRepository = StorageRepository()

    fun uploadAvatar(data: Uri) {
        storageRepository.uploadAvatar(data)
    }

}
