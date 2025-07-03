package com.example.mam.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.mam.R
// Set of Material typography styles to start with
val Typography = Typography(
    titleLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp,
        textAlign = TextAlign.Center,
        color = WhiteDefault
    ),
)