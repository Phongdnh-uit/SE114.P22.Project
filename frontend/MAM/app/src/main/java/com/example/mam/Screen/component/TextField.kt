package com.example.mam.Screen.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
import com.example.mam.ui.theme.BrownDefaults
import com.example.mam.ui.theme.Transparent
import com.example.mam.ui.theme.Variables
import com.example.mam.ui.theme.WhiteDefault

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
    }
}

@Composable
fun PasswordFieldType1(
    label: String,
    value: String,
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