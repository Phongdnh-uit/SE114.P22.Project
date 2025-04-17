package com.example.mam.services.repo

import android.util.Log

class FakeAuthorizationRepo : AuthorizationRepo() {
    override suspend fun SignIn() {
        Log.d("PREVIEW", "Fake sign-in executed")
    }
}