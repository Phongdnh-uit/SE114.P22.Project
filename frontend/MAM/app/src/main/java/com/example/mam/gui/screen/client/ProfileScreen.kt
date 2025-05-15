package com.example.mam.gui.screen.client

import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.service.autofill.FillEventHistory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.ArrowForwardIos
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.mam.entity.User
import com.example.mam.entity.authorization.request.SignUpRequest
import com.example.mam.gui.component.CircleIconButton
import com.example.mam.gui.component.NormalButtonWithIcon
import com.example.mam.gui.component.OuterShadowFilledButton
import com.example.mam.gui.component.ProfileInput
import com.example.mam.gui.component.UnderlinedClickableText
import com.example.mam.gui.component.innerShadow
import com.example.mam.gui.component.outerShadow
import com.example.mam.ui.theme.BrownDark
import com.example.mam.ui.theme.BrownDefault
import com.example.mam.ui.theme.GreyAvaDefault
import com.example.mam.ui.theme.GreyDark
import com.example.mam.ui.theme.OrangeDefault
import com.example.mam.ui.theme.OrangeLight
import com.example.mam.ui.theme.OrangeLighter
import com.example.mam.ui.theme.Typography
import com.example.mam.ui.theme.WhiteDefault
import com.example.mam.viewmodel.client.ProfileViewModel

