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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.HowToReg
import androidx.compose.material.icons.outlined.Inventory
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mam.gui.component.CircleIconButton
import com.example.mam.gui.component.OrderItemContainer
import com.example.mam.gui.component.OuterShadowFilledButton
import com.example.mam.gui.component.outerShadow
import com.example.mam.ui.theme.BrownDefault
import com.example.mam.ui.theme.GreyDark
import com.example.mam.ui.theme.GreyDefault
import com.example.mam.ui.theme.GreyLight
import com.example.mam.ui.theme.OrangeDefault
import com.example.mam.ui.theme.OrangeLight
import com.example.mam.ui.theme.OrangeLighter
import com.example.mam.ui.theme.Typography
import com.example.mam.ui.theme.WhiteDefault
import com.example.mam.viewmodel.client.CheckOutViewModel
import com.example.mam.viewmodel.client.OrderViewModel

@Composable
fun OrderScreen(
    onBackClicked: () -> Unit = {},
    onVerifyClicked: () -> Unit = {},
    viewModel: OrderViewModel = viewModel(),
    modifier: Modifier = Modifier
){
    val orderItems = viewModel.items.collectAsStateWithLifecycle()
    val address = viewModel.address
    val note = viewModel.note.collectAsStateWithLifecycle()
    val discount = viewModel.discount.collectAsStateWithLifecycle()
    val total = viewModel.totalPrice.collectAsStateWithLifecycle()
    val status = viewModel.status.collectAsStateWithLifecycle()
    val shipper = viewModel.shipper.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.loadOrder()
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
                text = "Đơn hàng của bạn",
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
                .fillMaxWidth()
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
            item{
                Column(
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ){
                    Column(
                        Modifier.fillMaxWidth(0.9f)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.95f)
                                .background(OrangeLight, RoundedCornerShape(50))) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier
                                    .fillMaxWidth(0.8f)
                                    .align(Alignment.Center)
                                    .zIndex(1f)
                            ) {
                                CircleIconButton(
                                    backgroundColor = when (status.value) {
                                        1, 2, 3, 4 -> OrangeDefault
                                        else -> WhiteDefault
                                    },
                                    foregroundColor = when (status.value) {
                                        1, 2, 3, 4 -> WhiteDefault
                                        else -> OrangeDefault
                                    },
                                    icon = Icons.Outlined.Inventory,
                                    shadow = "outer",
                                    onClick = onBackClicked,
                                    modifier = Modifier
                                        .padding(vertical = 5.dp)
                                )
                                CircleIconButton(
                                    backgroundColor = when (status.value) {
                                        2, 3, 4 -> OrangeDefault
                                        else -> WhiteDefault
                                    },
                                    foregroundColor = when (status.value) {
                                        2, 3, 4 -> WhiteDefault
                                        else -> OrangeDefault
                                    },
                                    icon = Icons.Outlined.LocalFireDepartment,
                                    shadow = "outer",
                                    onClick = onBackClicked,
                                    modifier = Modifier
                                        .padding(vertical = 5.dp)
                                )
                                CircleIconButton(
                                    backgroundColor = when (status.value) {
                                        3, 4 -> OrangeDefault
                                        else -> WhiteDefault
                                    },
                                    foregroundColor = when (status.value) {
                                        3, 4 -> WhiteDefault
                                        else -> OrangeDefault
                                    },
                                    icon = Icons.Outlined.LocalShipping,
                                    shadow = "outer",
                                    onClick = onBackClicked,
                                    modifier = Modifier
                                        .padding(vertical = 5.dp)
                                )
                                CircleIconButton(
                                    backgroundColor = when (status.value) {
                                        4 -> OrangeDefault
                                        else -> WhiteDefault
                                    },
                                    foregroundColor = when (status.value) {
                                        4 -> WhiteDefault
                                        else -> OrangeDefault
                                    },
                                    icon = Icons.Outlined.HowToReg,
                                    shadow = "outer",
                                    onClick = onBackClicked,
                                    modifier = Modifier
                                        .padding(vertical = 5.dp)
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth(0.6f)
                                    .height(10.dp)
                                    .background(GreyLight, RoundedCornerShape(50))
                                    .align(Alignment.Center)
                            ) {

                            }
                        }
                        Text(
                            text = when (status.value) {
                                0 -> "Đơn hàng chưa được xử lý"
                                1 -> "Đơn hàng đã được tiếp nhận"
                                2 -> "Đơn hàng đang được chế biến"
                                3 -> "Đơn hàng đang được giao tới bạn"
                                4 -> "Đơn hàng đã được giao tới bạn"
                                else -> "Đơn hàng không xác định"
                            },
                            fontSize = 18.sp,
                            color = WhiteDefault,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Column(
                        Modifier.fillMaxWidth(0.9f)
                    ) {
                        Text(
                            text = "Người giao hàng:",
                            fontSize = 18.sp,
                            color = WhiteDefault,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Start,
                            modifier = Modifier.padding(start = 10.dp).fillMaxWidth()
                        )
                        Text(
                        text = shipper.value.name + " - " + shipper.value.phoneNumber,
                        fontSize = 18.sp,
                        color = WhiteDefault,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.padding(start = 10.dp).fillMaxWidth()
                        )
                        Text(
                            text = "Biển số xe: " + shipper.value.licensePlate,
                            fontSize = 18.sp,
                            color = WhiteDefault,
                            textAlign = TextAlign.Start,
                            modifier = Modifier.padding(start = 10.dp).fillMaxWidth()
                        )
                    }
                }
            }
            item {
                Column(
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
                ) {
                    Spacer(Modifier.height(20.dp))
                    orderItems.value.forEach{item ->
                        OrderItemContainer(
                            item = item
                        )
                    }
                    Column(
                        verticalArrangement = Arrangement.spacedBy(5.dp),
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .background(
                                OrangeLight,
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
                            text = "Địa chỉ: " + address,
                            fontSize = 18.sp,
                            color = BrownDefault,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(start = 10.dp)
                        )
                        HorizontalDivider(
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .align(Alignment.CenterHorizontally),
                            color = BrownDefault
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = "Giảm giá:",
                                fontSize = 18.sp,
                                color = BrownDefault,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier
                                    .padding(start = 10.dp)

                            )
                            OuterShadowFilledButton(
                                text = "-"+viewModel.getPriceToString(discount.value),
                                fontSize = 18.sp,
                                onClick = {  },
                                modifier = Modifier.wrapContentWidth()

                            )
                        }
                        HorizontalDivider(
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .align(Alignment.CenterHorizontally),
                            color = BrownDefault
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = "Phương thức thanh toán:",
                                fontSize = 18.sp,
                                color = BrownDefault,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(start = 10.dp)
                            )
                            OuterShadowFilledButton(
                                text = viewModel.getPaymentType(),
                                fontSize = 18.sp,
                                onClick = {  },
                                modifier = Modifier.wrapContentWidth()

                            )
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
                            onValueChange = {},
                            readOnly = true,
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
                    Box (Modifier
                        .padding(bottom = 10.dp)
                        .outerShadow()
                        .fillMaxWidth(0.9f)
                        .wrapContentHeight()
                        .background(OrangeLight, shape = RoundedCornerShape(50))

                    ){
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                            modifier = Modifier.padding(10.dp).fillMaxWidth()

                        ) {
                            Column {
                                Text(
                                    text = "Tổng cộng",
                                    fontSize = 20.sp,
                                    color = BrownDefault,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = viewModel.getTotalToString(),
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = OrangeDefault,
                                )
                            }
                            OuterShadowFilledButton(
                                text = "Đã nhận đơn",
                                icon = Icons.Default.Check,
                                onClick = onVerifyClicked ,
                                modifier = Modifier
                            )
                        }
                    }
                }
            }

        }
    }
}
@Preview
@Composable
fun OrderPreview(){
    val viewModel: OrderViewModel = viewModel()
    OrderScreen(
        viewModel = viewModel
    )
}