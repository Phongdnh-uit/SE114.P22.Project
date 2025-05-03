<<<<<<< Updated upstream
package com.example.mam.gui.screen.authorization

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mam.gui.component.CircleIconButton
import com.example.mam.gui.component.OtpInputWithCountdown
import com.example.mam.gui.component.OuterShadowFilledButton
import com.example.mam.gui.component.newOtpInputField
import com.example.mam.gui.component.outerShadow
import com.example.mam.ui.theme.BrownDefault
import com.example.mam.ui.theme.GreyDark
import com.example.mam.ui.theme.OrangeDefault
import com.example.mam.ui.theme.OrangeLight
import com.example.mam.ui.theme.OrangeLighter
import com.example.mam.ui.theme.Variables
import com.example.mam.ui.theme.WhiteDefault
import com.example.mam.viewmodel.authorization.ForgetPasswordViewModel
import com.example.mam.viewmodel.authorization.otp.OtpAction
import com.plcoding.composeotpinput.OtpViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun OTPScreen(
    focusRequester: List<FocusRequester> = List(4) { FocusRequester() },
    forgetPasswordViewModel: ForgetPasswordViewModel = viewModel(),
    viewModel: OtpViewModel = viewModel(),
    onAction: (OtpAction) -> Unit = viewModel::onAction,
    onVerifyClicked: () -> Unit = {},
    onCloseClicked: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    // Quản lý focus và bàn phím
    val focusManager = LocalFocusManager.current
    val keyboardManager = LocalSoftwareKeyboardController.current

    val phoneNumber by forgetPasswordViewModel.phoneNumber.collectAsState()

    LaunchedEffect(Unit) {
        forgetPasswordViewModel.fetchPhoneNumber()
    }

    // Tự động focus vào ô tương ứng
    LaunchedEffect(state.focusedIndex) {
        state.focusedIndex?.let { index ->
            focusRequester.getOrNull(index)?.requestFocus()
        }
    }

    LaunchedEffect(state.code, keyboardManager) {
        val allNumbersEntered = state.code.none { it == null }
        if(allNumbersEntered) {
            focusRequester.forEach {
                it.freeFocus()
            }
            focusManager.clearFocus()
            keyboardManager?.hide()
        }
    }

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
                    onClick = onCloseClicked,
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
                    text = phoneNumber,
                   // text = viewModel.phoneNumber,
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
                        .width(238.dp)
                        .height(49.dp)
                        .background(
                            color = OrangeLight,
                            shape = RoundedCornerShape(40.dp)
                        )
                        .padding(start = 14.dp, end = 14.dp)
                        .focusable(false)
                ) {
//                    OtpInputField(
//                        otpLength = 4,
//                        onOtpChange = { viewModel.setOTP((it))
//                        },
//                        onOtpComplete = { viewModel.setOTP((it))
//                        },
//                        resetTrigger = resetTrigger
//                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(14.dp, Alignment.CenterHorizontally),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(vertical = 16.dp)
                            .fillMaxWidth()
                            .wrapContentHeight()
                    ) {
                        state.code.forEachIndexed { index, number ->
                            newOtpInputField(
                                number = number,
                                index = index,
                                focusedIndex = state.focusedIndex,
                                focusRequester = focusRequester[index],
                                onFocusChanged = { isFocused ->
                                    if (isFocused && state.focusedIndex != index) {
                                        onAction(OtpAction.OnChangeFieldFocused(index))
                                    }
                                },
                                onNumberChanged = { newNumber ->
                                    onAction(OtpAction.OnEnterNumber(newNumber, index))
                                },
                                onKeyboardBack = {
                                    onAction(OtpAction.OnKeyboardBack)
                                },
                                modifier = Modifier
                                    .width(42.dp)
                                    .aspectRatio(1f),
                                resetTrigger = resetTrigger
                            )
                        }
                    }
                }
                OtpInputWithCountdown(
                    onResendClick = {
                        resetTrigger = !resetTrigger
                        viewModel.setOTP((""))
                    }
                )
                OuterShadowFilledButton(
                    text = "Xác nhận",
                    onClick = onVerifyClicked,
                    //isEnable = viewModel.isOTPValid(),
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
    OTPScreen()
}
=======
//package com.example.mam.gui.screen.authorization
//
//import android.annotation.SuppressLint
//import androidx.compose.foundation.background
//import androidx.compose.foundation.focusable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.BoxWithConstraints
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.aspectRatio
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.wrapContentHeight
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Close
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.focus.FocusRequester
//import androidx.compose.ui.platform.LocalFocusManager
//import androidx.compose.ui.platform.LocalSoftwareKeyboardController
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.compose.collectAsStateWithLifecycle
//import androidx.lifecycle.viewmodel.compose.viewModel
//import com.example.mam.gui.component.OtpInputField
//import com.example.mam.gui.component.CircleIconButton
//import com.example.mam.gui.component.OtpInputWithCountdown
//import com.example.mam.gui.component.OuterShadowFilledButton
//import com.example.mam.gui.component.newOtpInputField
//import com.example.mam.viewmodel.authorization.otp.OtpAction
//import com.example.mam.viewmodel.authorization.otp.OtpState
//import com.example.mam.gui.component.outerShadow
//import com.example.mam.ui.theme.BrownDefault
//import com.example.mam.ui.theme.GreyDark
//import com.example.mam.ui.theme.OrangeDefault
//import com.example.mam.ui.theme.OrangeLight
//import com.example.mam.ui.theme.OrangeLighter
//import com.example.mam.ui.theme.Variables
//import com.example.mam.ui.theme.WhiteDefault
//import com.example.mam.viewmodel.authorization.ForgetPasswordViewModel
//import com.plcoding.composeotpinput.OtpViewModel
//
//@SuppressLint("StateFlowValueCalledInComposition")
//@Composable
//fun OTPScreen(
//    focusRequester: List<FocusRequester> = List(4) { FocusRequester() },
//    viewModel: ForgetPasswordViewModel = viewModel(),
//    onAction: (OtpAction) -> Unit = viewModel::onAction,
//    onVerifyClicked: () -> Unit = {},
//    onCloseClicked: () -> Unit = {},
//    modifier: Modifier = Modifier
//) {
//    val state by viewModel.state.collectAsStateWithLifecycle()
//
//    // Quản lý focus và bàn phím
//    val focusManager = LocalFocusManager.current
//    val keyboardManager = LocalSoftwareKeyboardController.current
//
//    // Tự động focus vào ô tương ứng
//    LaunchedEffect(state.focusedIndex) {
//        state.focusedIndex?.let { index ->
//            focusRequester.getOrNull(index)?.requestFocus()
//        }
//    }
//
//    LaunchedEffect(state.code, keyboardManager) {
//        val allNumbersEntered = state.code.none { it == null }
//        if(allNumbersEntered) {
//            focusRequester.forEach {
//                it.freeFocus()
//            }
//            focusManager.clearFocus()
//            keyboardManager?.hide()
//        }
//    }
//
//    var resetTrigger by remember { mutableStateOf(false) } // State để trigger reset
//
//    BoxWithConstraints(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(WhiteDefault)
//    ) {
//        val boxScope = this
//        Column(
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally,
//            modifier = modifier
//                .align(Alignment.Center)
//                .fillMaxWidth(0.9f)
//                .wrapContentHeight()
//                .background(
//                    color = OrangeDefault,
//                    shape = RoundedCornerShape(
//                        size = 50.dp
//                    )
//                )
//        ) {
//            Box(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(70.dp)
//            ) {
//                // Icon nằm trái
//                CircleIconButton(
//                    backgroundColor = OrangeLighter,
//                    foregroundColor = OrangeDefault,
//                    icon = Icons.Filled.Close,
//                    shadow = "outer",
//                    onClick = onCloseClicked,
//                    modifier = Modifier
//                        .focusable(false)
//                        .align(Alignment.CenterStart)
//                        .padding(start = 16.dp) // padding nếu cần
//                )
//                // Text nằm giữa
//                Text(
//                    text = "Quên mật khẩu",
//                    style = TextStyle(
//                        fontSize = Variables.HeadlineMediumSize,
//                        lineHeight = Variables.HeadlineMediumLineHeight,
//                        fontWeight = FontWeight(700),
//                        color = WhiteDefault,
//                        textAlign = TextAlign.Center,
//                    ),
//                    modifier = Modifier.align(Alignment.Center)
//                )
//            }
//            Column(
//                verticalArrangement = Arrangement.spacedBy(18.dp, Alignment.Top),
//                horizontalAlignment = Alignment.CenterHorizontally,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .focusable(false)
//                    .outerShadow(
//                        color = GreyDark,
//                        bordersRadius = 50.dp,
//                        offsetX = 0.dp,
//                        offsetY = -4.dp,
//                    )
//                    .wrapContentHeight()
//                    .background(
//                        color = OrangeLighter,
//                        shape = RoundedCornerShape(
//                            size = 50.dp
//                        )
//                    )
//            ) {
//                Spacer(modifier = Modifier.height(10.dp))
//                Text(
//                    text = "Chúng tôi sẽ gửi mã OTP đến \nsố điện thoại của bạn",
//                    style = TextStyle(
//                        fontSize = Variables.BodySizeMedium,
//                        lineHeight = 22.4.sp,
//                        fontWeight = FontWeight(Variables.BodyFontWeightRegular),
//                        color = BrownDefault,
//                        textAlign = TextAlign.Center,
//                    )
//                )
//                Text(
//                    text = viewModel.phoneNumber,
//                    style = TextStyle(
//                        fontSize = Variables.BodySizeMedium,
//                        lineHeight = 22.4.sp,
//                        fontWeight = FontWeight.Bold,
//                        color = BrownDefault,
//                        textAlign = TextAlign.Center,
//                    )
//                )
//                Box(
//                    modifier = Modifier
//                        .background(
//                            color = OrangeLight,
//                            shape = RoundedCornerShape(40.dp)
//                        )
//                        .padding(start = 14.dp, end = 14.dp)
//                        .focusable(false)
//                ) {
////                    OtpInputField(
////                        otpLength = 4,
////                        onOtpChange = { viewModel.setOTP((it))
////                        },
////                        onOtpComplete = { viewModel.setOTP((it))
////                        },
////                        resetTrigger = resetTrigger
////                    )
//                    Row(
//                        horizontalArrangement = Arrangement.spacedBy(12.dp),
//                        verticalAlignment = Alignment.CenterVertically,
//                        modifier = Modifier
//                            .padding(vertical = 16.dp)
//                            .fillMaxWidth()
//                            .height(64.dp) // Giới hạn chiều cao hợp lý
//                    ) {
//                        state.code.forEachIndexed { index, number ->
//                            newOtpInputField(
//                                number = number,
//                                index = index,
//                                focusedIndex = state.focusedIndex,
//                                focusRequester = focusRequester[index],
//                                onFocusChanged = { isFocused ->
//                                    if (isFocused && state.focusedIndex != index) {
//                                        onAction(OtpAction.OnChangeFieldFocused(index))
//                                    }
//                                },
//                                onNumberChanged = { newNumber ->
//                                    onAction(OtpAction.OnEnterNumber(newNumber, index))
//                                },
//                                onKeyboardBack = {
//                                    onAction(OtpAction.OnKeyboardBack)
//                                },
//                                modifier = Modifier
//                                    .weight(1f)
//                                    .aspectRatio(1f)
//                            )
//                        }
//                    }
//                    state.isValid?.let { isValid ->
//                    }
//                }
//                OtpInputWithCountdown(
//                    onResendClick = {
//                        resetTrigger = !resetTrigger
////                        viewModel.setOTP((""))
//                    }
//                )
//                OuterShadowFilledButton(
//                    text = "Xác nhận",
//                    onClick = onVerifyClicked,
//                    isEnable = viewModel.isOTPValid(),
//                    modifier = Modifier
//                        .fillMaxWidth(0.5f)
//                        .height(40.dp),
//                )
//                Spacer(modifier = Modifier.height(10.dp))
//            }
//        }
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun PreviewForgetPasswordScreen() {
//    OTPScreen()
//}
>>>>>>> Stashed changes
