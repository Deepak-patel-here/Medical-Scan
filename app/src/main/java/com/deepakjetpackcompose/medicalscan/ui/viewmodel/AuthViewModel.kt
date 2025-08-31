package com.deepakjetpackcompose.medicalscan.ui.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deepakjetpackcompose.medicalscan.data.remote.repository.CloudinaryRepository
import com.deepakjetpackcompose.medicalscan.domain.util.USER
import com.deepakjetpackcompose.medicalscan.ui.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val cloud: CloudinaryRepository,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _imageUrl=MutableStateFlow<String>("")
    val imageUrl=_imageUrl.asStateFlow()

    private val _userinfo=MutableStateFlow<User>(User())
    val userinfo=_userinfo.asStateFlow()

    private val _firestoreLoader=MutableStateFlow<Boolean>(false)
    val firestoreLoader:StateFlow<Boolean> =_firestoreLoader.asStateFlow()

    init {
        checkuser()
    }

    fun checkuser() {
        if (auth.currentUser != null) {
            _authState.value = AuthState.Success
        } else {
            _authState.value = AuthState.Idle
        }
    }

    fun login(email: String, password: String) {
        _authState.value = AuthState.Loading
        try {
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    Log.d("auth", it.user.toString())
                    _authState.value = AuthState.Success
                }
                .addOnFailureListener {
                    _authState.value = AuthState.Error(it.message.toString())
                    Log.e("auth", it.localizedMessage)
                }
        } catch (e: Exception) {
            _authState.value = AuthState.Error(e.localizedMessage.toString())
            Log.e("auth", e.localizedMessage)

        }
    }

    fun signUp(email: String, password: String, name: String) {
        _authState.value = AuthState.Loading
        try {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    _authState.value = AuthState.Success
                    saveUserInfo(name = name, email = email)
                    getUserInfo()
                    Log.d("auth", it.user.toString())

                }
                .addOnFailureListener {
                    _authState.value = AuthState.Error(it.message.toString())
                    Log.e("auth", it.localizedMessage)

                }
        } catch (e: Exception) {
            _authState.value = AuthState.Error(e.localizedMessage.toString())
            Log.e("auth", e.localizedMessage)

        }
    }

    fun sendPasswordReset(email: String, onResult: (Boolean, String) -> Unit) {
        _loading.value = true
        val auth = FirebaseAuth.getInstance()
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(true, "Password reset link sent to $email")
                    _loading.value = false
                } else {
                    onResult(false, task.exception?.message ?: "Something went wrong")
                    _loading.value = false
                }
            }
    }


    fun logout() {
        auth.signOut()
        _authState.value = AuthState.Idle
    }

    fun uploadImage(context: Context, imageUri: Uri) {
        viewModelScope.launch {
            try {
                _imageUrl.value = cloud.uploadImage(context = context, imageUri = imageUri)
                Log.d("cloud", "successfully uploaded")
            } catch (e: Exception) {
                Log.e("cloud", e.localizedMessage ?: "unknown error", e)
            }
        }
    }

    fun saveUserInfo(
        context: Context? = null,
        name: String? = null,
        email: String? = null,
        phone: String? = null,
        location: String? = null,
        birthday: String? = null,
        imageUri: Uri? = null, // pass actual Uri instead of string
        update: Boolean = false
    ) {
        _firestoreLoader.value = true
        val uid = auth.currentUser?.uid
        if (uid == null) {
            Log.d("user", "uid is empty")
            _firestoreLoader.value = false
            return
        }

        if (!update) {
            // Save first-time user directly
            val user = User(
                name = name,
                email = email,
                phone = phone,
                location = location,
                birthday = birthday,
                imageUrl = null
            )
            firestore.collection(USER).document(uid).set(user)
                .addOnSuccessListener {
                    Log.d("user", "successfully saved")
                    _firestoreLoader.value = false
                }
                .addOnFailureListener {
                    Log.e("user", it.localizedMessage ?: "unknown error", it)
                    _firestoreLoader.value = false
                }

        } else {
            // First upload the image, then update Firestore
            if (context != null && imageUri != null) {
                viewModelScope.launch {
                    try {
                        val uploadedUrl = cloud.uploadImage(context, imageUri) // ✅ wait for Cloudinary URL
                        val user = User(
                            name = name,
                            email = email,
                            phone = phone,
                            location = location,
                            birthday = birthday,
                            imageUrl = uploadedUrl
                        )

                        firestore.collection(USER).document(uid).update(
                            mapOf(
                                "name" to user.name,
                                "email" to user.email,
                                "phone" to user.phone,
                                "location" to user.location,
                                "birthday" to user.birthday,
                                "imageUrl" to uploadedUrl // ✅ Cloudinary URL
                            )
                        )
                            .addOnSuccessListener {
                                Log.d("user", "successfully updated")
                                _userinfo.value=user
                                _firestoreLoader.value = false
                            }
                            .addOnFailureListener {
                                Log.e("user", it.localizedMessage ?: "unknown error", it)
                                _firestoreLoader.value = false
                            }
                    } catch (e: Exception) {
                        Log.e("cloud", e.localizedMessage ?: "unknown error", e)
                        _firestoreLoader.value = false
                    }
                }
            } else {
                Log.e("user", "No image provided for update")
                _firestoreLoader.value = false
            }
        }
    }


    fun getUserInfo(){
        _firestoreLoader.value=true
        val uid=auth.currentUser?.uid
        if(uid==null){
            Log.d("user","uid is empty")
            _firestoreLoader.value=true
            return
        }
        firestore.collection(USER).document(uid).get()
            .addOnSuccessListener { document->
                if(document.exists()){
                    val user=document.toObject(User::class.java)
                    _userinfo.value=user!!
                    Log.d("user","successfully fetched")
                }else{
                    Log.d("user","user document doesn't exist")
                }
                _firestoreLoader.value=false
            }
            .addOnFailureListener {
                Log.e("user",it.localizedMessage?:"unknown error",it)
                _firestoreLoader.value=false

            }

    }


}

sealed class AuthState {
    object Loading : AuthState()
    object Idle : AuthState()
    object Success : AuthState()
    data class Error(val msg: String) : AuthState()
}