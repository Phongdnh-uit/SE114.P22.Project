package com.example.mam.gui.screen.client

import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mam.entity.Order
import com.example.mam.entity.OrderItem
import com.example.mam.gui.component.CircleIconButton
import com.example.mam.gui.component.outerShadow
import com.example.mam.ui.theme.BrownDefault
import com.example.mam.ui.theme.GreyDark
import com.example.mam.ui.theme.GreyDefault
import com.example.mam.ui.theme.GreyLight
import com.example.mam.ui.theme.OrangeDefault
import com.example.mam.ui.theme.OrangeLighter
import com.example.mam.ui.theme.Variables
import com.example.mam.ui.theme.WhiteDefault
import com.example.mam.viewmodel.client.OrderHistoryViewModel
import com.yourapp.ui.notifications.NotificationItem
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun OrderHistoryScreen(
    viewModel: OrderHistoryViewModel? = null,
    mockOrder: List<Order>? = null,
    onBackClicked: () -> Unit = {}
    ){
    val localOrder = remember { mutableStateListOf<Order>() }

    if (mockOrder != null && localOrder.isEmpty()) {
        localOrder.addAll(mockOrder)
    }
    val orders = if (mockOrder != null) localOrder else viewModel?.orders?.collectAsState()?.value

    val isLoading = mockOrder == null &&
            (viewModel?.isLoading?.collectAsState()?.value ?: true)

    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        if (mockOrder == null) {
            viewModel?.loadOrders()
        }
    }
    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
    else{
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(color = OrangeDefault)
                .padding(WindowInsets.statusBars.asPaddingValues())
                .verticalScroll(scrollState),
        ){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
            ) {
                // Icon nằm trái
                CircleIconButton(
                    backgroundColor = OrangeLighter,
                    foregroundColor = OrangeDefault,
                    icon = Icons.Filled.ArrowBack,
                    shadow = "outer",
                    onClick = onBackClicked,
                    modifier = Modifier
                        .focusable(false)
                        .align(Alignment.CenterStart)
                        .padding(start = 16.dp) // padding nếu cần
                )
                // Text nằm giữa
                Text(
                    text = "Lịch sử đơn hàng",
                    style = TextStyle(
                        fontSize = Variables.HeadlineMediumSize,
                        lineHeight = Variables.HeadlineMediumLineHeight,
                        fontWeight = FontWeight(700),
                        color = WhiteDefault,
                        textAlign = TextAlign.Center,
                    ),
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .outerShadow(
                        color = GreyDark,
                        bordersRadius = 50.dp,
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
                    .height(300.dp)
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(orders.orEmpty()) { order ->
                    OrderHistoryItem(order)
                }
            }
        }
    }
}
@Composable
fun OrderHistoryItem(order: Order) {
    val formattedTime = order.actualDeliveryTime?.let { formatInstantToDisplay(it) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .outerShadow(
                color = GreyDark,
                bordersRadius = 21.dp,
                offsetX = 15.dp,
                offsetY = 15.dp,
            )
            .background(
                color = WhiteDefault,
                shape = RoundedCornerShape(21.dp)
            )
    ){
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .padding(12.dp)
        ) {
            Text(
                text = "Đơn hàng ${order.id}",
                color = BrownDefault,
                fontSize = Variables.BodySizeBig,
                fontWeight = FontWeight.Bold,
            )
            if (formattedTime != null) {
                Text(
                    text = formattedTime,
                    color = BrownDefault,
                    fontSize = Variables.BodySizeMedium,
                    fontWeight = FontWeight.Medium,
                    fontStyle = FontStyle.Italic
                )
            }
            val orderStatusText = when (order.orderStatus) {
                0 -> "Đã giao"
                1 -> "Đang giao"
                2 -> "Đã hủy"
                else -> "Không xác định"
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "$orderStatusText",
                    color = if (order.orderStatus == 1) GreyDefault else OrangeDefault,
                    fontSize = Variables.BodySizeMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = order.getTotalToString(),
                    color = BrownDefault,
                    fontSize = Variables.BodySizeMedium,
                    fontWeight = FontWeight.Medium,
                    fontStyle = FontStyle.Italic
                )
            }
        }
    }
}

fun parseToInstant(dateStr: String): Instant {
    val formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")
    val localDateTime = LocalDateTime.parse(dateStr, formatter)
    return localDateTime.atZone(ZoneId.of("Asia/Ho_Chi_Minh")).toInstant()
}
fun formatInstantToDisplay(instant: Instant): String {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy, HH:mm")
    return instant.atZone(ZoneId.systemDefault()).format(formatter)
}
@Preview(showBackground = true)
@Composable
fun PreviewOrderHistoryScreen(){
    OrderHistoryScreen(
        mockOrder = listOf(
            Order(
                id = "Pizza001",
                orderStatus = 0,
                orderItems = listOf(OrderItem(price = 170000, quantity = 1)).toMutableList(),
                actualDeliveryTime = parseToInstant("12:00 15/04/2025")
            ),
            Order(
                id = "Pizza002",
                orderStatus = 0,
                orderItems = listOf(OrderItem(price = 200000, quantity = 1)).toMutableList(),
                actualDeliveryTime = parseToInstant("09:00 11/04/2025")
            )
        )
    )
}