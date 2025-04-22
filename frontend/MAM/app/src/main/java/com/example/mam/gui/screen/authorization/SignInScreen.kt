package com.example.mam.gui.screen.authorization

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mam.R
import com.example.mam.gui.component.CircleIconButton
import com.example.mam.gui.component.EditField
import com.example.mam.gui.component.OuterShadowFilledButton
import com.example.mam.gui.component.PasswordField
import com.example.mam.gui.component.UnderlinedClickableText
import com.example.mam.gui.component.outerShadow
import com.example.mam.entity.authorization.request.SignInRequest
import com.example.mam.ui.theme.GreyDark
import com.example.mam.ui.theme.OrangeDefault
import com.example.mam.ui.theme.OrangeLighter
import com.example.mam.ui.theme.Typography
import com.example.mam.viewmodel.authorization.SignInViewModel

@Composable
fun SignInScreen(
    onSignInClicked: () -> Unit = {},
    onForgotClicked: () -> Unit = {},
    onBackClicked: () -> Unit ={},
    viewModel: SignInViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val signInState: SignInRequest by viewModel.signInState.collectAsState()
    val scrollState = rememberScrollState()
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .background(color = OrangeDefault)
            .padding(WindowInsets.statusBars.asPaddingValues())
            //.padding(WindowInsets.ime.asPaddingValues())
            .verticalScroll(scrollState),
    ) {
        Box(
            contentAlignment = Alignment.TopStart
        ) {
            CircleIconButton(
                backgroundColor = OrangeLighter,
                foregroundColor = OrangeDefault,
                icon = Icons.Filled.ArrowBack,
                shadow = "outer",
                onClick = onBackClicked,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 16.dp, top = 16.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.ic_mam_foreground),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Text(
            text = "Đăng nhập",
            style = Typography.titleLarge
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp),
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
                .wrapContentHeight()
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
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
            ) {
                EditField(
                    label = "Số điện thoại/Email/Username",
                    value = signInState.username,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    onValueChange = { viewModel.setUsername(it) },
                    modifier = Modifier.fillMaxWidth()
                )
                PasswordField(
                    label = "Mật khẩu",
                    value = signInState.password,
                    onValueChange = { viewModel.setSIPassword(it)},
                    modifier = Modifier.fillMaxWidth()
                )
                UnderlinedClickableText(
                    link = "Quên mật khẩu",
                    onClick = onForgotClicked,
                    modifier = Modifier
                        .align(Alignment.Start)
                    )
            }
            OuterShadowFilledButton(
                text = "Đăng nhập",
                onClick = { onSignInClicked() },
                isEnable = (signInState.password.isNotEmpty() && signInState.username.isNotEmpty()),
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(40.dp),
            )
            Spacer(Modifier.height(10.dp))
        }
    }
}


@SuppressLint("SuspiciousIndentation")
@Preview(showBackground = true)
@Composable
fun SignInScreenPreview() {
        SignInScreen()
}