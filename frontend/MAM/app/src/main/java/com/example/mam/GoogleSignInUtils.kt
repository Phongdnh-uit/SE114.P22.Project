package com.example.mam

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.credentials.CredentialManager
import androidx.credentials.CredentialOption
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Firebase
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class GoogleSignInUtils {

    companion object {
        fun getGoogleIdToken(
            context: Context,
            scope: CoroutineScope,
            launcher: ManagedActivityResultLauncher<Intent, ActivityResult>?,
            handle: (String) -> Unit = {},
            timeout: () -> Unit = { /* No-op */ }
        ){
            val credentialManager = CredentialManager.create(context)
            val request = GetCredentialRequest.Builder()
                .addCredentialOption(getCredentialOptions(context))
                .build()
            scope.launch {
                try {
                    val result = credentialManager.getCredential(context,request)
                    when(result.credential){
                        is CustomCredential ->{
                            if(result.credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL){
                                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(result.credential.data)
                                val googleTokenId = googleIdTokenCredential.idToken
                                val authCredential = GoogleAuthProvider.getCredential(googleTokenId,null)
                                val user = Firebase.auth.signInWithCredential(authCredential).await().user
                                user?.let {
                                    if(it.isAnonymous.not()){
                                        Log.d("GoogleSignIn", "User signed in: ${it.email}")
                                        Log.d("GoogleSignIn", "User ID: ${it.uid}")
                                        Log.d("GoogleSignIn", "User Name: ${it.displayName}")
                                        Log.d("GoogleSignIn", "User Photo URL: ${it.photoUrl}")
                                        Log.d("GoogleSignIn", "User ID Token: ${it.getIdToken(true).await().token}")
                                        val idToken = it.getIdToken(true).await().token ?: ""
                                        handle.invoke(idToken)
                                    }
                                }
                            }
                        }
                        else ->{
                        }
                    }
                }catch (e: NoCredentialException){
                    //launcher?.launch(getIntent())
                }catch (e:GetCredentialException){
                    e.printStackTrace()
                }
                finally {
                    timeout.invoke()
                }
            }
        }

//        suspend fun getGoogleIdToken(
//            context: Context,
//            launcher: ManagedActivityResultLauncher<Intent, ActivityResult>?
//        ): String = suspendCoroutine { continuation ->
//            val credentialManager = CredentialManager.create(context)
//            val request = GetCredentialRequest.Builder()
//                .addCredentialOption(getCredentialOptions(context))
//                .build()
//
//            CoroutineScope(Dispatchers.Main).launch {
//                try {
//                    val result = credentialManager.getCredential(context, request)
//                    when (val credential = result.credential) {
//                        is CustomCredential -> {
//                            if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
//                                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
//                                val googleTokenId = googleIdTokenCredential.idToken
//                                val authCredential = GoogleAuthProvider.getCredential(googleTokenId, null)
//                                val user = Firebase.auth.signInWithCredential(authCredential).await().user
//
//                                val idToken = user?.getIdToken(true)?.await()?.token
//                                continuation.resume(idToken ?: "")
//                            } else {
//                                continuation.resume("")
//                            }
//                        }
//
//                        else -> {
//                            continuation.resume("")
//                        }
//                    }
//                } catch (e: NoCredentialException) {
//                    launcher?.launch(getIntent())
//                    continuation.resume("")
//                } catch (e: GetCredentialException) {
//                    e.printStackTrace()
//                    continuation.resume("")
//                }
//            }
//        }


        private fun getIntent(): Intent {
            return Intent(Settings.ACTION_ADD_ACCOUNT).apply {
                putExtra(Settings.EXTRA_ACCOUNT_TYPES, arrayOf("com.google"))
            }
        }

        private fun getCredentialOptions(context: Context):CredentialOption{
            return GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setAutoSelectEnabled(false)
                .setServerClientId(context.getString(R.string.Web_Client_ID))
                .build()
        }
    }
}