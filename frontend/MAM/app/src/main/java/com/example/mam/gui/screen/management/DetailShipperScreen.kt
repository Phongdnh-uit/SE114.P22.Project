package com.example.mam.gui.screen.management

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mam.entity.Order
import com.example.mam.entity.Shipper
import com.example.mam.gui.component.CircleIconButton
import com.example.mam.gui.component.outerShadow
import com.example.mam.ui.theme.BrownDefault
import com.example.mam.ui.theme.GreyDark
import com.example.mam.ui.theme.GreyDefault
import com.example.mam.ui.theme.OrangeDefault
import com.example.mam.ui.theme.OrangeLight
import com.example.mam.ui.theme.OrangeLighter
import com.example.mam.ui.theme.Typography
import com.example.mam.ui.theme.WhiteDefault
import com.example.mam.viewmodel.management.DetailShipperViewModel

@Composable
fun DetailShipperScreen(
    viewModel: DetailShipperViewModel,
    onBackClick: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    onEditClick: (String) -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onOrderClick: (String) -> Unit = {},
    mockShipper: Shipper ?= null,
    mockOrders: List<Order> ?= null,
) {
    val shipper = mockShipper ?: viewModel.shipper.collectAsStateWithLifecycle().value
    val orders = mockOrders ?: viewModel.orders.collectAsStateWithLifecycle().value
    val isLoading = viewModel.isLoading.collectAsStateWithLifecycle().value
    val isLoadingOrders = viewModel.isLoadingOrders.collectAsStateWithLifecycle().value

    LaunchedEffect(key1 = Unit) {
        viewModel.loadShipper()
        viewModel.loadShipper()
    }

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
            CircleIconButton(
                backgroundColor = OrangeLighter,
                foregroundColor = OrangeDefault,
                icon = Icons.Outlined.Home,
                shadow = "outer",
                onClick = onHomeClick,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 16.dp, top = 16.dp)
            )
            Text(
                text = "Chi tiết Shipper",
                style = Typography.titleLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 17.dp)
            )
        }
        Spacer(modifier = Modifier.size(20.dp))
        LazyColumn(
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
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = WhiteDefault
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 4.dp
                    ),
                    modifier = Modifier.padding(16.dp)
                ) {
                    if(isLoading) {
                        CircularProgressIndicator(
                            color = OrangeDefault,
                            modifier = Modifier
                                .padding(16.dp)
                                .size(40.dp)
                        )
                    } else {
                        shipper?.let { shipper  ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            ) {
                                Text(
                                    text = "#${shipper.id}",
                                    color = GreyDefault,
                                    fontSize = 14.sp,
                                    textAlign = TextAlign.Start,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp)
                                        .fillMaxWidth()
                                )
                                Text(
                                    text = shipper.name,
                                    textAlign = TextAlign.Start,
                                    color = BrownDefault,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp)
                                        .fillMaxWidth()
                                )
                                Text(
                                    text = "Số điện thoại: ${shipper.phoneNumber}",
                                    color = BrownDefault,
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.Start,
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp)
                                        .fillMaxWidth()
                                )
                                Text(
                                    text = "Biển số xe: ${shipper.licensePlate}",
                                    color = BrownDefault,
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.Start,
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp)
                                        .fillMaxWidth()
                                )

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.End,
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp, vertical = 8.dp)
                                        .fillMaxWidth()
                                ) {
                                    IconButton(onClick = { onEditClick(shipper.id) }) {
                                        Icon(
                                            Icons.Default.Edit,
                                            contentDescription = "Edit",
                                            tint = BrownDefault
                                        )
                                    }
                                    IconButton(onClick = { onDeleteClick() }) {
                                        Icon(
                                            Icons.Default.Delete,
                                            contentDescription = "Delete",
                                            tint = BrownDefault
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .background(
                            color = OrangeLight,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Đơn hàng đảm nhiệm",
                        color = BrownDefault,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth()
                    )
                    if (isLoadingOrders) {
                        CircularProgressIndicator(
                            color = OrangeDefault,
                            modifier = Modifier
                                .padding(16.dp)
                                .size(40.dp)
                        )
                    } else if (orders.isEmpty()) {
                        Text(
                            text = "Chưa có sản phẩm nào trong danh mục này",
                            style = Typography.bodyLarge,
                            modifier = Modifier.padding(16.dp)
                        )
                    } else {
                       orders.forEach() { order ->
                            OrderItem(
                                order = order,
                                isViewOnly = true,
                                onClick = { onOrderClick(order.id) },
                                onDeleteClick = {},
                                onEditClick = {}
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
fun DetailShipperScreenPreview() {
    
    DetailShipperScreen(
        viewModel = DetailShipperViewModel(
            savedStateHandle = SavedStateHandle(mapOf("idShipper" to "1"))
        ),
        mockShipper = Shipper(
            name = "Nguyễn Văn A",
            phoneNumber = "0123456789",
            licensePlate = "29A-123.45",
            id = "1"
        ),
        mockOrders = listOf(
            Order(
                id = "1",
                usedId = "1",
                orderDate = java.time.Instant.now(),
                paymentId = "1",
                shippingAddress = "123 Street",
                orderItems = mutableListOf(),
                totalPrice = 100000,
                note = "Note",
                orderStatus = 1,
                expectDeliveryTime = java.time.Instant.now(),
                actualDeliveryTime = java.time.Instant.now(),
                shipperId = "1"
            ),
            Order(
                id = "1",
                usedId = "1",
                orderDate = java.time.Instant.now(),
                paymentId = "1",
                shippingAddress = "123 Street",
                orderItems = mutableListOf(),
                totalPrice = 100000,
                note = "Note",
                orderStatus = 1,
                expectDeliveryTime = java.time.Instant.now(),
                actualDeliveryTime = java.time.Instant.now(),
                shipperId = "1"
            ),
            Order(
                id = "1",
                usedId = "1",
                orderDate = java.time.Instant.now(),
                paymentId = "1",
                shippingAddress = "123 Street",
                orderItems = mutableListOf(),
                totalPrice = 100000,
                note = "Note",
                orderStatus = 1,
                expectDeliveryTime = java.time.Instant.now(),
                actualDeliveryTime = java.time.Instant.now(),
                shipperId = "1"
            )
        )
    )
}