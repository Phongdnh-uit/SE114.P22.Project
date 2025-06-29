package com.example.mam.gui.component

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation.Companion.keyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheetDefaults.properties
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mam.data.UserPreferencesRepository
import com.example.mam.dto.review.ReviewRequest
import com.example.mam.ui.theme.BrownDefault
import com.example.mam.ui.theme.ErrorColor
import com.example.mam.ui.theme.GreyDefault
import com.example.mam.ui.theme.GreyLight
import com.example.mam.ui.theme.OrangeDefault
import com.example.mam.ui.theme.OrangeLighter
import com.example.mam.ui.theme.WhiteDefault
import com.example.mam.viewmodel.authentication.StartViewModel
import okhttp3.EventListener

@Composable
fun CustomDialog(
    title: String,
    message: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    isHavingCancelButton: Boolean = true,
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = WhiteDefault,
            contentColor = BrownDefault,
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(20.dp).fillMaxWidth()) {
                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth())
                HorizontalDivider(
                    thickness = 1.dp,
                )
                Text(
                    text = message,
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.padding(horizontal = 10.dp).fillMaxWidth()
                ) {
                    OuterShadowFilledButton(
                        text = "Xác nhận",
                        onClick = onConfirm,
                        modifier = Modifier
                            .weight(0.5f)
                            .height(40.dp),
                    )

                    if (isHavingCancelButton) {
                        Spacer(modifier = Modifier.width(10.dp))
                        OuterShadowFilledButton(
                            text = "Từ chối",
                            onClick = onDismiss,
                            modifier = Modifier
                                .weight(0.5f)
                                .height(40.dp),
                        )
                    }


                }

            }
        }
    }
}

@Composable
fun SignUpDialog(
    title: String,
    message: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    viewModel: StartViewModel?
) {
    val phoneNumber = viewModel?.phoneNumber?.collectAsStateWithLifecycle()?.value
    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = WhiteDefault,
            contentColor = BrownDefault,
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(20.dp).fillMaxWidth()) {
                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth())
                HorizontalDivider(
                    thickness = 1.dp,
                )
                Text(
                    text = message,
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = phoneNumber?:"",
                    onValueChange = {
                        viewModel?.setPhoneNumber(it)
                    },
                    textStyle = TextStyle(
                        color = BrownDefault,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BrownDefault,
                        unfocusedBorderColor = GreyDefault,
                    ),
                    singleLine = true,
                    label = {
                        Text(
                            text = "Số điện thoại",
                            color = BrownDefault,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier
                        )
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)

                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.padding(horizontal = 10.dp).fillMaxWidth()
                ) {
                    OuterShadowFilledButton(
                        text = "Xác nhận",
                        onClick = onConfirm,
                        modifier = Modifier
                            .weight(0.5f)
                            .height(40.dp),
                    )

                    Spacer(modifier = Modifier.width(10.dp))
                    OuterShadowFilledButton(
                        text = "Thoát",
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(0.5f)
                            .height(40.dp),
                    )
                }
            }
        }
    }
}

@Composable
fun LoadingAlertDialog() {
    AlertDialog(
        onDismissRequest = {},
        title = null,
        text = {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
            ) {
                CircularProgressIndicator(
                    color = OrangeDefault,
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth(0.4f)
        ,
        confirmButton = {},
        dismissButton = {},
        properties = DialogProperties(dismissOnClickOutside = false)
    )
}

@Composable
fun OrderRatingDialog(
    orderId: Long,
    onSubmit: (ReviewRequest) -> Unit,
    onDismiss: () -> Unit
) {
    var rating by remember { mutableStateOf(0) }
    var comment by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Đánh giá đơn hàng",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        containerColor = WhiteDefault,
        textContentColor = BrownDefault,
        titleContentColor = BrownDefault,
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.Center),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HorizontalDivider(
                    thickness = 1.dp,
                )
                Text("Bạn đánh giá đơn hàng này như thế nào?",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    modifier = Modifier.padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    repeat(5) { index ->
                        Icon(
                            imageVector = if (index < rating) Icons.Filled.Star else Icons.Outlined.StarBorder,
                            contentDescription = null,
                            tint = OrangeDefault,
                            modifier = Modifier
                                .size(32.dp)
                                .clickable { rating = index + 1 }
                        )
                    }
                }
                OutlinedTextField(
                    value = comment,
                    onValueChange = { comment = it },
                    textStyle = TextStyle(
                        color = BrownDefault,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BrownDefault,
                        unfocusedBorderColor = GreyDefault,
                    ),
                    label = {
                        Text(
                            text = "Nhận xét",
                            color = BrownDefault,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier
                        )
                    },
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    maxLines = 5,
                )
            }
        },
        confirmButton = {
            TextButton(
                colors = ButtonDefaults.textButtonColors(
                    contentColor = OrangeDefault,
                    disabledContentColor = GreyDefault.copy(alpha = 0.5f)
                ),
                onClick = {
                    onSubmit(
                        ReviewRequest(
                            orderId = orderId,
                            rate = rating,
                            content = comment
                        )
                    )
                },
                enabled = rating > 0
            ) {
                Text("Gửi")
            }
        },
        dismissButton = {
            TextButton(
                colors = ButtonDefaults.textButtonColors(
                    contentColor = OrangeDefault,
                    disabledContentColor = GreyDefault.copy(alpha = 0.5f)
                ),
                onClick = onDismiss) {
                Text("Huỷ")
            }
        }
    )
}

@Preview
@Composable
fun PreviewDialog() {
    Column {
        OrderRatingDialog(
            orderId = 12345L,
            onSubmit = { reviewRequest ->
                // Handle review submission
                println("Review submitted: $reviewRequest")
            },
            onDismiss = {}
        )
//        CustomDialog(
//            title = "Xác nhận",
//            message = "Bạn có chắc chắn muốn thực hiện hành động này?",
//            onDismiss = {},
//            onConfirm = {},
//            isHavingCancelButton = true
//        )
//        SignUpDialog(
//            title = "Đăng ký",
//            message = "Vui lòng nhập số điện thoại của bạn để đăng ký.",
//            onDismiss = {},
//            onConfirm = {},
//            viewModel = null
//        )
        //LoadingAlertDialog()

    }
}