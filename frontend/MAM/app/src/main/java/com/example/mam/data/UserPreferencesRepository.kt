package com.example.mam.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class UserPreferencesRepository (
    private val dataStore: DataStore<Preferences>,
){
    private companion object {
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        val ADDRESS = stringPreferencesKey("address")
        val LONGITUDE = doublePreferencesKey("longitude")
        val LATITUDE = doublePreferencesKey("latitude")
    }
    suspend fun saveAccessToken(accessToken: String, refreshToken: String) {
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN] = accessToken
            preferences[REFRESH_TOKEN] = refreshToken
        }
    }
    suspend fun saveAddress(address: String, longitude: Double, latitude: Double) {
        dataStore.edit{ preferences ->
            preferences[ADDRESS] = address
            preferences[LONGITUDE] = longitude
            preferences[LATITUDE] = latitude
        }
    }
    val accessToken: Flow<String> = dataStore.data
        .catch {
            if(it is IOException) {
                Log.e("DataStoreError", "Error reading preferences.", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[ACCESS_TOKEN] ?: ""
        }
    val refreshToken: Flow<String> = dataStore.data
        .catch {
            if(it is IOException) {
                Log.e("DataStoreError", "Error reading preferences.", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[REFRESH_TOKEN] ?: ""
        }
    val address: Flow<String> = dataStore.data
        .catch {
            if(it is IOException) {
                Log.e("DataStoreError", "Error reading preferences.", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[ADDRESS] ?: ""
        }
    val longitude: Flow<Double> = dataStore.data
        .catch {
            if(it is IOException) {
                Log.e("DataStoreError", "Error reading preferences.", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[LONGITUDE] ?: 0.0
        }
    val latitude: Flow<Double> = dataStore.data
        .catch {
            if(it is IOException) {
                Log.e("DataStoreError", "Error reading preferences.", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map { preferences ->
            preferences[LATITUDE] ?: 0.0
        }
}