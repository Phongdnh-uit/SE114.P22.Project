package com.example.mam.gui.screen.authorization

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mam.R
import com.example.mam.gui.component.OuterShadowFilledButton
import com.example.mam.gui.component.UnderlinedClickableText
import com.example.mam.gui.component.outerShadow
import com.example.mam.ui.theme.BrownDefault
import com.example.mam.ui.theme.GreyDark
import com.example.mam.ui.theme.OrangeDefault
import com.example.mam.ui.theme.OrangeLighter
import com.example.mam.ui.theme.Typography

@Composable
fun StartScreen(
    onSignInClicked: () -> Unit = {},
    onSignUpClicked: () -> Unit = {},
    onGGSignUpClicked: () -> Unit = {},
    onTermsClicked: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .background(color = OrangeDefault)
            .padding(WindowInsets.statusBars.asPaddingValues())
            .verticalScroll(scrollState)
            //.padding(WindowInsets.ime.asPaddingValues())
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_mam_foreground),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "MaM chào bạn!",
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
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
            ) {
                OuterShadowFilledButton(
                    text = "Đăng ký qua Google",
                    image = R.drawable.ic_google,
                    onClick = onGGSignUpClicked,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                )
                HorizontalDivider(
                    color = BrownDefault,
                    modifier = Modifier
                        .fillMaxWidth(0.8f),

                )
                OuterShadowFilledButton(
                    text = "Đăng ký",
                    onClick = onSignUpClicked,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                )
                UnderlinedClickableText(
                    text = "Bạn đã có tài khoản? ",
                    link = "Đăng nhập ngay",
                    linkColor = OrangeDefault,
                    onClick = onSignInClicked,
                    modifier = Modifier.padding(0.dp)
                )
            }
            UnderlinedClickableText(
                text = "Nếu tiếp tục, bạn đã đồng ý với\n",
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
fun StartScreenPreview() {
    StartScreen()
}