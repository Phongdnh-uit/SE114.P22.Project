package com.example.mam.gui.screen.client

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mam.gui.component.EditFieldType1
import com.example.mam.gui.component.OuterShadowFilledButton
import com.example.mam.gui.component.outerShadow
import com.example.mam.ui.theme.BrownDefault
import com.example.mam.ui.theme.GreyDark
import com.example.mam.ui.theme.OrangeLighter
import com.example.mam.ui.theme.WhiteDefault

@Composable
fun MapScreen(){
    Box(

    ){
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .outerShadow(
                    color = GreyDark,
                    bordersRadius = 50.dp,
                    blurRadius = 4.dp,
                    offsetX = 0.dp,
                    offsetY = -4.dp,
                )
                .background(
                    color = OrangeLighter,
                    shape = RoundedCornerShape(
                        topStart = 50.dp,
                        topEnd = 50.dp,
                        bottomStart = 0.dp,
                        bottomEnd = 0.dp
                    )
                )
                .fillMaxWidth()
                .wrapContentHeight()
                .clip(
                    shape = RoundedCornerShape(
                        topStart = 50.dp,
                        topEnd = 50.dp,
                        bottomStart = 0.dp,
                        bottomEnd = 0.dp
                    )
                )
                .align(Alignment.BottomCenter)
        ){
            Text(
                text = "Địa chỉ:",
                fontSize = 20.sp,
                color = BrownDefault,
                textAlign = TextAlign.Start,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(top = 20.dp).fillMaxWidth(0.9f)
            )
            OutlinedTextField(
                value = "",
                onValueChange = {  },
                textStyle = TextStyle(fontSize = 20.sp, color = BrownDefault),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = WhiteDefault,  // Màu nền khi focus
                    unfocusedContainerColor = WhiteDefault, // Màu nền khi không focus
                    focusedIndicatorColor = BrownDefault,  // Màu viền khi focus
                    unfocusedIndicatorColor = BrownDefault,  // Màu viền khi không focus
                    focusedTextColor = BrownDefault,       // Màu chữ khi focus
                    unfocusedTextColor = BrownDefault,      // Màu chữ khi không focus
                    cursorColor = BrownDefault             // Màu con trỏ nhập liệu
                ),
                shape = RoundedCornerShape(10.dp),
                singleLine = true,
                modifier = Modifier.padding(2.dp).fillMaxWidth(0.9f)
            )
            OuterShadowFilledButton(
                text = "Áp dụng",
                fontSize = 18.sp,
                onClick = {  },
                modifier = Modifier.fillMaxWidth(0.9f). padding(bottom = 10.dp)

            )

        }
    }
}

@Preview
@Composable
fun MapScreenPreview(){
    MapScreen()
}