@SuppressLint("ContextCastToActivity")
@Composable
fun ProfileScreen(
    onChangePasswordClicked: () -> Unit = {},
    onEditInfoClicked: () -> Unit = {},
    onLogoutClicked: () -> Unit = {},
    onBackClicked: () -> Unit = {},
    onSettingClicked: () -> Unit = {},
    onTermsClicked: () -> Unit = {},
    onHistoryClicked: () -> Unit = {},
    viewModel: ProfileViewModel? = null
    ) {
    val context = LocalContext.current
    val isPreview = LocalInspectionMode.current
    val activity = context as? Activity

    val userState = viewModel?.user?.collectAsState()
    val previewUser = remember {
        mutableStateOf(
            User(
                id = "1",
                fullName = "Nguyễn Văn A",
                phoneNumber = "0123456789",
                email = "a@example.com",
                username = "nguyenvana",
                password = "",
                avatarUrl = "",
                address = "245 Nguyễn Sinh Cung, Vỹ Dạ, Phú Nhuận, Thành phố Huế"
            )
        )
    }
    val user = userState?.value ?: if (isPreview) previewUser.value else return
    val isEditingState = viewModel?.isEditing?.collectAsStateWithLifecycle()
    val previewIsEditing = remember { mutableStateOf(false) }
    val isEditing = isEditingState?.value ?: if (isPreview) previewIsEditing.value else false
    val imagePicker = if (!isPreview) {
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                viewModel?.uploadAvatar(context, it)
            }
        }
    } else null
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(color = OrangeDefault)
            .padding(0.dp)
            .padding(WindowInsets.statusBars.asPaddingValues())
        //.padding(WindowInsets.ime.asPaddingValues())
    ){
        item{
            Box(
                contentAlignment = Alignment.CenterStart,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                CircleIconButton(
                    backgroundColor = OrangeLighter,
                    foregroundColor = OrangeDefault,
                    icon = Icons.Filled.ArrowBack,
                    shadow = "outer",
                    onClick = onBackClicked,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(top = 16.dp, start = 16.dp)
                )
                Text(
                    text = "Trang cá nhân",
                    style = Typography.titleLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(top = 17.dp)
                )
            }
        }
        item{
            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
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
                    .heightIn(min = LocalConfiguration.current.screenHeightDp.dp),
            ) {
                Spacer(Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .size(180.dp),
                ){
                    AsyncImage(
                        model = user?.avatarUrl?.takeIf { it.isNotEmpty() },
                        contentDescription = "Avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(180.dp)
                            .clip(CircleShape)
                            .background(GreyAvaDefault)
                    )
                    IconButton(
                        onClick = {
                            val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                android.Manifest.permission.READ_MEDIA_IMAGES
                            } else {
                                android.Manifest.permission.READ_EXTERNAL_STORAGE
                            }
                            if (context.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED && activity != null) {
                                ActivityCompat.requestPermissions(
                                    activity,
                                    arrayOf(permission),
                                    123
                                )
                            } else {
                                imagePicker?.launch("image/*")
                            }
                        },
                        modifier = Modifier
                            .size(30.dp)
                            .align(Alignment.BottomEnd)
                            .innerShadow(
                                color = GreyDark,
                                bordersRadius = 25.dp,
                                blurRadius = 10.dp,
                                offsetX = 0.dp,
                                offsetY = 10.dp,
                            )
                            .background(OrangeDefault, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = "Settings",
                            tint = WhiteDefault,
                            modifier = Modifier
                                .size(30.dp)
                        )
                    }
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(top = 10.dp)
                        .fillMaxHeight()
                ) {
                    ProfileInput(
                        label = "Họ tên",
                        value = user.fullName,
                        backgroundColor = WhiteDefault,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        onValueChange = {
                            if (viewModel != null) viewModel.setFullName(it)
                            else previewUser.value = previewUser.value.copy(fullName = it)
                        },
                        enabled = isEditing,
                        modifier = Modifier.fillMaxWidth()
                    )
                    ProfileInput(
                        label = "Số điện thoại",
                        value = user.phoneNumber,
                        backgroundColor = WhiteDefault,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        onValueChange = {
                            if (isPreview) {
                                previewUser.value = previewUser.value.copy(phoneNumber = it)
                            } else {
                                viewModel?.setFullName(it)
                            }
                        },
                        enabled = isEditing,
                        modifier = Modifier.fillMaxWidth()
                    )
                    ProfileInput(
                        label = "Email",
                        value = user.email,
                        backgroundColor = WhiteDefault,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        onValueChange = {
                            if (isPreview) {
                                previewUser.value = previewUser.value.copy(email = it)
                            } else {
                                viewModel?.setFullName(it)
                            }
                        },
                        enabled = isEditing,
                        modifier = Modifier.fillMaxWidth()
                    )
                    ProfileInput(
                        label = "Địa chỉ",
                        value = user.address,
                        backgroundColor = WhiteDefault,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        onValueChange = {
                            if (isPreview) {
                                previewUser.value = previewUser.value.copy(address = it)
                            } else {
                                viewModel?.setFullName(it)
                            }
                        },
                        enabled = isEditing,
                        modifier = Modifier.fillMaxWidth()
                    )
                    NormalButtonWithIcon(
                        text = "Lịch sử đơn hàng",
                        onClick = onChangePasswordClicked,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(39.dp),
                        icon = Icons.Outlined.ArrowForwardIos,
                        color = OrangeLight,
                        textColor = BrownDefault,
                        tintIcon = BrownDefault
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ){
                        OuterShadowFilledButton(
                            text = "Đổi mật khẩu",
                            onClick = onChangePasswordClicked,
                            modifier = Modifier
                                .wrapContentWidth()
                                .height(40.dp)
                        )
                        OuterShadowFilledButton(
                            onClick = {
                                if (viewModel != null) viewModel.setEditing(!isEditing)
                                else previewIsEditing.value = !previewIsEditing.value
                            },
                            text = if (isEditing) "Lưu" else "Chỉnh sửa",
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                        )
                    }
                    OuterShadowFilledButton(
                        text = "Đăng xuất",
                        onClick = onLogoutClicked,
                        modifier = Modifier
                            .width(182.dp)
                            .height(40.dp)
                    )
                    UnderlinedClickableText(
                        link = "Chính sách và điều khoản",
                        onClick = onTermsClicked,
                        modifier = Modifier
                            .align(Alignment.Start)
                    )
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen()
}
