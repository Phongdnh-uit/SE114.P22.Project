package com.example.mam

import android.graphics.BlurMaskFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mam.ui.theme.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MAMTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    SignInScreen()
                }
            }
        }
    }
}

@Composable
fun SignInScreen(modifier: Modifier = Modifier) {
    var sdtInput by remember { mutableStateOf("") }
    val sdt = sdtInput
    var mkInput by remember { mutableStateOf("") }
    val mk = mkInput

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(color = OrangeDefault)
    ) {
        Image(
            painter = painterResource(id = R.drawable.mam_ffffff),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = stringResource(R.string.dang_nhap),
            style = TextStyle(
                fontSize = Variables.HeadlineMediumSize,
                lineHeight = Variables.HeadlineMediumLineHeight,
                fontWeight = FontWeight(700),
                color = WhiteDefault,
                textAlign = TextAlign.Center,
            )
        )
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .outerShadow(
                    color = BlackDefault,
                    bordersRadius = 50.dp,
                    blurRadius = 10.dp,
                    offsetX = 0.dp,
                    offsetY = (-2).dp,
                    spread = 0.dp,
                )
                .fillMaxWidth()
                .height(330.dp)
                .background(
                    color = OrangeLight,
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
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight()
            ) {
                EditField(
                    label = R.string.so_dien_thoai,
                    value = sdtInput,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    onValueChange = { sdtInput = it },
                    modifier = Modifier
                )
                PasswordField(
                    label = R.string.mat_khau,
                    value = mkInput,
                    onValueChange = { mkInput = it },
                    modifier = Modifier
                )
                UnderlinedClickableText(
                    text = "Đổi mật khẩu",
                    targetActivity = MainActivity::class.java,

                    )
            }
            Spacer(Modifier.height(10.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {
        SignInScreen()
}
