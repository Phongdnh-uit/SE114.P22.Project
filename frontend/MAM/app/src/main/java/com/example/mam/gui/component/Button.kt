package com.example.mam.gui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mam.ui.theme.BrownDark
import com.example.mam.ui.theme.BrownDefault
import com.example.mam.ui.theme.GreyDark
import com.example.mam.ui.theme.OrangeDefault
import com.example.mam.ui.theme.Transparent
import com.example.mam.ui.theme.WhiteDefault

@Composable
fun UnderlinedClickableText(
    text: String = "",
    link: String,
    color: Color = BrownDefault,
    linkColor: Color = OrangeDefault,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val annotatedText = buildAnnotatedString {
        withStyle(style = SpanStyle(color = color)) {
            append(text)
        }
        append("   ")
        // Đánh dấu phần cần click
        pushStringAnnotation(tag = "CLICK", annotation = "clicked")
        withStyle(style = SpanStyle(
            color = linkColor,
            textDecoration = TextDecoration.Underline,
            fontWeight = FontWeight.Bold
        )) {
            append(link)
        }
        pop()
    }


    ClickableText(
        text = annotatedText,
        style = TextStyle(
            textAlign = TextAlign.Center),
        onClick = { offset ->
            annotatedText.getStringAnnotations(tag = "CLICK", start = offset, end = offset)
                .firstOrNull()?.let {
                    onClick()
                }
        },
        modifier = modifier

        // style mặc định cho đoạn text
    )
}

@Composable
fun InnerShadowFilledButton(
    text: String,
    icon: ImageVector?= null,
    image: Int ?= null,
    isEnable: Boolean = true,
    onClick: () -> Unit,
    modifier: Modifier
){
    val isPressed = remember { mutableStateOf(false) }
    Button(
        colors = ButtonDefaults.buttonColors(containerColor = OrangeDefault),
        enabled = isEnable,
        onClick = {
            isPressed.value = true
            onClick()
        },
        modifier = modifier
            .bounceClick()
            .then(
            if (isEnable) Modifier
                .innerShadow(
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
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp)
    ){
        icon?.let {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = WhiteDefault,
                modifier = Modifier
                    .size(30.dp)
                    .padding(end = 8.dp)
            )
        }
        image?.let {
            Icon(
                painter = painterResource(image),
                contentDescription = null,
                modifier = Modifier
                    .size(30.dp)
                    .padding(end = 8.dp)
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
    isEnable: Boolean = true,
    //borderStroke: BorderStroke = BorderStroke(width = 1.dp, color = BrownDark),
    shadowColor: Color = BrownDark,
    blurRadius: Dp = 4.dp,
    offsetX: Dp = 0.dp,
    offsetY: Dp = 4.dp,
    spread: Dp = 0.dp,
    icon: ImageVector ?= null,
    image: Int ?= null,
    onClick: () -> Unit,
    modifier: Modifier
){
//    val isPressed = remember { mutableStateOf(false) }
    Button(
        colors = ButtonDefaults.buttonColors(containerColor = OrangeDefault),
        //border = borderStroke,
        onClick = onClick,
        enabled = isEnable,
        modifier = modifier
            .then(
            if (isEnable) Modifier
                .bounceClick()
                .outerShadow(
                color = shadowColor,
                bordersRadius = 25.dp,
                blurRadius = blurRadius,
                offsetX = offsetX,
                offsetY = offsetY,
                spread = spread,
            ) else modifier
        ),
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp)
    ){
        icon?.let {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = WhiteDefault,
                modifier = Modifier
                    .size(30.dp)
                    .padding(end = 8.dp)
            )
        }
        image?.let {
            Icon(
                painter = painterResource(image),
                contentDescription = null,
                modifier = Modifier
                    .size(30.dp)
                    .padding(end = 8.dp)
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
    foregroundColor: Color = BrownDefault,
    icon: ImageVector ?= null,
    image: Int ?= null,
    isEnable: Boolean = true,
    onClick: () -> Unit,
    modifier: Modifier
){
    val bordercolor = if (isEnable) foregroundColor else WhiteDefault
    OutlinedButton(
        colors = ButtonDefaults.buttonColors(containerColor = Transparent),
        border = BorderStroke( width = 2.dp, color = bordercolor),
        enabled = isEnable,
        onClick = onClick,
        modifier = modifier.then(
            if(isEnable) Modifier.bounceClick()
            else Modifier
        ),
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp),
//        interactionSource = interactionSource
    ){
        icon?.let {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = BrownDefault,
                modifier = Modifier
                    .size(30.dp)
                    .padding(end = 8.dp)
            )
        }
        image?.let {
            Icon(
                painter = painterResource(image),
                contentDescription = null,
                modifier = Modifier
                    .size(30.dp)
                    .padding(end = 8.dp)
            )
        }
        Text(
            text = text,
            color = foregroundColor
        )
    }
}

@Composable
fun CircleIconButton(
    backgroundColor: Color ?= null,
    foregroundColor: Color ?= null,
    icon: ImageVector,
    isEnable: Boolean = true,
    shadow: String ?= null,
    onClick: () -> Unit,
    modifier: Modifier,
){
    IconButton (
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = backgroundColor ?: OrangeDefault
        ),
        enabled = isEnable,
        onClick = onClick,
        modifier = modifier
            .size(50.dp)
            .focusable(false)
            .then(
                if (isEnable) Modifier.bounceClick()
                else Modifier
            )
            .size(50.dp)
            .then(
                if (shadow.equals("inner")) {
                    Modifier
                        .innerShadow(
                            color = GreyDark,
                            bordersRadius = 25.dp,
                            blurRadius = 4.dp,
                            offsetX = 0.dp,
                            offsetY = 4.dp,
                            spread = 0.dp,
                        )
                        .clip(RoundedCornerShape(25.dp))
                } else if (shadow.equals("outer")) {
                    Modifier.outerShadow(
                        color = GreyDark,
                        bordersRadius = 25.dp,
                        blurRadius = 4.dp,
                        offsetX = 0.dp,
                        offsetY = 4.dp,
                        spread = 0.dp,
                    )
                } else Modifier
            ),
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
    onValueChange: (Int) -> Unit = {},
    modifier: Modifier = Modifier// Callback để cập nhật giá trị bên ngoài
) {
    Box(modifier = modifier
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