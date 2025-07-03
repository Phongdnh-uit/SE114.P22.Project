package com.example.mam.repository.retrofit

import android.util.Log
import com.example.mam.data.UserPreferencesRepository
import com.example.mam.dto.authentication.AuthResponse
import com.example.mam.dto.authentication.RefreshTokenRequest
import com.example.mam.navigation.AuthEventManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val userPreferencesRepository: UserPreferencesRepository
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        // 🛑 Fetch Access Token synchronously
        val accessToken = runBlocking { userPreferencesRepository.accessToken.first() }

        if (accessToken.isNotEmpty()) {
            request = request.newBuilder()
                .header("Authorization", "Bearer $accessToken")
                .build()
        }

        // ⏳ Proceed with the original request
        var response = chain.proceed(request)

        // 🔄 If token expired (401), refresh and retry the request
        if (response.code == 401) {
            Log.d("AUTH", "Old AccessToken: ${accessToken}.")
            Log.d("AUTH", "Token expired, attempting to refresh...")

            val newToken = runBlocking { refreshAccessToken() }
            if (newToken == null) {
                Log.d("AUTH", "Failed to refresh token, redirecting to login...")
                response.close()
                AuthEventManager.triggerUnauthorized()
                return response
            }
            runBlocking {
                userPreferencesRepository.saveAccessToken(
                    newToken?.accessToken ?: "",
                    newToken?.refreshToken ?: ""
                )
            }
            Log.d("AUTH", "New AccessToken: ${newToken.accessToken}.")
            // 🔄 Retry with new token
            request = request.newBuilder()
                .header("Authorization", "Bearer ${newToken.accessToken}")
                .build()

            response.close() // ✅ Close old response before retrying
            response = chain.proceed(request) // ✅ Retry the request
        }
        return response
    }

    private suspend fun refreshAccessToken(): AuthResponse? {
        val refreshToken = runBlocking { userPreferencesRepository.refreshToken.first() }
        return try {
            val response = BaseRepository(userPreferencesRepository).authPublicRepository.refreshToken(
                RefreshTokenRequest(refreshToken = refreshToken)
            ) // ✅ Using .execute() to ensure synchronous execution

            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            null
        }
    }
}
