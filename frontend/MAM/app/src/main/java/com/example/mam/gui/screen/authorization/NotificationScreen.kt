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
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mam.entity.Notification
import com.example.mam.gui.component.CircleIconButton
import com.example.mam.gui.component.outerShadow
import com.example.mam.ui.theme.BrownDefault
import com.example.mam.ui.theme.GreyDark
import com.example.mam.ui.theme.OrangeDefault
import com.example.mam.ui.theme.OrangeLighter
import com.example.mam.ui.theme.Variables
import com.example.mam.ui.theme.WhiteDefault
import com.example.mam.viewmodel.authorization.NotificationViewModel

@Composable
fun NotificationScreen(
    viewModel: NotificationViewModel? = null,
    mockNotifications: List<Notification>? = null,
    onBackClicked: () -> Unit = {},
    ) {
//    val notifications = mockNotifications
//        ?: viewModel?.notifications?.collectAsState()?.value
//        ?: emptyList()
    val localNotifications = remember { mutableStateListOf<Notification>() }

    if (mockNotifications != null && localNotifications.isEmpty()) {
        localNotifications.addAll(mockNotifications)
    }

    val notifications = if (mockNotifications != null) localNotifications else viewModel?.notifications?.collectAsState()?.value

    val isLoading = mockNotifications == null &&
            (viewModel?.isLoading?.collectAsState()?.value ?: true)

    fun markAllAsReadLocal() {
        val updatedList = localNotifications.map { it.copy(isRead = true) }
        localNotifications.clear()
        localNotifications.addAll(updatedList)
    }

    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        if (mockNotifications == null) {
            viewModel?.loadNotifications()
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
    } else {
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
                verticalArrangement = Arrangement.spacedBy(12.dp)
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
                                    if (mockNotifications != null) {
                                        markAllAsReadLocal()
                                    } else {
                                        viewModel?.markAllAsRead()
                                    } },
                            color = OrangeDefault,
                            fontSize = Variables.BodySizeMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                items(notifications.orEmpty()) { notification ->
                    NotificationItem(notification)
                }
            }
        }
    }
}

@Composable
fun NotificationItem(notification: Notification) {
    val backgroundColor = if (notification.isRead) OrangeLighter else OrangeDefault.copy(alpha = 0.1f)
    Box(
        modifier = Modifier
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp, bottomStart = 10.dp, bottomEnd = 10.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            //        AsyncImage(
            //            model = notification.image,
            //            contentDescription = "Ảnh thông báo",
            //            modifier = Modifier
            //                .size(50.dp)
            //                .clip(RoundedCornerShape(8.dp))
            //        )
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
            ){
                Icon(
                    imageVector = notification.icon,
                    contentDescription = "Done",
                    tint = WhiteDefault,
                    modifier = Modifier
                        .size(40.dp)
                        .align(Alignment.Center)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text(
                    notification.title ?: "",
                    color = BrownDefault,
                    fontSize = Variables.BodySizeMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    notification.content ?: "",
                    color = BrownDefault,
                    fontSize = Variables.BodySizeMedium
                )
                Text(
                    notification.timestamp ?: "",
                    color = BrownDefault,
                    fontSize = Variables.BodySizeMedium,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewNotificationScreen() {
    NotificationScreen(
        mockNotifications = listOf(
            Notification(
                id = "1",
                title = "Đơn hàng đã hoàn tất",
                content = "Đơn hàng nước chanh đã giao tới bạn!",
                timestamp = "12:00 15/04/2025",
                isRead = false,
                icon = Icons.Filled.DoneAll
            ),
            Notification(
                id = "2",
                title = "Đơn hàng đã hoàn tất",
                content = "Đơn hàng nước cam đã được giao tới bạn!",
                timestamp = "09:00 10/04/2025",
                isRead = true,
                icon = Icons.Filled.DoneAll
            )
        )
    )
}
