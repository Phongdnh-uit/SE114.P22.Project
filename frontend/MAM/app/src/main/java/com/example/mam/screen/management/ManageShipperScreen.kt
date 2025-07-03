package com.example.mam.screen.management

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mam.component.CircleIconButton
import com.example.mam.component.outerShadow
import com.example.mam.ui.theme.BrownDefault
import com.example.mam.ui.theme.ErrorColor
import com.example.mam.ui.theme.GreyDark
import com.example.mam.ui.theme.GreyDefault
import com.example.mam.ui.theme.OrangeDefault
import com.example.mam.ui.theme.OrangeLighter
import com.example.mam.ui.theme.Typography
import com.example.mam.viewmodel.management.ManageShipperViewModel
import kotlinx.coroutines.launch

@Composable
fun ManageShipperScreen(
    viewModel: ManageShipperViewModel,
    onBackClick: () -> Unit,
    isPreview: Boolean = false,
    isEdit: Boolean = false,
    isAdd: Boolean = false,
) {
    val shipperID = viewModel.shipperID.collectAsStateWithLifecycle().value
    val shipperName = viewModel.shipperName.collectAsStateWithLifecycle().value
    val shipperPhone = viewModel.shipperPhone.collectAsStateWithLifecycle().value
    val shipperLicense = viewModel.shipperLicense.collectAsStateWithLifecycle().value
    val isLoading = viewModel.isLoading.collectAsStateWithLifecycle().value
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val activity = context as? Activity
    LaunchedEffect(Unit) {
        if (isPreview) {
            viewModel.mockData()
        } else if (isEdit) {
            viewModel.loadData()
        } else {
            viewModel.clearData()
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OrangeDefault)
            .padding(WindowInsets.statusBars.asPaddingValues())
    ) {
        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            CircleIconButton(
                backgroundColor = OrangeLighter,
                foregroundColor = OrangeDefault,
                icon = Icons.Outlined.ArrowBack,
                shadow = "outer",
                onClick = onBackClick,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 16.dp, start = 16.dp)
            )
            val isButtonEnable = if (isAdd || isEdit) {
                shipperLicense.isNotEmpty() && shipperName.isNotEmpty() && shipperPhone.isNotEmpty()
                        && viewModel.isShipperNameValid().isEmpty() && viewModel.isPhoneValid().isEmpty()
                        && viewModel.isLicenseValid().isEmpty()
            } else true
            if (isButtonEnable) CircleIconButton(
                backgroundColor = OrangeLighter,
                foregroundColor = OrangeDefault,
                icon = Icons.Default.Done ,
                shadow = "outer",
                onClick = {
                    scope.launch {
                        if (isEdit) {
                            val result = viewModel.updateShipper()
                            Toast.makeText(
                                context,
                                when(result){
                                    1 -> "Chỉnh sửa thành công"
                                    else -> "Chỉnh sửa thất bại"
                                },
                                Toast.LENGTH_SHORT
                            ).show()
                            onBackClick()
                        } else if (isAdd) {
                            val result = viewModel.addShipper()
                            Toast.makeText(
                                context,
                                when(result){
                                    1 -> "Thêm thành công"
                                    else -> "Thêm thất bại"
                                },
                                Toast.LENGTH_SHORT
                            ).show()
                            onBackClick()
                        }
                    }
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 16.dp, top = 16.dp)
            )
            Text(
                text = if (isAdd) "Thêm Shipper" else "Chỉnh sửa Shipper" ,
                style = Typography.titleLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 17.dp)
            )
        }
        Spacer(modifier = Modifier.size(20.dp))
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
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
                .height(LocalConfiguration.current.screenHeightDp.dp)
                .clip(
                    shape = RoundedCornerShape(
                        topStart = 50.dp,
                        topEnd = 50.dp,
                        bottomStart = 0.dp,
                        bottomEnd = 0.dp
                    )
                )
        ) {
            item {
                Spacer(modifier = Modifier.size(20.dp))
            }
            if (isLoading)
            {
                item {
                    CircularProgressIndicator(
                        color = OrangeDefault,
                        modifier = Modifier
                            .padding(16.dp)
                            .size(40.dp)
                    )
                }
            }
            else {
                if (!isAdd) item {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("ID: ")
                            }
                            append(shipperID.toString())
                        },
                        textAlign = TextAlign.Start,
                        color = GreyDefault,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth()
                    )
                }
                item {
                    OutlinedTextField(
                        value = shipperName,
                        onValueChange = {
                            viewModel.setShipperName(it)
                        },
                        textStyle = TextStyle(
                            color = BrownDefault,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BrownDefault,
                            unfocusedBorderColor = GreyDefault,
                        ),
                        singleLine = true,
                        label = {
                            Text(
                                text = "Tên shipper",
                                color = BrownDefault,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier
                            )
                        },
                        supportingText = {
                            if (viewModel.isShipperNameValid()
                                    .isNotEmpty() && shipperName.isNotEmpty()
                            ) {
                                Text(
                                    text = viewModel.isShipperNameValid(),
                                    color = ErrorColor,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal,
                                    modifier = Modifier
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        ),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    )
                }
                item {
                    OutlinedTextField(
                        value = shipperPhone,
                        onValueChange = {
                            viewModel.setShipperPhone(it)
                        },
                        textStyle = TextStyle(
                            color = BrownDefault,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BrownDefault,
                            unfocusedBorderColor = GreyDefault,
                        ),
                        singleLine = true,
                        label = {
                            Text(
                                text = "Số điện thoại",
                                color = BrownDefault,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier
                            )
                        },
                        supportingText = {
                            if (viewModel.isPhoneValid()
                                    .isNotEmpty() && shipperPhone.isNotEmpty()
                            ) {
                                Text(
                                    text = viewModel.isPhoneValid(),
                                    color = ErrorColor,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal,
                                    modifier = Modifier
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        ),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    )
                }
                item {
                    OutlinedTextField(
                        value = shipperLicense,
                        onValueChange = {
                            viewModel.setShipperLicense(it)
                        },
                        textStyle = TextStyle(
                            color = BrownDefault,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BrownDefault,
                            unfocusedBorderColor = GreyDefault,
                        ),
                        singleLine = true,
                        label = {
                            Text(
                                text = "Biển số xe",
                                color = BrownDefault,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier
                            )
                        },
                        supportingText = {
                            if (viewModel.isLicenseValid()
                                    .isNotEmpty() && shipperLicense.isNotEmpty()
                            ) {
                                Text(
                                    text = viewModel.isLicenseValid(),
                                    color = ErrorColor,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal,
                                    modifier = Modifier
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        ),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    )
                }
            }
        }
    }
}

//@Preview
//@Composable
//fun ManageShipperScreenPreview() {
//    val viewModel = ManageShipperViewModel(savedStateHandle = SavedStateHandle().apply {
//        set("shipperId", "123")
//    })
//    ManageShipperScreen(
//        viewModel = viewModel,
//        onBackClick = {},
//        isPreview = false,
//        isEdit = false,
//        isAdd = true
//    )
//}