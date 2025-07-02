package com.example.mam.gui.screen.authentication

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation.Companion.keyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mam.gui.component.CircleIconButton
import com.example.mam.gui.component.EditFieldType1
import com.example.mam.gui.component.OuterShadowFilledButton
import com.example.mam.gui.component.PasswordFieldType1
import com.example.mam.gui.component.outerShadow
import com.example.mam.dto.authentication.ForgetPasswordRequest
import com.example.mam.ui.theme.GreyDark
import com.example.mam.ui.theme.OrangeDefault
import com.example.mam.ui.theme.OrangeLighter
import com.example.mam.ui.theme.Typography
import com.example.mam.ui.theme.WhiteDefault
import com.example.mam.viewmodel.authentication.ForgetPasswordViewModel
import kotlinx.coroutines.launch

@Composable
fun ForgetPasswordScreen(
    onChangeClicked: (String,String) -> Unit = { _,_ -> },
    onCloseClicked: () -> Unit = {},
    isForgot: Boolean = true,
    viewModel: ForgetPasswordViewModel = viewModel(),
    modifier: Modifier = Modifier
) {

    val email= viewModel.email.collectAsStateWithLifecycle().value
    val repeatPassword= viewModel.repeatPassword.collectAsStateWithLifecycle().value
    val password= viewModel.password.collectAsStateWithLifecycle().value
    val oldPassword = viewModel.oldPassword.collectAsStateWithLifecycle().value

    val context = LocalContext.current
    val scope =  rememberCoroutineScope()

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(GreyDark.copy(0.5f))
    ) {
        val boxScope = this
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .align(Alignment.Center)
                .fillMaxWidth(0.9f)
                .wrapContentHeight()
                .background(
                    color = OrangeDefault,
                    shape = RoundedCornerShape(
                        size = 50.dp
                    )
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp),
            ) {
                // Icon nằm trái
                CircleIconButton(
                    backgroundColor = OrangeLighter,
                    foregroundColor = OrangeDefault,
                    icon = Icons.Filled.Close,
                    shadow = "outer",
                    onClick = onCloseClicked,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 16.dp) // padding nếu cần
                )
                // Text nằm giữa
                Text(
                    text = if(isForgot) "Quên mật khẩu" else "Đổi mật khẩu",
                    style = Typography.titleLarge,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(18.dp, Alignment.Top),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .outerShadow(
                        color = GreyDark,
                        bordersRadius = 50.dp,
                        offsetX = 0.dp,
                        offsetY = -4.dp,
                    )
                    .wrapContentHeight()
                    .background(
                        color = OrangeLighter,
                        shape = RoundedCornerShape(
                            size = 50.dp
                        )
                    )
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.Top),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .wrapContentHeight()
                ) {
                    Spacer(modifier = Modifier.height(10.dp))
                    EditFieldType1(
                        label = if(isForgot)"Email" else "Mật khẩu cũ",
                        errorLabel = if (isForgot && !viewModel.isEmailValid()) "Email không hợp lệ" else "",
                        value = if(isForgot) email else oldPassword,
                        backgroundColor = WhiteDefault,
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        onValueChange = {
                            if(isForgot) viewModel.setEmail(it)
                            else viewModel.setOldPassword(it)},
                        modifier = Modifier.fillMaxWidth()
                    )
                    PasswordFieldType1(
                        label = "Mật khẩu mới",
                        subLabel = "Mật khẩu có ít nhất 6 chữ số",
                        errorLabel = if (!viewModel.isPasswordValid()) "Mật khẩu không hợp lệ!" else "",
                        value = password,
                        backgroundColor = WhiteDefault,
                        onValueChange = { viewModel.setNewPassword(it) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    PasswordFieldType1(
                        label = "Xác nhận mật khẩu mới",
                        errorLabel = if (!viewModel.isRepeatPasswordValid()) "Mật khẩu chưa đúng!" else "",
                        value = repeatPassword,
                        backgroundColor = WhiteDefault,
                        onValueChange = { viewModel.setRepeatPassword(it) },
                        imeAction = ImeAction.Done,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OuterShadowFilledButton(
                        text = "Xác nhận",
                        isEnable = viewModel.isChangeButtonEnable(isForgot),
                        onClick = {
                            if (isForgot) {
                                scope.launch{
                                    val message = viewModel.sendOTP()
                                    if( message == "SUCCESS") {
                                        Toast.makeText(
                                            context,
                                            "Gửi mã OTP thành công",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        onChangeClicked(email, password)
                                    } else if (message == "FAILED") {
                                        Toast.makeText(
                                            context,
                                            "Gửi mã OTP thất bại, vui lòng thử lại sau",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        Toast.makeText(
                                            context,
                                            message,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }

                            } else {
                                scope.launch {
                                    val message =viewModel.changePassword()
                                    if( message == "SUCCESS") {
                                        Toast.makeText(
                                            context,
                                            "Đổi mật khẩu thành công",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        onCloseClicked()
                                    } else if (message == "FAILED") {
                                        Toast.makeText(
                                            context,
                                            "Đổi mật khẩu thất bại, vui lòng thử lại sau",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    else {
                                        Toast.makeText(
                                            context,
                                            message,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }

                        },
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .height(40.dp)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}
