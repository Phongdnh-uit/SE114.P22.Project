package com.example.mam.gui.screen.client

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
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mam.gui.component.CircleIconButton
import com.example.mam.gui.component.OrderItemContainer
import com.example.mam.gui.component.OuterShadowFilledButton
import com.example.mam.gui.component.innerShadow
import com.example.mam.gui.component.outerShadow
import com.example.mam.ui.theme.BrownDefault
import com.example.mam.ui.theme.GreyDark
import com.example.mam.ui.theme.OrangeDefault
import com.example.mam.ui.theme.OrangeLight
import com.example.mam.ui.theme.OrangeLighter
import com.example.mam.ui.theme.Typography
import com.example.mam.ui.theme.WhiteDefault
import com.example.mam.viewmodel.client.CheckOutViewModel

@Composable
fun CheckOutScreen(
    onBackClicked: () -> Unit = {},
    onCheckOutClicked: () -> Unit = {},
    onChangeAddressClicked: () -> Unit = { },
    viewModel: CheckOutViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val items = viewModel.orderItems.collectAsStateWithLifecycle()

    val promoCodeState = viewModel.promoCode.collectAsStateWithLifecycle()
    val discount = viewModel.discount.collectAsStateWithLifecycle()
    val isScreenActive = rememberUpdatedState(newValue = true)

    val note = viewModel.note.collectAsStateWithLifecycle()

    var paymentExpanded by remember { mutableStateOf(false) }
    val paymentOptions = viewModel.paymentOptions.collectAsStateWithLifecycle()
    var paymentOption by remember { mutableStateOf("") }

    LaunchedEffect(isScreenActive.value) {
        viewModel.loadCart()
        viewModel.loadOrderItems()
        viewModel.loadPaymentOptions()
        viewModel.loadUser()
        paymentOption = paymentOptions.value.firstOrNull()?.name ?: "Default"
    }
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .background(color = OrangeDefault)
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
                icon = Icons.Default.ArrowBack,
                shadow = "outer",
                onClick = onBackClicked,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 16.dp, start = 16.dp)
            )
            Text(
                text = "Thanh toán",
                style = Typography.titleLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 17.dp)
            )
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(20.dp),
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
        )
        {
            item { Spacer(Modifier.height(20.dp)) }
            items(items.value) { item ->
                OrderItemContainer(
                    item = item
                )
            }
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .background(OrangeLight,
                            RoundedCornerShape(10.dp))
                        .padding(5.dp)
                ) {
                    Text(
                        text = viewModel.getUser().fullName + " - " + viewModel.getUser().phoneNumber,
                        fontSize = 18.sp,
                        color = BrownDefault,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 10.dp)
                    )
                    Text(
                        text = "Địa chỉ: " + viewModel.address,
                        fontSize = 18.sp,
                        color = BrownDefault,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(start = 10.dp)
                    )
                    OuterShadowFilledButton(
                        text = "Chọn địa chỉ khác",
                        fontSize = 18.sp,
                        onClick = onChangeAddressClicked,
                        modifier = Modifier.wrapContentWidth().align(Alignment.End)
                    )

                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .align(Alignment.CenterHorizontally),
                        color = BrownDefault
                    )
                    Text(
                        text = "Mã giảm giá:",
                        fontSize = 18.sp,
                        color = BrownDefault,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp)

                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .wrapContentHeight()
                            .fillMaxWidth()
                            .background(OrangeLight)
                    ) {
                        OutlinedTextField(
                            value = promoCodeState.value,
                            onValueChange = { viewModel.setDiscount(it) },
                            textStyle = TextStyle(fontSize = 18.sp, color = BrownDefault),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = WhiteDefault,  // Màu nền khi focus
                                unfocusedContainerColor = WhiteDefault, // Màu nền khi không focus
                                focusedIndicatorColor = BrownDefault,  // Màu viền khi focus
                                unfocusedIndicatorColor = BrownDefault,  // Màu viền khi không focus
                                focusedTextColor = BrownDefault,       // Màu chữ khi focus
                                unfocusedTextColor = BrownDefault,      // Màu chữ khi không focus
                                cursorColor = BrownDefault             // Màu con trỏ nhập liệu
                            ),
                            shape = RoundedCornerShape(10.dp),
                            singleLine = true,
                            modifier = Modifier.padding(2.dp).fillMaxWidth(0.7f)
                        )
                        OuterShadowFilledButton(
                            text = "Áp dụng",
                            fontSize = 18.sp,
                            onClick = { viewModel.getDiscount() },
                            modifier = Modifier.width(90.dp)

                        )
                    }
                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .align(Alignment.CenterHorizontally),
                        color = BrownDefault
                    )
                    Text(
                        text = "Phương thức thanh toán:",
                        fontSize = 18.sp,
                        color = BrownDefault,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(start = 10.dp)
                    )
                    Box(
                        Modifier
                            .padding(8.dp)
                    ) {
                        FilterChip(
                            selected = paymentExpanded,
                            onClick = { paymentExpanded = !paymentExpanded },
                            label = { Text(paymentOption, fontSize = 18.sp, textAlign = TextAlign.Center) },
                            trailingIcon = {
                                Icon(Icons.Default.ArrowDropDown, contentDescription = "Expand")
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = OrangeDefault,
                                labelColor = WhiteDefault,
                                iconColor = WhiteDefault,
                                selectedContainerColor = OrangeDefault,
                                selectedLabelColor = WhiteDefault,
                                selectedLeadingIconColor = WhiteDefault,
                                selectedTrailingIconColor = WhiteDefault
                            ),
                            shape = RoundedCornerShape(50),
                            modifier = Modifier.fillMaxWidth().height(40.dp)
                        )

                        DropdownMenu(
                            expanded = paymentExpanded,
                            onDismissRequest = { paymentExpanded = false },
                            containerColor = WhiteDefault,
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
                            paymentOptions.value.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option.name, color = BrownDefault) },
                                    onClick = {
                                        paymentOption = option.name
                                        viewModel.setupPaymentOption(option)
                                        paymentExpanded = false
                                    }
                                )
                            }
                        }
                    }
                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .align(Alignment.CenterHorizontally),
                        color = BrownDefault
                    )
                    Text(
                        text = "Ghi chú:",
                        fontSize = 20.sp,
                        color = BrownDefault,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp)

                    )
                    OutlinedTextField(
                        value = note.value,
                        onValueChange = { viewModel.setNote(it) },
                        textStyle = TextStyle(fontSize = 20.sp, color = BrownDefault),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = WhiteDefault,  // Màu nền khi focus
                            unfocusedContainerColor = WhiteDefault, // Màu nền khi không focus
                            focusedIndicatorColor = BrownDefault,  // Màu viền khi focus
                            unfocusedIndicatorColor = BrownDefault,  // Màu viền khi không focus
                            focusedTextColor = BrownDefault,       // Màu chữ khi focus
                            unfocusedTextColor = BrownDefault,      // Màu chữ khi không focus
                            cursorColor = BrownDefault             // Màu con trỏ nhập liệu
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.padding(2.dp).fillMaxWidth()
                    )
                }
            }
            item {
                Box(
                    Modifier
                        .padding(bottom = 10.dp)
                        .outerShadow(bordersRadius = 20.dp)
                        .fillMaxWidth(0.8f)
                        .wrapContentHeight()
                        .background(OrangeLight, shape = RoundedCornerShape(20.dp))
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(10.dp).fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                            modifier = Modifier.padding(10.dp).fillMaxWidth()

                        ) {
                            Text(
                                text = "Tạm tính:",
                                fontSize = 20.sp,
                                color = BrownDefault,
                            )
                            Text(
                                text = viewModel.getCartTotalToString(),
                                fontSize = 24.sp,
                                color = OrangeDefault,
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                            modifier = Modifier.padding(10.dp).fillMaxWidth()

                        ) {
                            Text(
                                text = "Giảm giá:",
                                fontSize = 20.sp,
                                color = BrownDefault,
                            )
                            Text(
                                text = viewModel.getPriceToString(discount.value),
                                fontSize = 24.sp,
                                color = OrangeDefault,
                            )
                        }
                        HorizontalDivider(
                            modifier = Modifier,
                            color = BrownDefault
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                            modifier = Modifier.padding(10.dp).fillMaxWidth()
                        ) {
                            Text(
                                text = "Tổng cộng:",
                                fontSize = 20.sp,
                                color = BrownDefault,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = viewModel.getTotalPriceToString(),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = OrangeDefault,
                            )
                        }
                        OuterShadowFilledButton(
                            text = "Đặt hàng",
                            onClick = {
                                onCheckOutClicked()
                                      },
                            modifier = Modifier.fillMaxWidth(0.8f)
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun CheckOutPreview(){
    val viewModel: CheckOutViewModel = viewModel()
    CheckOutScreen(
        viewModel = viewModel
    )
}