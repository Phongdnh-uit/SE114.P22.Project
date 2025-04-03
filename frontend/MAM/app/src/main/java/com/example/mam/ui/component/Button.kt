package com.example.mam.ui.component

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.annotation.DrawableRes
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.mam.ui.theme.BlackDefault
import com.example.mam.ui.theme.BrownDefault
import com.example.mam.ui.theme.OrangeDefault
import com.example.mam.ui.theme.WhiteDefault

@Composable
fun UnderlinedClickableText(
    text: String,
    targetActivity: Class<out ComponentActivity>,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val annotatedText = buildAnnotatedString {
        withStyle(style = SpanStyle(color = BrownDefault, textDecoration = TextDecoration.Underline, fontWeight = FontWeight.Bold)) {
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

@Composable
fun InnerShadowFilledButton(
    text: String,
    @DrawableRes icon: Int?= null,
    onClick: () -> Unit,
    modifier: Modifier
){
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed = interactionSource.collectIsPressedAsState().value
    Button(
        colors = ButtonDefaults.buttonColors(containerColor = OrangeDefault),
        onClick = onClick,
        modifier = modifier.then(
            if (!isPressed) Modifier.innerShadow(
                color = BlackDefault,
                bordersRadius = 25.dp,
                blurRadius = 4.dp,
                offsetX = 0.dp,
                offsetY = (4).dp,
                spread = 0.dp,
            ) else Modifier
        ),
        interactionSource = interactionSource
    ){
        Text(
            text = text,
            color = WhiteDefault
        )
    }
}
@Composable
fun OuterShadowFilledButton(
    text: String,
    @DrawableRes icon: Int?= null,
    onClick: () -> Unit,
    modifier: Modifier
){
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed = interactionSource.collectIsPressedAsState().value
    Button(
        colors = ButtonDefaults.buttonColors(containerColor = OrangeDefault),
        onClick = onClick,
        modifier = modifier.then(
            if (!isPressed) Modifier.outerShadow(
                color = BlackDefault,
                bordersRadius = 25.dp,
                blurRadius = 4.dp,
                offsetX = 0.dp,
                offsetY = (4).dp,
                spread = 0.dp,
            ) else Modifier
        ),
        interactionSource = interactionSource
    ){
        Text(
            text = text,
            color = WhiteDefault
        )
    }
}