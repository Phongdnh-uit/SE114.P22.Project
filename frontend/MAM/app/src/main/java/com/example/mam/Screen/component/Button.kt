package com.example.mam.Screen.component

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposableTarget
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mam.R
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
    color: Color = BrownDefault,
    targetActivity: Class<out ComponentActivity>,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val annotatedText = buildAnnotatedString {
        withStyle(style = SpanStyle(color = color, textDecoration = TextDecoration.Underline, fontWeight = FontWeight.Bold)) {
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
    icon: ImageVector?= null,
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
                imageVector = icon,
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
    fontSize: TextUnit = 16.sp,
    borderStroke: BorderStroke = BorderStroke(width = 1.dp, color = BrownDark),
    shadowColor: Color = BrownDark,
    blurRadius: Dp = 4.dp,
    offsetX: Dp = 0.dp,
    offsetY: Dp = 4.dp,
    spread: Dp = 0.dp,
    @DrawableRes icon: Int?= null,
    onClick: () -> Unit,
    modifier: Modifier
){
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed = interactionSource.collectIsPressedAsState().value
    Button(
        colors = ButtonDefaults.buttonColors(containerColor = OrangeDefault),
        border = borderStroke,
        onClick = onClick,
        modifier = modifier.then(
            if (!isPressed) Modifier.outerShadow(
                color = shadowColor,
                bordersRadius = 25.dp,
                blurRadius = blurRadius,
                offsetX = offsetX,
                offsetY = offsetY,
                spread = spread,
            ) else Modifier
        ),
        interactionSource = interactionSource,
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp)
    ){
        icon?.let {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = WhiteDefault,
                modifier = Modifier.size(30.dp).padding(end = 8.dp)
            )
        }
        Text(
            text = text,
            fontSize = fontSize,
            color = WhiteDefault
        )
    }
}

@Composable
fun BasicOutlinedButton(
    text: String,
    icon: ImageVector ?= null,
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
                imageVector = icon,
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

@Composable
fun CircleIconButton(
    backgroundColor: Color ?= null,
    foregroundColor: Color ?= null,
    icon: ImageVector,
    shadow: String ?= null,
    onClick: () -> Unit,
    modifier: Modifier,
){
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed = interactionSource.collectIsPressedAsState().value
    IconButton (
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = backgroundColor ?: OrangeDefault
        ),
        onClick = onClick,
        modifier = modifier.size(50.dp)
            .then(
                if (shadow.equals("inner")){
                    Modifier.innerShadow(
                        color = GreyDark,
                        bordersRadius = 25.dp,
                        blurRadius = 4.dp,
                        offsetX = 0.dp,
                        offsetY = 4.dp,
                        spread = 0.dp,
                    ).clip(RoundedCornerShape(25.dp))
                }
                else if(shadow.equals("outer")){
                    Modifier.outerShadow(
                        color = GreyDark,
                        bordersRadius = 25.dp,
                        blurRadius = 4.dp,
                        offsetX = 0.dp,
                        offsetY = 4.dp,
                        spread = 0.dp,
                    )
                }
                else Modifier
            ),
        interactionSource = interactionSource,
    ){
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = foregroundColor ?: WhiteDefault,
            modifier = Modifier.size(40.dp)
        )
    }
}

@Composable
fun QuantitySelectionButton(
    count: Int,
    onValueChange: (Int) -> Unit // Callback để cập nhật giá trị bên ngoài
) {
    Box(modifier = Modifier
        .outerShadow() // Đổ bóng lên nền
        .background(OrangeDefault, shape = RoundedCornerShape(50))
        .padding(0.dp)) {
        Row(
            modifier = Modifier
                .padding(0.dp)
                .wrapContentWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Nút Giảm (-)
            CircleIconButton(
                icon = Icons.Filled.Remove, // Thay bằng icon phù hợp
                onClick = { if (count > 0) onValueChange(count - 1) },
                modifier = Modifier
            )

            // Hiển thị số lượng
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .background(Color.White, shape = RoundedCornerShape(50))
                    .wrapContentWidth()
                    .padding(0.dp)
                    .height(50.dp)
            ){
                Text(
                    text = "$count",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = OrangeDefault,
                    modifier = Modifier
                        .padding(10.dp)
                        .wrapContentWidth()
                        .defaultMinSize(minWidth = 30.dp)
                )
            }
            // Nút Tăng (+)
            CircleIconButton(
                icon = Icons.Filled.Add, // Thay bằng icon phù hợp
                onClick = { onValueChange(count + 1) },
                modifier = Modifier
            )
        }
    }
}