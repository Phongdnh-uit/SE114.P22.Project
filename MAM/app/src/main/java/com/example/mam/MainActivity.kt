package com.example.mam

import android.graphics.BlurMaskFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mam.ui.theme.MAMTheme
import com.example.mam.ui.theme.Variables
import com.example.mam.ui.theme.innerShadow
import com.example.mam.ui.theme.outerShadow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MAMTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Greeting()
                }
            }
        }
    }
}

@Composable
fun Greeting(modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(R.color.orange_default))
    ) {
        Image(
            painter = painterResource(id = R.drawable.mam_ffffff),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "Đăng Nhập",
            style = TextStyle(
                fontSize = Variables.HeadlineMediumSize,
                lineHeight = Variables.HeadlineMediumLineHeight,
                fontWeight = FontWeight(700),
                color = colorResource(R.color.white_default),
                textAlign = TextAlign.Center,
            )
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .outerShadow(
                    color = colorResource(R.color.black_default),
                    bordersRadius = 50.dp,
                    blurRadius = 10.dp,
                    offsetX = 0.dp,
                    offsetY = -2.dp,
                    spread = 0.dp,
                )
                .fillMaxWidth()
                .height(330.dp)
                .background(
                    color = colorResource(R.color.orange_light),
                    shape = RoundedCornerShape(
                        topStart = 50.dp,
                        topEnd = 50.dp,
                        bottomStart = 0.dp,
                        bottomEnd = 0.dp
                    )
                )
        ) {
            // Child views.
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
        Greeting()
}
