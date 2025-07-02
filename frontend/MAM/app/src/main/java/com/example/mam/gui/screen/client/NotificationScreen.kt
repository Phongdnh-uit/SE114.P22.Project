package com.yourapp.ui.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mam.data.Constant
import com.example.mam.dto.notification.NotificationResponse
import com.example.mam.gui.component.CircleIconButton
import com.example.mam.gui.component.outerShadow
import com.example.mam.ui.theme.BrownDefault
import com.example.mam.ui.theme.GreyDark
import com.example.mam.ui.theme.OrangeDefault
import com.example.mam.ui.theme.OrangeLight
import com.example.mam.ui.theme.OrangeLighter
import com.example.mam.ui.theme.Variables
import com.example.mam.ui.theme.WhiteDefault
import com.example.mam.viewmodel.client.NotificationViewModel
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun NotificationScreen(
    viewModel: NotificationViewModel = viewModel(),
    onBackClicked: () -> Unit = {},
    ) {

    val notifications =  viewModel.notifications.collectAsStateWithLifecycle().value
    val scope = rememberCoroutineScope()
    val isLoading = viewModel.isLoading.collectAsStateWithLifecycle().value
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        viewModel.loadNotifications()
    }

        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(color = OrangeDefault)
                .padding(WindowInsets.statusBars.asPaddingValues())
                .verticalScroll(scrollState),
        ) {
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
                    text = "Thông báo",
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
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 30.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = "Đọc tất cả",
                            modifier = Modifier
                                .clickable {
                                        scope.launch {
                                            viewModel.markAllAsRead()
                                        } },
                            color = OrangeDefault,
                            fontSize = Variables.BodySizeMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                if (isLoading){
                    item{
                        CircularProgressIndicator(
                            color = OrangeDefault,
                            modifier = Modifier
                                .padding(16.dp)
                                .size(40.dp)
                        )
                    }
                }
                else
                items(notifications) { notification ->
                    NotificationItem(notification,viewModel)
                }
            }
        }

}

@Composable
fun NotificationItem(notification: NotificationResponse, viewModel: NotificationViewModel? = null) {
    var isRead = notification.isRead
    Card(
        shape = RoundedCornerShape(40.dp,10.dp,40.dp,10.dp),
        colors = CardDefaults.cardColors(
            containerColor = if(isRead) WhiteDefault else OrangeLight,
        ),
        onClick = {
            viewModel?.viewModelScope?.launch {
                viewModel.markAsRead(notification.id)
            }
        },
        modifier = Modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(
                        color = OrangeDefault,
                        shape = RoundedCornerShape(
                            topStart = 30.dp,
                            topEnd = 10.dp,
                            bottomStart = 10.dp,
                            bottomEnd = 10.dp
                        )
                    )
            ) {
                Icon(
                    imageVector = when (notification.type) {
                        Constant.notiStatus.ORDER_PLACED.name -> Icons.Filled.ShoppingCart
                        Constant.notiStatus.ORDER_RECEIVED.name -> Icons.Filled.Inventory
                        Constant.notiStatus.ORDER_PREPARING.name -> Icons.Filled.RestaurantMenu
                        Constant.notiStatus.ORDER_DELIVERING.name -> Icons.Filled.LocalShipping
                        Constant.notiStatus.ORDER_DELIVERED.name -> Icons.Filled.CheckCircle
                        Constant.notiStatus.PROMOTION.name -> Icons.Filled.LocalOffer
                        else -> Icons.Filled.Notifications
                    },
                    contentDescription = "",
                    tint = WhiteDefault,
                    modifier = Modifier
                        .size(40.dp)
                        .align(Alignment.Center)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(
            ) {
                Text(
                    notification.title,
                    color = BrownDefault,
                    fontSize = Variables.BodySizeMedium,
                    fontWeight = if(isRead)FontWeight.Bold else FontWeight.ExtraBold,
                )
                Text(
                    notification.message,
                    color = BrownDefault,
                    fontSize = Variables.BodySizeSmall,
                    fontWeight = if(isRead)FontWeight.Normal else FontWeight.SemiBold,
                )
                Instant.parse(notification.createdAt).atZone(ZoneId.systemDefault())?.let {
                    Text(
                        text = it.format(
                                    DateTimeFormatter.ofPattern(
                                        "HH:mm dd/MM/yyyy"
                                    )
                        ),
                        color = BrownDefault,
                        fontSize = Variables.BodySizeSmall,
                        fontWeight = if(isRead)FontWeight.Normal else FontWeight.SemiBold,
                    )
                }
            }
        }
    }
}

