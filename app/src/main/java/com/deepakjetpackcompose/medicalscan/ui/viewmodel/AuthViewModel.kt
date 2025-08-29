package com.deepakjetpackcompose.medicalscan.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val auth: FirebaseAuth) : ViewModel() {

    private val _authState= MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _loading =MutableStateFlow(false)
    val loading= _loading.asStateFlow()
    init {
        checkuser()
    }

    fun checkuser(){
        if(auth.currentUser!=null){
            _authState.value= AuthState.Success
        }else{
            _authState.value= AuthState.Idle
        }
    }
    fun login(email: String, password: String) {
        _authState.value = AuthState.Loading
        try {
            auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    Log.d("auth",it.user.toString())
                    _authState.value= AuthState.Success
                }
                .addOnFailureListener {
                    _authState.value= AuthState.Error(it.message.toString())
                    Log.e("auth",it.localizedMessage)
                }
        }catch (e: Exception){
            _authState.value= AuthState.Error(e.localizedMessage.toString())
            Log.e("auth",e.localizedMessage)

        }
    }

    fun signUp(email: String, password: String, name: String){
        _authState.value= AuthState.Loading
        try {
            auth.createUserWithEmailAndPassword(email,password)
                .addOnSuccessListener {
                    _authState.value= AuthState.Success
                    Log.d("auth",it.user.toString())

                }
                .addOnFailureListener {
                    _authState.value= AuthState.Error(it.message.toString())
                    Log.e("auth",it.localizedMessage)

                }
        }catch (e: Exception){
            _authState.value=AuthState.Error(e.localizedMessage.toString())
            Log.e("auth",e.localizedMessage)

        }
    }

    fun sendPasswordReset(email: String, onResult: (Boolean, String) -> Unit) {
        _loading.value=true
        val auth = FirebaseAuth.getInstance()
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onResult(true, "Password reset link sent to $email")
                    _loading.value=false
                } else {
                    onResult(false, task.exception?.message ?: "Something went wrong")
                    _loading.value=false
                }
            }
    }
}

sealed class AuthState{
    object Loading: AuthState()
    object Idle: AuthState()
    object Success: AuthState()
    data class Error(val msg:String): AuthState()
}