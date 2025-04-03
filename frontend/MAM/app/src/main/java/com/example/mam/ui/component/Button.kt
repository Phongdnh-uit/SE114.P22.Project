package com.example.mam.ui.component

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.mam.ui.theme.BlackDefault
import com.example.mam.ui.theme.BrownDark
import com.example.mam.ui.theme.BrownDefault
import com.example.mam.ui.theme.GreyDark
import com.example.mam.ui.theme.GreyDefault
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
        modifier = modifier
            .then(
            if (!isPressed) Modifier.innerShadow(
                color = GreyDark,
                bordersRadius = 25.dp,
                blurRadius = 4.dp,
                offsetX = 0.dp,
                offsetY = 4.dp,
                spread = 0.dp,
            )
                .clip(RoundedCornerShape(25.dp))
            else Modifier
        ),
        interactionSource = interactionSource,
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp)
    ){
        icon?.let {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = WhiteDefault,
                modifier = Modifier.size(30.dp).padding(end = 8.dp)
            )
        }
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
                color = GreyDark,
                bordersRadius = 25.dp,
                blurRadius = 4.dp,
                offsetX = 0.dp,
                offsetY = (4).dp,
                spread = 0.dp,
            ) else Modifier
        ),
        interactionSource = interactionSource,
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp)
    ){
        icon?.let {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = WhiteDefault,
                modifier = Modifier.size(30.dp).padding(end = 8.dp)
            )
        }
        Text(
            text = text,
            color = WhiteDefault
        )
    }
}
@Composable
fun BasicOutlinedButton(
    text: String,
    @DrawableRes icon: Int?= null,
    onClick: () -> Unit,
    modifier: Modifier
){
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed = interactionSource.collectIsPressedAsState().value
    OutlinedButton(
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        border = BorderStroke( width = 2.dp, color = BrownDefault),
        onClick = onClick,
        modifier = modifier,
        interactionSource = interactionSource,
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp)
    ){
        icon?.let {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = BrownDefault,
                modifier = Modifier.size(30.dp).padding(end = 8.dp)
            )
        }
        Text(
            text = text,
            color = BrownDefault
        )
    }
}