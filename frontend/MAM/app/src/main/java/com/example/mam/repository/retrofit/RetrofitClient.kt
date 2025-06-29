package com.example.mam.repository.retrofit

import com.example.mam.data.Constant.BASE_URL
import com.example.mam.data.UserPreferencesRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    //tạo Retrofit client cho public
    fun createPublicRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    //tạo Retrofit client cho private service, cần truyền access token vào
    fun createPrivateRetrofit(
        userPreferencesRepository: UserPreferencesRepository
    ): Retrofit {
        val authInterceptor = Interceptor { chain ->
            var request = chain.request()
            // Lấy access token từ UserPreferencesRepository
            val accessToken = runBlocking {userPreferencesRepository.accessToken.first()}
            if (accessToken.isNotEmpty()) {
                request = request.newBuilder()
                    .header("Authorization", "Bearer $accessToken")
                    .build()
            }
            chain.proceed(request)
        }
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(userPreferencesRepository))
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }



}