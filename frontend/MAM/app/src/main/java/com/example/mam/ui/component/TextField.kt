package com.example.mam.ui.component

import androidx.annotation.StringRes
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.mam.R
import com.example.mam.ui.theme.BrownDefault
import com.example.mam.ui.theme.OrangeDefault
import com.example.mam.ui.theme.OrangeLight
import com.example.mam.ui.theme.OrangeLighter

@Composable
fun EditField(
    @StringRes label: Int,
    value: String,
    backgroundColor: Color = OrangeLighter.copy(alpha = 0f),
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
                stringResource(label),
                color = textColor,
                fontWeight = FontWeight.Bold)
        },
        value = value,
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
fun AnotherInputField(
    @StringRes label: Int,
    value: String,
    keyboardOptions: KeyboardOptions,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        label = {
            Text(
                stringResource(label),
                color = BrownDefault,
                fontWeight = FontWeight.Bold)
        },
        value = value,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = OrangeLight.copy(alpha = 0f),  // Màu nền khi focus
            unfocusedContainerColor = OrangeLight.copy(alpha = 0f), // Màu nền khi không focus
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
            focusedContainerColor = OrangeLight.copy(alpha = 0f),
            unfocusedContainerColor = OrangeLight.copy(alpha = 0f),
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
                    painter = if (passwordVisible) painterResource(R.drawable.ic_eye_outline) else painterResource(
                        R.drawable.ic_eye_off_outline),
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