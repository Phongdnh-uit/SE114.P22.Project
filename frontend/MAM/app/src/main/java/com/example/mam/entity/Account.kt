package com.example.mam.entity

data class Account(
    private var currentUser: User? = null
){
    fun setUser(user: User) {
        currentUser = user
    }

    fun getUser(): User? = currentUser

    fun clearUser() {
        currentUser = null
    }
}
