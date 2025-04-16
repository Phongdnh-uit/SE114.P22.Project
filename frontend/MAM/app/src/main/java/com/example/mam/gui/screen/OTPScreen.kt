package com.example.mam.gui.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mam.gui.component.OtpInputField
import com.example.mam.gui.component.CircleIconButton
import com.example.mam.gui.component.OtpInputWithCountdown
import com.example.mam.gui.component.OuterShadowFilledButton
import com.example.mam.gui.component.outerShadow
import com.example.mam.ui.theme.BrownDefault
import com.example.mam.ui.theme.GreyDark
import com.example.mam.ui.theme.OrangeDefault
import com.example.mam.ui.theme.OrangeLight
import com.example.mam.ui.theme.OrangeLighter
import com.example.mam.ui.theme.Variables
import com.example.mam.ui.theme.WhiteDefault

@Composable
fun ForgetPasswordScreen(modifier: Modifier = Modifier) {
    var otpInput by remember { mutableStateOf("") }
    val otp = otpInput
    var resetTrigger by remember { mutableStateOf(false) } // State để trigger reset

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(WhiteDefault)
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
                    .height(70.dp)
            ) {
                // Icon nằm trái
                CircleIconButton(
                    backgroundColor = OrangeLighter,
                    foregroundColor = OrangeDefault,
                    icon = Icons.Filled.Close,
                    shadow = "outer",
                    onClick = {},
                    modifier = Modifier
                        .focusable(false)
                        .align(Alignment.CenterStart)
                        .padding(start = 16.dp) // padding nếu cần
                )
                // Text nằm giữa
                Text(
                    text = "Quên mật khẩu",
                    style = TextStyle(
                        fontSize = Variables.HeadlineMediumSize,
                        lineHeight = Variables.HeadlineMediumLineHeight,
                        fontWeight = FontWeight(700),
                        color = WhiteDefault,
                        textAlign = TextAlign.Center,
                    ),
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(18.dp, Alignment.Top),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusable(false)
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
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Chúng tôi sẽ gửi mã OTP đến \nsố điện thoại của bạn",
                    style = TextStyle(
                        fontSize = Variables.BodySizeMedium,
                        lineHeight = 22.4.sp,
                        fontWeight = FontWeight(Variables.BodyFontWeightRegular),
                        color = BrownDefault,
                        textAlign = TextAlign.Center,
                    )
                )
                Text(
                    text = "+84 904 599 204",
                    style = TextStyle(
                        fontSize = Variables.BodySizeMedium,
                        lineHeight = 22.4.sp,
                        fontWeight = FontWeight.Bold,
                        color = BrownDefault,
                        textAlign = TextAlign.Center,
                    )
                )
                Box(
                    modifier = Modifier
                        .background(
                            color = OrangeLight,
                            shape = RoundedCornerShape(40.dp)
                        )
                        .padding(start = 14.dp, end = 14.dp)
                        .focusable(false)
                ) {
                    OtpInputField(
                        otpLength = 4,
                        onOtpChange = { otp ->
                            otpInput = otp
                        },
                        onOtpComplete = { otp ->
                            otpInput = otp
                        },
                        resetTrigger = resetTrigger
                    )
                }
                OtpInputWithCountdown(
                    onResendClick = {
                        resetTrigger = !resetTrigger
                        otpInput = ""
                    }
                )
                OuterShadowFilledButton(
                    text = "Xác nhận",
                    onClick = { },
                    isEnable = (otpInput.length == 4),
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(40.dp),
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewForgetPasswordScreen() {
    ForgetPasswordScreen()
}