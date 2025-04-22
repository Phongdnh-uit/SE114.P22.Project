package com.example.mam.entity

import com.example.mam.entity.authorization.request.SignInRequest
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class User @Inject constructor() {
    private var currentUser: SignInRequest? = null

    fun setUser(user: SignInRequest) {
        currentUser = user
    }

    fun getUser(): SignInRequest? = currentUser
}