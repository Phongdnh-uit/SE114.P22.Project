package com.example.mam.viewmodel.client

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mam.entity.User
import com.example.mam.services.APIservice
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class ProfileViewModel(
    private val api: APIservice
) : ViewModel() {
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()
    private val _isEditing = MutableStateFlow(false)
    val isEditing: StateFlow<Boolean> = _isEditing

    fun setEditing(value: Boolean) {
        _isEditing.value = value
    }

    fun setFullName(fullName: String) {
        _user.update { it?.copy(fullName = fullName) }
    }

    fun setPhoneNumber(phoneNumber: String){
        _user.update { it?.copy(phoneNumber = phoneNumber) }
    }
    fun setEmail(email: String){
        _user.update { it?.copy(email = email) }
    }
    init {
        fetchUser()
    }

    fun fetchUser() {
        viewModelScope.launch {
            val fetchedUser = api.getUser()
            _user.value = fetchedUser
        }
    }
    fun createImagePartFromUri(context: Context, uri: Uri): MultipartBody.Part {
        val contentResolver = context.contentResolver
        val inputStream = contentResolver.openInputStream(uri)!!
        val fileBytes = inputStream.readBytes()
        val requestBody = fileBytes.toRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("avatar", "avatar.jpg", requestBody)
    }
    fun uploadAvatar(context: Context, uri: Uri) {
        viewModelScope.launch {
            try {
                val avatarPart = createImagePartFromUri(context, uri)
                val updatedUser = api.uploadAvatar(avatarPart)
                _user.value = updatedUser
            } catch (e: Exception) {
            }
        }
    }
}