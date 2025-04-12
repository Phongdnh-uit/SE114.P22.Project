package com.example.mam.Screen.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mam.MainActivity
import com.example.mam.R
import com.example.mam.Screen.component.EditFieldType1
import com.example.mam.Screen.component.OuterShadowFilledButton
import com.example.mam.Screen.component.PasswordFieldType1
import com.example.mam.Screen.component.UnderlinedClickableText
import com.example.mam.Screen.component.outerShadow
import com.example.mam.ui.theme.BrownDark
import com.example.mam.ui.theme.GreyDark
import com.example.mam.ui.theme.OrangeDefault
import com.example.mam.ui.theme.OrangeLighter
import com.example.mam.ui.theme.Typography
import com.example.mam.ui.theme.Variables
import com.example.mam.ui.theme.WhiteDefault

@Composable
fun SignUpScreen(){
    var hotenInput by remember { mutableStateOf("") }
    val hoten = hotenInput

    var sdtInput by remember { mutableStateOf("") }
    val sdt = sdtInput

    var emailInput by remember { mutableStateOf("") }
    val email = emailInput

    var tenUserInput by remember { mutableStateOf("") }
    val tenUser = tenUserInput

    var mkInput by remember { mutableStateOf("") }
    val mk = mkInput

    var mkNhapLaiInput by remember { mutableStateOf("") }
    val mkNhapLai = mkNhapLaiInput

    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.Top),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(color = OrangeDefault)
            .padding(WindowInsets.safeDrawing.asPaddingValues())
            .padding(WindowInsets.ime.asPaddingValues()),
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
                .fillMaxHeight()
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
                    value = hotenInput,
                    backgroundColor = WhiteDefault,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    onValueChange = { hotenInput = it },
                    modifier = Modifier.fillMaxWidth()
                )
                EditFieldType1(
                    label = "Số điện thoại",
                    value = sdtInput,
                    backgroundColor = WhiteDefault,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    onValueChange = { sdtInput = it },
                    modifier = Modifier.fillMaxWidth()
                )
                EditFieldType1(
                    label = "Email",
                    value = emailInput,
                    backgroundColor = WhiteDefault,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    onValueChange = { emailInput = it },
                    modifier = Modifier.fillMaxWidth()
                )
                EditFieldType1(
                    label = "Tên người dùng",
                    value = tenUserInput,
                    backgroundColor = WhiteDefault,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    onValueChange = { tenUserInput = it },
                    modifier = Modifier.fillMaxWidth()
                )
                PasswordFieldType1(
                    label = "Mật khẩu",
                    value = mkInput,
                    subLabel = "Mật khẩu có ít nhất 6 chữ số",
                    backgroundColor = WhiteDefault,
                    onValueChange = { mkInput = it },
                    modifier = Modifier.fillMaxWidth()
                )
                PasswordFieldType1(
                    label = "Xác nhận mật khẩu",
                    value = mkNhapLaiInput,
                    errorLabel = "Mật khẩu chưa đúng!",
                    backgroundColor = WhiteDefault,
                    imeAction = ImeAction.Done,
                    onValueChange = { mkNhapLaiInput = it },
                    modifier = Modifier.fillMaxWidth()
                )

            }
            Column (
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OuterShadowFilledButton(
                    text = "Đăng Ký",
                    onClick = { },
                    modifier = Modifier
                        .width(182.dp)
                        .height(40.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Bạn đã có tài khoản?",
                        color = BrownDark,
                    )
                    UnderlinedClickableText(
                        text = "Đăng nhập ngay",
                        color = OrangeDefault,
                        targetActivity = MainActivity::class.java,
                        modifier = Modifier
                    )
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    SignUpScreen()
}