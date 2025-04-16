package com.example.mam.gui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mam.R
import com.example.mam.ViewModel.SignUpViewModel
import com.example.mam.data.SignUpState
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

@Composable
fun SignUpScreen(
    onSignInClicked: () -> Unit = {},
    onSignUpClicked: () -> Unit = {},
    modifier: Modifier = Modifier
){
    val signUpVM: SignUpViewModel = viewModel()
    val signUpState: SignUpState by signUpVM.signUpState.collectAsState()
    val scrollState = rememberScrollState()
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(color = OrangeDefault)
            .padding(WindowInsets.statusBars.asPaddingValues())
            //.padding(WindowInsets.ime.asPaddingValues())
            .verticalScroll(scrollState),
    ) {
        Text(
            text = stringResource(R.string.dang_ky),
            modifier = Modifier
                .padding(top = 20.dp),
            style = Typography.titleLarge
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.Top),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .outerShadow(
                    color = GreyDark,
                    bordersRadius = 50.dp,
                    blurRadius = 4.dp,
                    offsetX = 0.dp,
                    offsetY = -4.dp,
                )
                .weight(1f)
                .background(
                    color = OrangeLighter,
                    shape = RoundedCornerShape(
                        topStart = 50.dp,
                        topEnd = 50.dp,
                        bottomStart = 0.dp,
                        bottomEnd = 0.dp
                    )
                )
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(top = 10.dp)
                    .wrapContentHeight(),
            ) {
                EditFieldType1(
                    label = "Họ tên",
                    value = signUpState.name,
                    backgroundColor = WhiteDefault,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    onValueChange = { signUpVM.setName(it) },
                    modifier = Modifier.fillMaxWidth()
                )
                EditFieldType1(
                    label = "Số điện thoại",
                    value = signUpState.phoneNumber,
                    backgroundColor = WhiteDefault,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    onValueChange = { signUpVM.setPhoneNumber(it) },
                    modifier = Modifier.fillMaxWidth()
                )
                EditFieldType1(
                    label = "Email",
                    value = signUpState.email,
                    backgroundColor = WhiteDefault,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    onValueChange = { signUpVM.setEmail(it) },
                    modifier = Modifier.fillMaxWidth()
                )
                EditFieldType1(
                    label = "Tên người dùng",
                    value = signUpState.name,
                    backgroundColor = WhiteDefault,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    onValueChange = { signUpVM.setUserName(it) },
                    modifier = Modifier.fillMaxWidth()
                )
                PasswordFieldType1(
                    label = "Mật khẩu",
                    value = signUpState.password,
                    subLabel = "Mật khẩu có ít nhất 6 chữ số",
                    backgroundColor = WhiteDefault,
                    onValueChange = { signUpVM.setPassword(it)},
                    modifier = Modifier.fillMaxWidth()
                )
                PasswordFieldType1(
                    label = "Xác nhận mật khẩu",
                    value = signUpState.repeatPassword,
                    errorLabel = if (signUpVM.isRepeatPasswordValid()) "Mật khẩu chưa đúng!" else "",
                    backgroundColor = WhiteDefault,
                    imeAction = ImeAction.Done,
                    onValueChange = { signUpVM.setRepeatPassword(it) },
                    modifier = Modifier.fillMaxWidth()
                )

            }
            Column (
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OuterShadowFilledButton(
                    text = "Đăng Ký",
                    isEnable = signUpVM.isSignUpButtonEnable(),
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
@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    SignUpScreen()
}