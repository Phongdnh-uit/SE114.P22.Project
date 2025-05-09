package com.example.mam.gui.screen.client

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mam.gui.component.MapBoxMap
import com.example.mam.gui.component.OuterShadowFilledButton
import com.example.mam.gui.component.outerShadow
import com.example.mam.ui.theme.BrownDefault
import com.example.mam.ui.theme.GreyDark
import com.example.mam.ui.theme.OrangeDefault
import com.example.mam.ui.theme.OrangeLighter
import com.example.mam.ui.theme.WhiteDefault
import com.mapbox.geojson.Point
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

@SuppressLint("MissingPermission")
@Composable
fun MapScreen(
    onSelectClicked: (String) -> Unit = {},
    onBackClicked: () -> Unit = {},
    modifier: Modifier = Modifier
){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(OrangeDefault)
            .padding(WindowInsets.statusBars.asPaddingValues())
    ){
        val defaultLocation by remember { mutableStateOf(Point.fromLngLat(106.803017, 10.870483)) }
        var currentLocation by remember { mutableStateOf(defaultLocation) }
        var address by remember { mutableStateOf("") }
        var relaunch by remember {
            mutableStateOf(false)
        }
        val coroutine = rememberCoroutineScope()
        val context = LocalContext.current
        val permissionRequest = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestMultiplePermissions(),
            onResult = { permissions ->
                if (!permissions.values.all { it }) {
                    //handle permission denied
                }
                else {
                    relaunch = !relaunch
                }
            }
        )
        MapBoxMap(
            point = currentLocation,
            onPointChange = {
                coroutine.launch {
                    currentLocation = it
                    address = getAddressFromCoordinates(
                        context,
                        currentLocation.latitude(),
                        currentLocation.longitude()
                    )
                }
            },
            modifier = Modifier
                .fillMaxSize()
        )
        LaunchedEffect(key1 = relaunch) {
            try {
                // Use Android's FusedLocationProviderClient for location retrieval
                val fusedLocationClient = com.google.android.gms.location.LocationServices.getFusedLocationProviderClient(context)
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    location?.let {
                        currentLocation = Point.fromLngLat(it.longitude, it.latitude)
                        address  = getAddressFromCoordinates(context, currentLocation.latitude(), currentLocation.longitude())
                    } ?: run {
                        // Handle case where location is null
                    }
                }.addOnFailureListener {
                    // Handle failure to retrieve location
                }
            } catch (e: Exception) {
                when (e) {
                    is SecurityException -> {
                        permissionRequest.launch(
                            arrayOf(
                                android.Manifest.permission.ACCESS_FINE_LOCATION,
                                android.Manifest.permission.ACCESS_COARSE_LOCATION
                            )
                        )
                    }
                    else -> {
                        // Handle other exceptions
                    }
                }
            }
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .outerShadow(
                    color = GreyDark,
                    bordersRadius = 50.dp,
                    blurRadius = 4.dp,
                    offsetX = 0.dp,
                    offsetY = -4.dp,
                )
                .background(
                    color = OrangeLighter,
                    shape = RoundedCornerShape(
                        topStart = 50.dp,
                        topEnd = 50.dp,
                        bottomStart = 0.dp,
                        bottomEnd = 0.dp
                    )
                )
                .fillMaxWidth()
                .wrapContentHeight()
                .clip(
                    shape = RoundedCornerShape(
                        topStart = 50.dp,
                        topEnd = 50.dp,
                        bottomStart = 0.dp,
                        bottomEnd = 0.dp
                    )
                )
                .align(Alignment.BottomCenter)
        ){
            Text(
                text = "Địa chỉ:",
                fontSize = 20.sp,
                color = BrownDefault,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth(0.9f)
            )
            OutlinedTextField(
                value = address,
                onValueChange = { newAddress ->
                    address = newAddress
                    coroutine.launch{
                        var geocoder = Geocoder(context, Locale("vi", "VN"));
                        geocoder.getFromLocationName(address, 1)?.firstOrNull()?.let { location ->
                            currentLocation =
                                Point.fromLngLat(location.longitude, location.latitude)
                        }
                    }
                },
                textStyle = TextStyle(fontSize = 20.sp, color = BrownDefault),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = WhiteDefault,  // Màu nền khi focus
                    unfocusedContainerColor = WhiteDefault, // Màu nền khi không focus
                    focusedIndicatorColor = BrownDefault,  // Màu viền khi focus
                    unfocusedIndicatorColor = BrownDefault,  // Màu viền khi không focus
                    focusedTextColor = BrownDefault,       // Màu chữ khi focus
                    unfocusedTextColor = BrownDefault,      // Màu chữ khi không focus
                    cursorColor = BrownDefault             // Màu con trỏ nhập liệu
                ),
                shape = RoundedCornerShape(10.dp),
                singleLine = true,
                modifier = Modifier
                    .padding(2.dp)
                    .fillMaxWidth(0.9f)
            )
            OuterShadowFilledButton(
                text = "Áp dụng",
                fontSize = 18.sp,
                onClick = { onSelectClicked(address) },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(bottom = 10.dp)

            )

        }
    }
}

fun getAddressFromCoordinates(context: Context, lat: Double, lon: Double): String {
    return try {
        val geocoder = Geocoder(context, Locale("vi", "VN"))
        geocoder.getFromLocation(lat, lon, 1)?.firstOrNull()?.getAddressLine(0) ?: "Không tìm thấy địa chỉ"
    } catch (e: Exception) {
        "Không thể lấy địa chỉ"
    }
}

@Preview
@Composable
fun MapScreenPreview(){
    MapScreen()
}