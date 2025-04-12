package com.example.mam.Screen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.mam.ui.theme.BrownDark
import com.example.mam.ui.theme.BrownDefault
import com.example.mam.ui.theme.ErrorColor
import com.example.mam.ui.theme.GreyDark
import com.example.mam.ui.theme.OrangeDefault
import com.example.mam.ui.theme.OrangeLight
import com.example.mam.ui.theme.OrangeLighter
import com.example.mam.ui.theme.Transparent
import com.example.mam.ui.theme.Variables
import com.example.mam.ui.theme.WhiteDefault
import kotlinx.coroutines.delay
import kotlin.math.sin

@Composable
fun EditField(
    label: String,
    value: String,
    backgroundColor: Color = Transparent,
    foregroundColor: Color = BrownDefault,
    textColor: Color = BrownDefault,
    radius: Dp = 20.dp,
    keyboardOptions: KeyboardOptions,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        label = {
            Text(
                label,
                color = textColor,
                fontWeight = FontWeight.Bold)
        },
        value = value,
        textStyle = TextStyle(
            fontSize = Variables.BodySizeMedium, // <-- Chỉnh font size ở đây
            color = textColor // Đảm bảo màu chữ vẫn đúng
        ),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = backgroundColor,  // Màu nền khi focus
            unfocusedContainerColor = backgroundColor, // Màu nền khi không focus
            focusedIndicatorColor = foregroundColor,  // Màu viền khi focus
            unfocusedIndicatorColor = foregroundColor,  // Màu viền khi không focus
            focusedTextColor = textColor,       // Màu chữ khi focus
            unfocusedTextColor = textColor,      // Màu chữ khi không focus
            cursorColor = textColor             // Màu con trỏ nhập liệu
        ),
        shape = RoundedCornerShape(radius),
        onValueChange = onValueChange,
        keyboardOptions = keyboardOptions,
        singleLine = true,
        modifier = modifier,
    )
}

