package com.example.mam.screen.management

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mam.component.CircleIconButton
import com.example.mam.component.outerShadow
import com.example.mam.ui.theme.BrownDefault
import com.example.mam.ui.theme.ErrorColor
import com.example.mam.ui.theme.GreyDark
import com.example.mam.ui.theme.GreyDefault
import com.example.mam.ui.theme.OrangeDefault
import com.example.mam.ui.theme.OrangeLighter
import com.example.mam.ui.theme.Typography
import com.example.mam.viewmodel.management.ManagePromotionViewModel
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManagePromotionScreen(
    viewModel: ManagePromotionViewModel,
    onBackClick: () -> Unit,
    isAdd: Boolean = false,
    ) {
    val code = viewModel.code.collectAsStateWithLifecycle().value
    val description = viewModel.description.collectAsStateWithLifecycle().value
    val value = viewModel.value.collectAsStateWithLifecycle().value
    val startDate = viewModel.startDate.collectAsStateWithLifecycle().value
    val endDate = viewModel.endDate.collectAsStateWithLifecycle().value
    val minValue = viewModel.minValue.collectAsStateWithLifecycle().value

    val context = LocalContext.current
    val datePickerState = rememberDatePickerState()
    var isShowStartDateDialog by remember { mutableStateOf(false) }
    var isShowEndDateDialog by remember { mutableStateOf(false) }
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
                text = "Thêm khuyến mãi",
                style = Typography.titleLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 17.dp)
            )
            CircleIconButton(
                backgroundColor = OrangeLighter,
                foregroundColor = OrangeDefault,
                icon = Icons.Outlined.Done,
                shadow = "outer",
                onClick = {
                    scope.launch {
                        if (code.isNotEmpty() && description.isNotEmpty() && value.isNotEmpty() && value.toInt() > 0
                            && startDate.isBefore(endDate)
                            && minValue.isNotEmpty() && minValue.toInt() >= 0
                        ) {
                            if (viewModel.createPromotion() == 1){
                                Toast.makeText(
                                    context,
                                    "Thêm khuyến mãi thành công",
                                    Toast.LENGTH_SHORT
                                ).show()
                            onBackClick()
                            }
                            else {
                                Toast.makeText(
                                    context,
                                    "Thêm khuyến mãi thất bại",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Toast.makeText(
                                context,
                                "Vui lòng điền đầy đủ thông tin",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 16.dp, end = 16.dp)
            )
        }
        if (isShowStartDateDialog) {
            DatePickerDialog(
                onDismissRequest = { isShowStartDateDialog = false },
                confirmButton = {
                    TextButton(onClick = {
                        val selectedMillis = datePickerState.selectedDateMillis
                        val dateTimeAtStartOfDay = selectedMillis?.let {
                            val localDate = Instant.ofEpochMilli(it)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()

                            // Kết hợp với giờ 23:59:59
                            val startOfDay = LocalTime.of(0, 0, 0)
                            LocalDateTime.of(localDate, startOfDay)
                        }
                        val startDate = dateTimeAtStartOfDay?.atZone(ZoneId.systemDefault())?.toInstant()
                        viewModel.setStartDate(startDate!!)
                        isShowStartDateDialog = false
                    }) {
                        Text("OK")
                    }
                }
            ) {
                    DatePicker(state = datePickerState)
            }
        }
        if (isShowEndDateDialog) {
            DatePickerDialog(
                onDismissRequest = { isShowEndDateDialog = false },
                confirmButton = {
                    TextButton(onClick = {
                        val selectedMillis = datePickerState.selectedDateMillis
                        val dateTimeAtEndOfDay = selectedMillis?.let {
                            val localDate = Instant.ofEpochMilli(it)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()

                            // Kết hợp với giờ 23:59:59
                            val endOfDay = LocalTime.of(23, 59, 59)
                            LocalDateTime.of(localDate, endOfDay)
                        }
                        val endDate = dateTimeAtEndOfDay?.atZone(ZoneId.systemDefault())?.toInstant()
                        viewModel.setEndDate(endDate!!)
                        isShowEndDateDialog = false
                    }) {
                        Text("OK")
                    }
                }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    DatePicker(state = datePickerState)
                }
            }
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
                OutlinedTextField(
                    value = code,
                    onValueChange = {
                        viewModel.setCode(it)
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
                            text = "Mã khuyến mãi",
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
                    value = description,
                    onValueChange = {
                        viewModel.setDescription(it)
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
                            text = "Mô tả",
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
                    value = value.toString(),
                    onValueChange = {
                        viewModel.setValue(it)
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
                            text = "Giá trị",
                            color = BrownDefault,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier
                        )
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
            }
            item {
                OutlinedTextField(
                    value = startDate.atZone(ZoneId.systemDefault())?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))!!,
                    onValueChange = {
                    },
                    readOnly = true,
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
                            text = "Ngày bắt đầu",
                            color = BrownDefault,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier
                        )
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                isShowStartDateDialog = true
                            },
                            modifier = Modifier
                                .size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.DateRange,
                                contentDescription = null,
                                tint = BrownDefault,
                                modifier = Modifier.size(24.dp)
                            )
                        }
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
                    value = endDate.atZone(ZoneId.systemDefault())?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))!!,
                    onValueChange = {
                    },
                    readOnly = true,
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
                            text = "Ngày kết thúc",
                            color = BrownDefault,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier
                        )
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                isShowEndDateDialog = true
                            },
                            modifier = Modifier
                                .size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.DateRange,
                                contentDescription = null,
                                tint = BrownDefault,
                                modifier = Modifier.size(24.dp)
                            )
                        }
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
                    value = minValue.toString(),
                    onValueChange = {
                        viewModel.setMinValue(it)
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
                            text = "Giá trị đơn hàng tối thiểu",
                            color = BrownDefault,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier
                        )
                    },
                    supportingText = {
                        if (!viewModel.isMinValueValid()) {
                            Text(
                                text = "Giá trị đơn hàng tối thiểu phải lớn hơn giá trị khuyến mãi",
                                color = ErrorColor,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal,
                                modifier = Modifier
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
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
//fun ManagePromotionScreenPreview() {
//    ManagePromotionScreen(
//        viewModel = ManagePromotionViewModel(),
//        onBackClick = {}
//    )
//}