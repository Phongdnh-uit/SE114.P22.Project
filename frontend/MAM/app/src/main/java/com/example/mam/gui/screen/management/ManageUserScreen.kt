package com.example.mam.gui.screen.management

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.core.app.ActivityCompat
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.mam.R
import com.example.mam.gui.component.CircleIconButton
import com.example.mam.gui.component.outerShadow
import com.example.mam.ui.theme.BrownDefault
import com.example.mam.ui.theme.ErrorColor
import com.example.mam.ui.theme.GreenDefault
import com.example.mam.ui.theme.GreyDark
import com.example.mam.ui.theme.GreyDefault
import com.example.mam.ui.theme.GreyLight
import com.example.mam.ui.theme.OrangeDefault
import com.example.mam.ui.theme.OrangeLighter
import com.example.mam.ui.theme.Typography
import com.example.mam.ui.theme.WhiteDefault
import com.example.mam.viewmodel.management.ManageUserViewModel
import kotlinx.coroutines.launch
import okhttp3.internal.platform.Jdk9Platform.Companion.isAvailable
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun ManageUserScreen(
    viewModel: ManageUserViewModel,
    onBackClick: () -> Unit,
    isPreview: Boolean = false,
    isEdit : Boolean = false,
) {
    val scope = rememberCoroutineScope()
    val userId = viewModel.userID.collectAsStateWithLifecycle().value
    val userName = viewModel.userName.collectAsStateWithLifecycle().value
    val imgUrl = viewModel.userImage.collectAsStateWithLifecycle().value
    val fullName = viewModel.fullName.collectAsStateWithLifecycle().value
    val email = viewModel.email.collectAsStateWithLifecycle().value
    val role = viewModel.role.collectAsStateWithLifecycle().value
    val phone = viewModel.phone.collectAsStateWithLifecycle().value
    val statusList = viewModel.statusList.collectAsStateWithLifecycle().value
    val status = viewModel.status.collectAsStateWithLifecycle().value
    val createAt = viewModel.createAt.collectAsStateWithLifecycle().value
    val updateAt = viewModel.updateAt.collectAsStateWithLifecycle().value
    val isLoading = viewModel.isLoading.collectAsStateWithLifecycle().value
    var isEditMode by remember { mutableStateOf(isEdit) }
    val context = LocalContext.current
    val activity = context as? Activity
    val imagePicker = if (!isPreview) {
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                viewModel.setUserImage(it.toString())
            }
        }
    } else null
    LaunchedEffect(Unit) {
        viewModel.loadData()
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
             CircleIconButton(
                backgroundColor = OrangeLighter,
                foregroundColor = OrangeDefault,
                icon = if (isEditMode) Icons.Default.Done else Icons.Default.Edit,
                shadow = "outer",
                onClick = {
                    scope.launch {
                        if (isEditMode) {
                            val result = viewModel.updateUserStatus()
                            Toast.makeText(
                                context,
                                when (result) {
                                    1 -> "Chỉnh sửa thành công"
                                    else -> "Chỉnh sửa thất bại"
                                },
                                Toast.LENGTH_SHORT
                            ).show()
                            onBackClick()
                        }
                        else {
                            isEditMode = true
                        }
                    }
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 16.dp, top = 16.dp)
            )
            Text(
                text = if (isEditMode) "Chỉnh sửa tài khoản" else "Chi tiết tài khoản",
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
            if (isLoading) {
                item {
                    CircularProgressIndicator(
                        color = OrangeDefault,
                        modifier = Modifier
                            .padding(16.dp)
                            .size(40.dp)
                    )
                }
            } else {
                item {
                    AsyncImage(
                        model = imgUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(R.drawable.upload_image),
                        error = null,
                        modifier = Modifier
                            .fillMaxWidth(0.8f) // Set width to 80% of screen width
                            .aspectRatio(1f)
                            .padding(8.dp)
                            .clip(RoundedCornerShape(50))
                    )

                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("ID: ")
                            }
                            append(userId.toString())
                        },
                        textAlign = TextAlign.Start,
                        color = GreyDefault,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth()
                    )
                    createAt.atZone(ZoneId.systemDefault()).let {
                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("Ngày tạo: ")
                                }
                                append(it.format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")))
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
                    updateAt.atZone(ZoneId.systemDefault()).let {
                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("Ngày cập nhật: ")
                                }
                                append(it.format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")))
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
                }
                item {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth().padding(horizontal = 16.dp)
                    ) {
                        FilterChip(
                            selected = false,
                            onClick = { },
                            label = {
                                Text(
                                    text = role,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = if (role == "Admin") GreenDefault else OrangeDefault,
                                labelColor = WhiteDefault,
                            ),
                            modifier = Modifier
                        )
                        Box(
                            Modifier
                                .zIndex(1f)
                        ) {
                            var statusExpanded by remember { mutableStateOf(false) }
                            FilterChip(
                                selected = statusExpanded,
                                onClick = {
                                    if (isEditMode) statusExpanded = !statusExpanded
                                },
                                label = {
                                    Text(
                                        text = status,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier
                                    )
                                },
                                trailingIcon = {
                                    if (isEditMode) Icon(
                                        if (!statusExpanded) Icons.Default.ArrowDropDown else Icons.Default.ArrowDropUp,
                                        contentDescription = "Expand"
                                    )
                                },
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled = true,
                                    selected = statusExpanded,
                                    borderWidth = 1.dp,
                                    selectedBorderColor = OrangeDefault
                                ),
                                colors = FilterChipDefaults.filterChipColors(
                                    containerColor = OrangeDefault,
                                    labelColor = WhiteDefault,
                                    iconColor = WhiteDefault,
                                    selectedContainerColor = OrangeDefault,
                                    selectedLabelColor = WhiteDefault,
                                    selectedLeadingIconColor = WhiteDefault,
                                    selectedTrailingIconColor = WhiteDefault
                                ),
                                modifier = Modifier
                            )
                            DropdownMenu(
                                expanded = statusExpanded,
                                onDismissRequest = { statusExpanded = false },
                                containerColor = WhiteDefault,
                                modifier = Modifier
                            ) {
                                statusList.forEach { status ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                status,
                                                color = BrownDefault
                                            )
                                        },
                                        onClick = {
                                            viewModel.setStatus(status)
                                            statusExpanded = false
                                        },
                                    )
                                }
                            }
                        }
                    }
                }
                item {
                    OutlinedTextField(
                        value = userName,
                        readOnly = true,
                        onValueChange = {
                            viewModel.setUserName(it)
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
                                text = "Tên người dùng",
                                color = BrownDefault,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier
                            )
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
                        value = fullName,
                        readOnly = true,
                        onValueChange = {
                            viewModel.setFullName(it)
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
                                text = "Tên đầy đủ",
                                color = BrownDefault,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier
                            )
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
                        value = email,
                        readOnly = true,
                        onValueChange = {
                            viewModel.setEmail(it)
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
                                text = "Email",
                                color = BrownDefault,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier
                            )
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
                        value = phone,
                        readOnly = true,
                        onValueChange = {
                            viewModel.setPhone(it)
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
                    Spacer(modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}

//@Preview
//@Composable
//fun ManageUserScreenPreview() {
//    ManageUserScreen(
//        viewModel = ManageUserViewModel(savedStateHandle = SavedStateHandle(mapOf("userId" to "1"))),
//        onBackClick = {},
//        isPreview = true,
//        isEdit = true,
//        isAdd = false
//    )
//}