@Composable
fun EditFieldType1(
    label: String,
    subLabel: String = "",
    errorLabel: String = "",
    value: String,
    backgroundColor: Color = WhiteDefault,
    foregroundColor: Color = BrownDefault,
    textColor: Color = BrownDefault,
    radius: Dp = 20.dp,
    keyboardOptions: KeyboardOptions,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier){
        Text(
            label,
            style = TextStyle(
                fontSize = Variables.BodySizeMedium,
                fontWeight = FontWeight(Variables.BodyFontWeightRegular),
                color = textColor,
                textAlign = TextAlign.Start
            ),
            modifier = Modifier.fillMaxWidth()
        )
        if(!subLabel.isEmpty()){
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                subLabel,
                style = TextStyle(
                    fontSize = Variables.BodySizeSmall,
                    fontWeight = FontWeight(Variables.BodyFontWeightRegular),
                    color = GreyDark,
                    textAlign = TextAlign.Start
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = value,
            textStyle = TextStyle(
                fontSize = Variables.BodySizeMedium, // <-- Chỉnh font size ở đây
                color = textColor // Đảm bảo màu chữ vẫn đúng
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = backgroundColor,  // Màu nền khi focus
                unfocusedContainerColor = backgroundColor, // Màu nền khi không focus
                focusedIndicatorColor = foregroundColor,  // Màu viền khi focus
                unfocusedIndicatorColor = foregroundColor,  // Màu viền khi không focus
                focusedTextColor = textColor,       // Màu chữ khi focus
                unfocusedTextColor = textColor,      // Màu chữ khi không focus
                cursorColor = textColor             // Màu con trỏ nhập liệu
            ),
            shape = RoundedCornerShape(radius),
            onValueChange = onValueChange,
            keyboardOptions = keyboardOptions,
            singleLine = true,
            modifier = modifier
                .fillMaxWidth()
        )
        if(!errorLabel.isEmpty()){
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                errorLabel,
                style = TextStyle(
                    fontSize = Variables.BodySizeSmall,
                    fontWeight = FontWeight(Variables.BodyFontWeightRegular),
                    color = ErrorColor,
                    textAlign = TextAlign.Start
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun PasswordFieldType1(
    label: String,
    value: String,
    subLabel: String = "",
    errorLabel: String = "",
    backgroundColor: Color = WhiteDefault,
    foregroundColor: Color = BrownDefault,
    textColor: Color = BrownDefault,
    radius: Dp = 20.dp,
    imeAction: ImeAction = ImeAction.Next,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            label,
            style = TextStyle(
                fontSize = Variables.BodySizeMedium,
                fontWeight = FontWeight(Variables.BodyFontWeightRegular),
                color = textColor,
                textAlign = TextAlign.Start
            ),
            modifier = Modifier.fillMaxWidth()
        )
        if(!subLabel.isEmpty()){
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                subLabel,
                style = TextStyle(
                    fontSize = Variables.BodySizeSmall,
                    fontWeight = FontWeight(Variables.BodyFontWeightRegular),
                    color = GreyDark,
                    textAlign = TextAlign.Start
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = TextStyle(
                fontSize = Variables.BodySizeMedium, // <-- Chỉnh font size ở đây
                color = textColor // Đảm bảo màu chữ vẫn đúng
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = backgroundColor,  // Màu nền khi focus
                unfocusedContainerColor = backgroundColor, // Màu nền khi không focus
                focusedIndicatorColor = foregroundColor,  // Màu viền khi focus
                unfocusedIndicatorColor = foregroundColor,  // Màu viền khi không focus
                focusedTextColor = textColor,       // Màu chữ khi focus
                unfocusedTextColor = textColor,      // Màu chữ khi không focus
                cursorColor = textColor
            ),
            shape = RoundedCornerShape(radius),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction =  imeAction),
            modifier = modifier
                .fillMaxWidth()
        )
        if(!errorLabel.isEmpty()){
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                errorLabel,
                style = TextStyle(
                    fontSize = Variables.BodySizeSmall,
                    fontWeight = FontWeight(Variables.BodyFontWeightRegular),
                    color = ErrorColor,
                    textAlign = TextAlign.Start
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun PasswordField(
    label: String,
    value: String,
    backgroundColor: Color = Transparent,
    foregroundColor: Color = BrownDefault,
    textColor: Color = BrownDefault,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var passwordVisible by remember { mutableStateOf(false) }
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = TextStyle(
            fontSize = Variables.BodySizeMedium, // <-- Chỉnh font size ở đây
            color = textColor // Đảm bảo màu chữ vẫn đúng
        ),
        label = {
            Text(
                text = label,
                color = textColor,
                fontWeight = FontWeight.Bold
            )
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = backgroundColor,  // Màu nền khi focus
            unfocusedContainerColor = backgroundColor, // Màu nền khi không focus
            focusedIndicatorColor = foregroundColor,  // Màu viền khi focus
            unfocusedIndicatorColor = foregroundColor,  // Màu viền khi không focus
            focusedTextColor = textColor,       // Màu chữ khi focus
            unfocusedTextColor = textColor,      // Màu chữ khi không focus
            cursorColor = textColor
        ),
        shape = RoundedCornerShape(20.dp),
        singleLine = true,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                    imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                    contentDescription = if (passwordVisible) "Hide password" else "Show password",
                    tint = textColor
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
fun OtpInputField(
    otpLength: Int = 4,
    onOtpComplete: (String) -> Unit
) {
    val otpValues = remember { mutableStateListOf(*Array(otpLength) { "" }) }
    val focusRequesters = remember { List(otpLength) { FocusRequester() } }

    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.padding(vertical = 5.dp)
    ) {
        for (i in 0 until otpLength) {
            BasicTextField(
                value = otpValues[i],
                onValueChange = { value ->
                    if (value.length <= 1 && value.all { it.isDigit() }) {
                        otpValues[i] = value
                        if (value.isNotEmpty() && i < otpLength - 1) {
                            focusRequesters[i + 1].requestFocus()
                        }

                        // Khi nhập đủ
                        if (otpValues.all { it.length == 1 }) {
                            onOtpComplete(otpValues.joinToString(""))
                        }
                    }
                },
                modifier = Modifier
                    .size(48.dp)
                    .border(2.dp, Color(0xFFFF7A00), CircleShape)
                    .background(Color(0xFFFFF6ED), CircleShape)
                    .focusRequester(focusRequesters[i])
                    .focusable(),
                textStyle = TextStyle(
                    fontSize = Variables.BodySizeMedium,
                    textAlign = TextAlign.Center,
                    color = Color.Black
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                singleLine = true,
                decorationBox = { innerTextField ->
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        innerTextField()
                    }
                }
            )
        }
    }
    // Focus vào ô đầu tiên khi màn hình hiển thị
    LaunchedEffect(Unit) {
        delay(300)
        focusRequesters[0].requestFocus()
    }
}
