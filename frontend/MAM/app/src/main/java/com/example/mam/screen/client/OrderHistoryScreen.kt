 package com.example.mam.screen.client

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
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
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.mam.dto.order.OrderResponse
import com.example.mam.component.BasicOutlinedButton
import com.example.mam.component.CircleIconButton
import com.example.mam.component.outerShadow
import com.example.mam.ui.theme.BrownDefault
import com.example.mam.ui.theme.ErrorColor
import com.example.mam.ui.theme.GreyDark
import com.example.mam.ui.theme.GreyDefault
import com.example.mam.ui.theme.OrangeDefault
import com.example.mam.ui.theme.OrangeLighter
import com.example.mam.ui.theme.Variables
import com.example.mam.ui.theme.WhiteDefault
import com.example.mam.viewmodel.client.OrderHistoryViewModel
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

 @Composable
fun OrderHistoryScreen(
viewModel: OrderHistoryViewModel,
mockOrder: List<OrderResponse>? = null,
onBackClicked: () -> Unit = {},
onClick: (Long) -> Unit = { orderId ->  }
){

val scrollState = rememberScrollState()
val orders = viewModel.orders.collectAsLazyPagingItems()
val asc = viewModel.asc.collectAsStateWithLifecycle().value
val orderStatus = viewModel.orderStatus.collectAsStateWithLifecycle().value
val scope = rememberCoroutineScope()



LaunchedEffect(asc) {
    orders.refresh()
    viewModel.loadOrderStatus()
}
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
                .clip(RoundedCornerShape(
                    topStart = 50.dp,
                    topEnd = 50.dp,
                    bottomStart = 0.dp,
                    bottomEnd = 0.dp
                ))
                .height(LocalConfiguration.current.screenHeightDp.dp)
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                val selectedStatus = viewModel.selectedOrderStatus.collectAsStateWithLifecycle().value
                LazyRow(
                    modifier = Modifier
                        .clipToBounds()
                        .background(OrangeLighter)
                ) {
                    items(orderStatus) { status ->
                        Spacer(Modifier.width(5.dp))
                        BasicOutlinedButton(
                            text = status,
                            onClick = {
                                if (selectedStatus == status) {
                                    viewModel.setOrderStatus("")
                                } else {
                                    viewModel.setOrderStatus(status)
                                }
                            },
                            modifier = if (selectedStatus == status) Modifier.background(WhiteDefault) else Modifier
                        )
                        Spacer(Modifier.width(5.dp))
                    }
                }
            }
            if(orders.itemCount == 0) {
                item {
                    Text(
                        text = "Không có đơn hàng nào",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = GreyDefault
                        ),
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
            else {
                items(orders.itemCount) { index ->
                    val order = orders[index]
                    order?.let {
                        OrderHistoryItem(order,
                            onClick = { onClick(order.id) }
                        )
                    }
                }
                orders.apply {
                    when {
                        loadState.append is LoadState.Loading -> {
                            item { CircularProgressIndicator(
                                color = OrangeDefault,
                                modifier = Modifier.padding(16.dp)) }
                        }
                        loadState.append is LoadState.Error -> {
                            item { Text("Lỗi khi tải thêm", color = ErrorColor) }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun OrderHistoryItem(order: OrderResponse, onClick: () -> Unit = {}) {
    Surface(
        shadowElevation = 4.dp, // Elevation applied here instead
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.padding(8.dp)
    ) {
        Card(
            onClick = onClick,
            colors = CardDefaults.cardColors(
                containerColor = WhiteDefault
            ),
            modifier = Modifier
                .animateContentSize()
        ) {
            Instant.parse(order.createdAt).atZone(ZoneId.systemDefault()).let {
                Text(
                    text = "Ngày đặt: " + it.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")),
                    textAlign = TextAlign.Start,
                    color = OrangeDefault,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth().padding(start = 10.dp, top = 8.dp, end = 10.dp)
                )
            }
            Text(
                text = "Đơn hàng #" + order.txnRef,
                textAlign = TextAlign.Start,
                color = BrownDefault,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth().padding(start = 10.dp, end = 10.dp)
            )
            Text(
                text = "Sản phẩm: " + order.orderDetails.groupBy { it.productName }
                    .map { (productName, items) ->
                        "$productName *${items.sumOf { it.quantity }}"
                    }
                    .joinToString(", "),
                textAlign = TextAlign.Start,
                color = BrownDefault,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth().padding(start = 10.dp, end = 10.dp)
            )
            Text(
                text = "Tổng tiền: " + order.getPriceToString(),
                textAlign = TextAlign.Start,
                color = BrownDefault,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth().padding(start = 10.dp, end = 10.dp)
            )
            Text (
                text = "Trạng thái: " + order.orderStatus,
                textAlign = TextAlign.Start,
                color = GreyDefault,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth().padding(start = 10.dp, bottom = 8.dp, end = 10.dp)
            )
        }
    }
}



