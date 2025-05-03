package com.example.mam.gui.screen.authorization

import android.annotation.SuppressLint
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
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mam.R
import com.example.mam.entity.authorization.request.SignUpRequest
import com.example.mam.gui.component.CircleIconButton
import com.example.mam.gui.component.EditFieldType1
import com.example.mam.gui.component.OuterShadowFilledButton
import com.example.mam.gui.component.PasswordFieldType1
import com.example.mam.gui.component.UnderlinedClickableText
import com.example.mam.gui.component.outerShadow
import com.example.mam.ui.theme.GreyDark
import com.example.mam.ui.theme.OrangeDefault
import com.example.mam.ui.theme.OrangeLighter
import com.example.mam.ui.theme.Typography
import com.example.mam.ui.theme.WhiteDefault
import com.example.mam.viewmodel.authorization.SignUpViewModel

@Composable
fun SignUpScreen(
    onSignInClicked: () -> Unit = {},
    onSignUpClicked: () -> Unit = {},
    onBackClicked: () -> Unit = {},
    viewModel: SignUpViewModel = viewModel(),
    modifier: Modifier = Modifier
){

    val signUpState: SignUpRequest by viewModel.signUpState.collectAsStateWithLifecycle()
    val repeatPassword: String by viewModel.repeatPassword.collectAsStateWithLifecycle()
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(color = OrangeDefault)
            .padding(0.dp)
            .padding(WindowInsets.statusBars.asPaddingValues())
            //.padding(WindowInsets.ime.asPaddingValues())
    ) {
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
                    text = stringResource(R.string.dang_ky),
                    style = Typography.titleLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(top = 17.dp)
                )
            }
        }
        item {
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
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(top = 10.dp)
                        .fillMaxHeight()
                ) {
                    EditFieldType1(
                        label = "Họ tên",
                        errorLabel = if (!viewModel.isFullNameValid()) "Họ tên không hợp lệ" else "",
                        value = signUpState.fullName,
                        backgroundColor = WhiteDefault,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        onValueChange = { viewModel.setName(it) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    EditFieldType1(
                        label = "Số điện thoại",
                        errorLabel = if (!viewModel.isPhoneNumberValid()) "Số điện thoại không hợp lệ" else "",
                        value = signUpState.phoneNumber,
                        backgroundColor = WhiteDefault,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        ),
                        onValueChange = { viewModel.setPhoneNumber(it) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    EditFieldType1(
                        label = "Email",
                        errorLabel = if (!viewModel.isEmailValid()) "Email không hợp lệ" else "",
                        value = signUpState.email,
                        backgroundColor = WhiteDefault,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        onValueChange = { viewModel.setEmail(it) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    EditFieldType1(
                        label = "Tên người dùng",
                        errorLabel = if (!viewModel.isUserNameValid()) "Tên người dùng không hợp lệ" else "",
                        value = signUpState.username,
                        backgroundColor = WhiteDefault,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        onValueChange = { viewModel.setUserName(it) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    PasswordFieldType1(
                        label = "Mật khẩu",
                        value = signUpState.password,
                        subLabel = "Mật khẩu có ít nhất 6 chữ số",
                        errorLabel = if (!viewModel.isPasswordValid()) "Mật khẩu không hợp lệ!" else "",
                        backgroundColor = WhiteDefault,
                        onValueChange = { viewModel.setSUPassword(it) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    PasswordFieldType1(
                        label = "Xác nhận mật khẩu",
                        value = repeatPassword,
                        errorLabel = if (!viewModel.isRepeatPasswordValid()) "Mật khẩu chưa đúng!" else "",
                        backgroundColor = WhiteDefault,
                        imeAction = ImeAction.Done,
                        onValueChange = { viewModel.setRepeatPassword(it) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OuterShadowFilledButton(
                        text = "Đăng Ký",
                        isEnable = viewModel.isSignUpButtonEnable(),
                        onClick = onSignUpClicked,
                        modifier = Modifier
                            .width(182.dp)
                            .height(40.dp)
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        UnderlinedClickableText(
                            text = "Bạn đã có tài khoản? ",
                            link = "Đăng nhập ngay",
                            linkColor = OrangeDefault,
                            onClick = onSignInClicked,
                            modifier = Modifier.padding(0.dp)
                        )
                    }
                    Spacer(Modifier.height(10.dp))
                }
            }
        }
    }
}

@SuppressLint("SuspiciousIndentation")
@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    SignUpScreen()
}