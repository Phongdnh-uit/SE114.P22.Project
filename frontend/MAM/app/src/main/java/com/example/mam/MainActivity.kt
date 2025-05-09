package com.example.mam

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.mam.navigation.MainNavHost


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var permissionsGranted by remember { mutableStateOf(false) }

            if (permissionsGranted) {
                App()
            } else {
                RequestPermissions { granted ->
                    permissionsGranted = granted  // Update state when permissions are granted
                }
            }
        }
    }
}

@Composable
fun RequestPermissions(setPermissionsGranted: (Boolean) -> Unit) {
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsMap ->
        val granted = permissionsMap.all { it.value }
        if (granted) {
            Log.d("Map", "All required permissions granted")
            setPermissionsGranted(true)  // Set state instead of calling App()
        } else {
            Log.d("Map", "Permissions denied")
        }
    }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }
}

@Composable
fun App(){
    Surface() {
        MainNavHost(modifier = Modifier.padding())
    }
}

