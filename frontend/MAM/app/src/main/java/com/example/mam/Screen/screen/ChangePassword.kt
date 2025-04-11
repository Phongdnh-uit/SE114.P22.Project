package com.example.mam.Screen.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mam.MainActivity
import com.example.mam.R
import com.example.mam.Screen.component.BasicOutlinedButton
import com.example.mam.Screen.component.EditField
import com.example.mam.Screen.component.EditFieldType1
import com.example.mam.Screen.component.InnerShadowFilledButton
import com.example.mam.Screen.component.OuterShadowFilledButton
import com.example.mam.Screen.component.PasswordField
import com.example.mam.Screen.component.PasswordFieldType1
import com.example.mam.Screen.component.UnderlinedClickableText
import com.example.mam.Screen.component.outerShadow
import com.example.mam.ui.theme.GreyDark
import com.example.mam.ui.theme.OrangeDefault
import com.example.mam.ui.theme.OrangeLighter
import com.example.mam.ui.theme.Variables
import com.example.mam.ui.theme.WhiteDefault

@Composable
fun ChangePasswordScreen(modifier: Modifier = Modifier) {
    var mkCuInput by remember { mutableStateOf("") }
    val mkCu = mkCuInput
    var mkMoiInput by remember { mutableStateOf("") }
    val mkMoi = mkMoiInput
    var mkMoiNhapLaiInput by remember { mutableStateOf("") }
    val mkMoiNhapLai = mkMoiNhapLaiInput
    Column(
        verticalArrangement = Arrangement.spacedBy(0.dp, Alignment.Top),
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(
                color = OrangeDefault,
                shape = RoundedCornerShape(
                    size = 50.dp
                )
            )
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp),
            verticalAlignment = Alignment.CenterVertically, // Thêm dòng này để căn giữa theo chiều dọc
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Đổi mật khẩu",
                style = TextStyle(
                    fontSize = Variables.HeadlineMediumSize,
                    lineHeight = Variables.HeadlineMediumLineHeight,
                    fontWeight = FontWeight(700),
                    color = WhiteDefault,
                    textAlign = TextAlign.Center,
                )
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
                    .fillMaxWidth(0.9f)
                    .wrapContentHeight()
            ) {
                Spacer(modifier = Modifier.height(10.dp))
                PasswordFieldType1(
                    label = "Mật khẩu cũ",
                    value = mkCuInput,
                    backgroundColor = WhiteDefault,
                    onValueChange = { mkCuInput = it },
                    modifier = Modifier.fillMaxWidth()
                )
                PasswordFieldType1(
                    label = "Mật khẩu mới",
                    value = mkMoiInput,
                    backgroundColor = WhiteDefault,
                    onValueChange = { mkMoiInput = it },
                    modifier = Modifier.fillMaxWidth()
                )
                PasswordFieldType1(
                    label = "Xác nhận mật khẩu mới",
                    value = mkMoiNhapLaiInput,
                    backgroundColor = WhiteDefault,
                    onValueChange = { mkMoiNhapLaiInput = it },
                    modifier = Modifier.fillMaxWidth()
                )
                OuterShadowFilledButton(
                    text = "Xác nhận",
                    onClick = {
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(40.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewChangePasswordScreen() {
    ChangePasswordScreen()
}