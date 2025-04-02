package com.example.mam.ui.theme

import android.app.Activity
import android.content.Intent
import android.graphics.BlurMaskFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mam.R

private val DarkColorScheme = darkColorScheme(
    primary = OrangeDefault,
    secondary = BrownDefault,
    tertiary = Momo,
    background = BlackDefault,
    surface = BrownDark,
    onPrimary = WhiteDefault,
    onSecondary = WhiteDefault,
    onBackground = GreyDefault,
    onSurface = WhiteDefault
)

private val LightColorScheme = lightColorScheme(
    primary = OrangeDefault,
    secondary = OrangeLight,
    tertiary = BrownLight,
    background = WhiteDefault,
    surface = GreyDefault,
    onPrimary = BlackDefault,
    onSecondary = BlackDefault,
    onBackground = BlackDefault,
    onSurface = BlackDefault

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

object Variables {
    val HeadlineMediumSize = 28.sp
    val HeadlineMediumLineHeight = 36.sp
}

@Composable
fun MAMTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
//Shadow
@Composable
fun Modifier.outerShadow(
    color: Color = Color.Black,
    bordersRadius: Dp = 0.dp,
    blurRadius: Dp = 0.dp,
    offsetY: Dp = 0.dp,
    offsetX: Dp = 0.dp,
    spread: Dp = 0f.dp,
    modifier: Modifier = Modifier
) = this.then(
    modifier.drawBehind {
        this.drawIntoCanvas {
            val paint = Paint()
            val frameworkPaint = paint.asFrameworkPaint()
            val spreadPixel = spread.toPx()
            val leftPixel = (0f - spreadPixel) + offsetX.toPx()
            val topPixel = (0f - spreadPixel) + offsetY.toPx()
            val rightPixel = (this.size.width + spreadPixel)
            val bottomPixel = (this.size.height + spreadPixel)

            if (blurRadius != 0.dp) {
                frameworkPaint.maskFilter =
                    (BlurMaskFilter(blurRadius.toPx(), BlurMaskFilter.Blur.NORMAL))
            }

            frameworkPaint.color = color.toArgb()
            it.drawRoundRect(
                left = leftPixel,
                top = topPixel,
                right = rightPixel,
                bottom = bottomPixel,
                radiusX = bordersRadius.toPx(),
                radiusY = bordersRadius.toPx(),
                paint
            )
        }
    }
)

@Composable
fun Modifier.innerShadow(
    color: Color = Color.Black,
    bordersRadius: Dp = 0.dp,
    blurRadius: Dp = 0.dp,
    offsetY: Dp = 0.dp,
    offsetX: Dp = 0.dp,
    spread: Dp = 0.dp
) = drawWithContent {

    drawContent()

    val rect = Rect(Offset.Zero, size)
    val paint = Paint()

    drawIntoCanvas {

        paint.color = color
        paint.isAntiAlias = true
        it.saveLayer(rect, paint)
        it.drawRoundRect(
            left = rect.left,
            top = rect.top,
            right = rect.right,
            bottom = rect.bottom,
            bordersRadius.toPx(),
            bordersRadius.toPx(),
            paint
        )
        val frameworkPaint = paint.asFrameworkPaint()
        frameworkPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
        if (blurRadius.toPx() > 0) {
            frameworkPaint.maskFilter = BlurMaskFilter(blurRadius.toPx(), BlurMaskFilter.Blur.NORMAL)
        }
        val left = if (offsetX > 0.dp) {
            rect.left + offsetX.toPx()
        } else {
            rect.left
        }
        val top = if (offsetY > 0.dp) {
            rect.top + offsetY.toPx()
        } else {
            rect.top
        }
        val right = if (offsetX < 0.dp) {
            rect.right + offsetX.toPx()
        } else {
            rect.right
        }
        val bottom = if (offsetY < 0.dp) {
            rect.bottom + offsetY.toPx()
        } else {
            rect.bottom
        }
        paint.color = Color.Black
        it.drawRoundRect(
            left = left + spread.toPx() / 2,
            top = top + spread.toPx() / 2,
            right = right - spread.toPx() / 2,
            bottom = bottom - spread.toPx() / 2,
            bordersRadius.toPx(),
            bordersRadius.toPx(),
            paint
        )
        frameworkPaint.xfermode = null
        frameworkPaint.maskFilter = null
    }
}
//end Shadow

//Component
@Composable
fun EditField(
    @StringRes label: Int,
    value: String,
    keyboardOptions: KeyboardOptions,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier) {
    OutlinedTextField(
        label = {
            Text(
                stringResource(label),
                color = BrownDefault,
                fontWeight = FontWeight.Bold)},
        value = value,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = OrangeLight,  // Màu nền khi focus
            unfocusedContainerColor = OrangeLight, // Màu nền khi không focus
            focusedIndicatorColor = BrownDefault,  // Màu viền khi focus
            unfocusedIndicatorColor = BrownDefault,  // Màu viền khi không focus
            focusedTextColor = BrownDefault,       // Màu chữ khi focus
            unfocusedTextColor = BrownDefault,      // Màu chữ khi không focus
            cursorColor = BrownDefault             // Màu con trỏ nhập liệu
        ),
        shape = RoundedCornerShape(20.dp),
        onValueChange = onValueChange,
        keyboardOptions = keyboardOptions,
        singleLine = true,
        modifier = modifier,
    )
}
@Composable
fun PasswordField(
    @StringRes label: Int,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var passwordVisible by remember { mutableStateOf(false) }
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                text = stringResource(label),
                color = BrownDefault,
                fontWeight = FontWeight.Bold
            )
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = OrangeLight,
            unfocusedContainerColor = OrangeLight,
            focusedIndicatorColor = BrownDefault,
            unfocusedIndicatorColor = BrownDefault,
            focusedTextColor = BrownDefault,
            unfocusedTextColor = BrownDefault,
            cursorColor = BrownDefault
        ),
        shape = RoundedCornerShape(20.dp),
        singleLine = true,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                    painter = if (passwordVisible) painterResource(R.drawable.eye_outline) else painterResource(R.drawable.eye_off_outline),
                    contentDescription = if (passwordVisible) "Hide password" else "Show password",
                    tint = BrownDefault
                )
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction =  ImeAction.Done),
        modifier = modifier
    )
}

@Composable
fun UnderlinedClickableText(
    text: String,
    targetActivity: Class<out ComponentActivity>,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val annotatedText = buildAnnotatedString {
        withStyle(style = SpanStyle(color = BrownDefault, textDecoration = TextDecoration.Underline)) {
            append(text)
        }
    }

    ClickableText(
        text = annotatedText,
        modifier = modifier,
        onClick = {
            val intent = Intent(context, targetActivity)
            context.startActivity(intent)
        }
    )
}
//end Component