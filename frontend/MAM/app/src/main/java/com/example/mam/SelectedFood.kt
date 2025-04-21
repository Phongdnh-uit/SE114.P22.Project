package com.example.mam

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mam.ui.theme.MAMTheme

class SelectedFood : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MAMTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    SelectedFoodScreen()
                }
            }
        }
    }
}

@Composable
fun SelectedFoodScreen(modifier: Modifier = Modifier){
    Box(modifier = Modifier.fillMaxSize()){
        Image(
            painter = painterResource(id = com.example.mam.R.drawable.bacon_and_cheese_heaven),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(373.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 280.dp),
            verticalArrangement = Arrangement.Top
        ){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                    .background(Color(0xFFFFE4C7)) // Màu nền của phần thông tin
                    .padding(16.dp)
            ){
                Text(text = "Thông tin món ăn", style = MaterialTheme.typography.headlineSmall)
            }
        }
    }
}
@Preview
@Composable
fun PreviewSelectedFoodScreen(){
    SelectedFoodScreen()
}