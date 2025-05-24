package com.example.mam.gui.screen.management

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Moped
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Sell
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.example.mam.viewmodel.management.DashboardViewModel

@OptIn( ExperimentalLayoutApi::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    onBackClicked: () -> Unit = {},
    onItemClicked: (String) -> Unit = {},
    onActiveOrderClicked: (String) -> Unit = {},
) {
    // Your UI code here
    // You can use viewModel to access the data and state
    // For example:
    val isLoading = viewModel.isLoading.collectAsState()
    val activeOrder = viewModel.activeOrder.collectAsState()
    LaunchedEffect(key1 = Unit) {
        viewModel.loadData()
    }
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
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
                icon = Icons.Outlined.Logout,
                shadow = "outer",
                onClick = onBackClicked,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 16.dp, start = 16.dp)
            )
            Text(
                text = "Trang chủ",
                style = Typography.titleLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 17.dp)
            )
        }

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
        )
        {
            item {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(16.dp)
                        .background(
                            color = OrangeLight,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(16.dp)
                ) {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.wrapContentWidth(align = Alignment.CenterHorizontally),
                    ) {
                        MyNav.entries.forEach {
                            Button(
                                onClick = {
                                    onItemClicked(it.label)
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = OrangeDefault,
                                    contentColor = WhiteDefault
                                ),
                                shape = RoundedCornerShape(20.dp),
                                modifier = Modifier
                                    .size(100.dp)
                            ) {
                                Column(
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .fillParentMaxWidth()
                                ) {
                                    Icon(
                                        imageVector = it.icon,
                                        contentDescription = "Item ${it.label}",
                                        //tint = WhiteDefault,
                                        modifier = Modifier
                                            .size(60.dp)
                                            .padding(8.dp)
                                    )
                                    Text(
                                        text = it.label,
                                        fontSize = 16.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .padding(
                                                bottom = 5.dp,
                                                top = 0.dp,
                                                start = 5.dp,
                                                end = 5.dp
                                            )
                                    )
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
                        text = "Đơn hàng đang xử lý",
                        color = BrownDefault,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth()
                    )
                    if (isLoading.value) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    } else {
                        if (activeOrder.value.isEmpty()) {
                            Text(
                                text = "Hiện chưa có đơn hàng nào",
                                color = GreyDefault,
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .padding(10.dp)
                                    .fillMaxWidth()
                            )

                        } else {
                            activeOrder.value.forEach { order ->
                                OrderItem(
                                    order = order,
                                    onClick = { onActiveOrderClicked(order.id) },
                                    onEditClick = { },
                                    onDeleteClick = { },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
enum class MyNav(val label: String, val icon: ImageVector) {
    Catergory("Danh mục", Icons.Default.Inventory),
    Product("Sản phẩm", Icons.Default.Fastfood),
    User("Người dùng", Icons.Default.Person),
    Shipper("Shipper", Icons.Default.Moped),
    Notification("Thông báo", Icons.Default.NotificationsActive),
    Promotion("Khuyến mãi", Icons.Default.Sell),
    Order("Đơn hàng", Icons.Default.ShoppingBag),
    Statistic("Thống kê", Icons.Default.BarChart)

}
@Preview
@Composable
fun DashboardScreenPreview() {
    val viewModel = DashboardViewModel()
    DashboardScreen(
        viewModel = viewModel,
        onBackClicked = {}
    )
}