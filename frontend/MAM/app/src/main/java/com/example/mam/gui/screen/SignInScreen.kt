package com.example.mam.gui.screen

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mam.R
import com.example.mam.gui.component.BasicOutlinedButton
import com.example.mam.gui.component.EditField
import com.example.mam.gui.component.OuterShadowFilledButton
import com.example.mam.gui.component.PasswordField
import com.example.mam.gui.component.UnderlinedClickableText
import com.example.mam.gui.component.outerShadow
import com.example.mam.ui.theme.GreyDark
import com.example.mam.ui.theme.OrangeDefault
import com.example.mam.ui.theme.OrangeLighter
import com.example.mam.ui.theme.Typography

@Composable
fun SignInScreen(
    onSignInClicked: (String, String) -> Unit = {phone: String, passw: String ->},
    onSignUpClicked: () -> Unit = {},
    onForgotClicked: () -> Unit = {},
    onTermsClicked: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var sdtInput by remember { mutableStateOf("admin0173") }
    val sdt = sdtInput
    var mkInput by remember { mutableStateOf("admin0173@") }
    val mk = mkInput
    val scrollState = rememberScrollState()
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .background(color = OrangeDefault)
            .padding(WindowInsets.statusBars.asPaddingValues())
            //.padding(WindowInsets.ime.asPaddingValues())
            .verticalScroll(scrollState),
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "Đăng nhập",
            style = Typography.titleLarge
        )
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .outerShadow(
                    color = GreyDark,
                    bordersRadius = 50.dp,
                    blurRadius = 4.dp,
                    offsetX = 0.dp,
                    offsetY = -4.dp,
                )
                .fillMaxWidth()
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
            Spacer(Modifier.height(10.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.Top),
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
            ) {
                EditField(
                    label = "Số điện thoại/Email/Username",
                    value = sdtInput,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    onValueChange = { sdtInput = it },
                    modifier = Modifier.fillMaxWidth()
                )
                PasswordField(
                    label = "Mật khẩu",
                    value = mkInput,
                    onValueChange = { mkInput = it },
                    modifier = Modifier.fillMaxWidth()
                )
                UnderlinedClickableText(
                    link = "Quên mật khẩu",
                    onClick = onForgotClicked,
                    modifier = Modifier
                        .align(Alignment.Start)
                    )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .wrapContentHeight()
            ){
                BasicOutlinedButton(
                    text = "Đăng ký",
                    onClick = onSignUpClicked,
                    modifier = Modifier
                        .fillMaxWidth(0.3f)
                        .height(40.dp)
                )
                OuterShadowFilledButton(
                        text = "Đăng nhập",
                        onClick = { onSignInClicked(sdt, mk) },
                        isEnable = (mkInput.isNotEmpty() && sdtInput.isNotEmpty()),
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(40.dp),
                )
            }
            UnderlinedClickableText(
                link = "Điều khoản và Chính sách",
                onClick = onTermsClicked,
                modifier = Modifier
            )
            Spacer(Modifier.height(10.dp))
        }
    }
}


@Preview(showBackground = true)
@Composable
fun Preview() {
        SignInScreen()
}