package com.example.mam.gui.screen.management

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mam.gui.component.CircleIconButton
import com.example.mam.gui.component.CustomDialog
import com.example.mam.gui.component.outerShadow
import com.example.mam.ui.theme.BrownDefault
import com.example.mam.ui.theme.ErrorColor
import com.example.mam.ui.theme.GreenDefault
import com.example.mam.ui.theme.GreyDark
import com.example.mam.ui.theme.GreyDefault
import com.example.mam.ui.theme.OrangeDefault
import com.example.mam.ui.theme.OrangeLighter
import com.example.mam.ui.theme.Typography
import com.example.mam.ui.theme.WhiteDefault
import com.example.mam.viewmodel.management.ManageNotificationViewModel
import com.mapbox.maps.extension.style.expressions.dsl.generated.typeofExpression
import kotlinx.coroutines.launch

@Composable
fun ManageNotificationScreen(
    viewModel: ManageNotificationViewModel,
    onBackClick: () -> Unit,
) {
    val title = viewModel.title.collectAsStateWithLifecycle().value
    val message = viewModel.message.collectAsStateWithLifecycle().value
    val type = viewModel.type.collectAsStateWithLifecycle().value
    val typeList = viewModel.typeList.collectAsStateWithLifecycle().value
    val isLoading = viewModel.isLoading.collectAsStateWithLifecycle().value
    var isShowDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(OrangeDefault)
            .padding(WindowInsets.statusBars.asPaddingValues())
    ) {
        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            CircleIconButton(
                backgroundColor = OrangeLighter,
                foregroundColor = OrangeDefault,
                icon = Icons.Outlined.ArrowBack,
                shadow = "outer",
                onClick = onBackClick,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 16.dp, start = 16.dp)
            )
            Text(
                text = "Soạn thông báo",
                style = Typography.titleLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 17.dp)
            )
            CircleIconButton(
                backgroundColor = OrangeLighter,
                foregroundColor = OrangeDefault,
                icon = Icons.Outlined.Send,
                shadow = "outer",
                onClick = {
                    if (title.isEmpty() || message.isEmpty()) {
                        Toast.makeText(
                            context,
                            "Vui lòng nhập tiêu đề và nội dung thông báo",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        isShowDialog = true
                    }
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 16.dp, end = 16.dp)
            )
        }
        if(isShowDialog) {
            CustomDialog(
                title = "Xác nhận",
                message = "Bạn có chắc chắn muốn gửi thông báo này không?",
                onDismiss = { isShowDialog = false },
                onConfirm = {
                    scope.launch {
                        isShowDialog = false
                        viewModel.createNotification()
                        onBackClick()
                        Toast.makeText(
                            context,
                            "Gửi thông báo thành công",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            )
        }
        Spacer(modifier = Modifier.size(20.dp))
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .outerShadow(
                    color = GreyDark,
                    bordersRadius = 50.dp,
                    blurRadius = 4.dp,
                    offsetX = 0.dp,
                    offsetY = -4.dp,
                )
                .background(
                    color = OrangeLighter,
                    shape = RoundedCornerShape(
                        topStart = 50.dp,
                        topEnd = 50.dp,
                        bottomStart = 0.dp,
                        bottomEnd = 0.dp
                    )
                )
                .fillMaxWidth()
                .height(LocalConfiguration.current.screenHeightDp.dp)
                .clip(
                    shape = RoundedCornerShape(
                        topStart = 50.dp,
                        topEnd = 50.dp,
                        bottomStart = 0.dp,
                        bottomEnd = 0.dp
                    )
                )
        ) {
            item {
                Spacer(modifier = Modifier.size(20.dp))
            }
            item {
                var typeExpand by remember { mutableStateOf(false) }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                ) {
                    Text(
                        text = "Loại thông báo:",
                        fontSize = 14.sp,
                        color = BrownDefault,
                        textAlign = TextAlign.Start,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                    )
                    Box(Modifier
                        .zIndex(1f)
                    ) {
                        FilterChip(
                            selected = typeExpand,
                            onClick = { typeExpand = !typeExpand },
                            label = {
                                Text(
                                    text = type,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier
                                )
                            },
                            trailingIcon = {
                                Icon(
                                    if (!typeExpand) Icons.Default.ArrowDropDown else Icons.Default.ArrowDropUp,
                                    contentDescription = "Expand"
                                )
                            },
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = typeExpand,
                                borderWidth = 1.dp,
                                borderColor = OrangeDefault,
                                selectedBorderColor = OrangeDefault
                            ),
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = OrangeDefault,
                                labelColor = WhiteDefault,
                                iconColor = WhiteDefault,
                                selectedContainerColor = OrangeDefault,
                                selectedLabelColor = WhiteDefault,
                                selectedLeadingIconColor = WhiteDefault,
                                selectedTrailingIconColor = WhiteDefault
                            ),
                            modifier = Modifier
                        )
                        DropdownMenu(
                            expanded = typeExpand,
                            onDismissRequest = { typeExpand = false },
                            containerColor = WhiteDefault,
                            modifier = Modifier
                        ) {
                            typeList.forEach { type ->
                                DropdownMenuItem(
                                    text = { Text(
                                        type,
                                        color = BrownDefault) },
                                    onClick = {
                                        viewModel.setType(type)
                                        typeExpand = false
                                    },
                                )
                            }
                        }
                    }
                }
            }
            item {
                OutlinedTextField(
                    value = title,
                    onValueChange = {
                        viewModel.setTitle(it)
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
                            text = "Tiêu đề",
                            color = BrownDefault,
                            fontSize = 14.sp,
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
            }
            item {
                OutlinedTextField(
                    value =message,
                    onValueChange = {
                        viewModel.setMessage(it)
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
                            text = "Nội dung",
                            color = BrownDefault,
                            fontSize = 14.sp,
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
            }
        }
    }
}

//@Preview
//@Composable
//fun ManageNotificationScreenPreview() {
//    ManageNotificationScreen(
//        viewModel = ManageNotificationViewModel(),
//        onBackClick = {}
//    )
//}
