package com.example.mam.gui.screen.authorization

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mam.entity.Notification
import com.example.mam.services.RetrofitClient.api
import com.example.mam.ui.theme.MAMTheme
import com.example.mam.ui.theme.OrangeLighter
import com.example.mam.viewmodel.authorization.NotificationViewModel

@Composable
fun NotificationScreen(
    viewModel: NotificationViewModel = viewModel(), // Mặc định dùng Hilt
    mockNotifications: List<Notification>? = null   // Thêm tham số cho Preview
) {
    val notifications = mockNotifications ?: viewModel.notifications.collectAsState().value
    val isLoading = mockNotifications == null && viewModel.isLoading.collectAsState().value

    LaunchedEffect(Unit) {
        if (mockNotifications == null) {
            viewModel.loadNotifications() // Chỉ gọi API khi không dùng mock data
        }
    }

    if (isLoading) {
        CircularProgressIndicator()
    } else {
        LazyColumn(
            modifier = Modifier
                .background(
                    color = OrangeLighter,
                    shape = RoundedCornerShape(50.dp)
                )
                .fillMaxWidth()  // Thêm dòng này
                .height(300.dp)  // Hoặc kích thước cố định
        ) {
            items(notifications) { notification ->
                NotificationItem(notification)
            }
        }
    }
}
@Composable
fun NotificationItem(notification: Notification) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(notification.title ?: "", fontWeight = FontWeight.Bold)
        Text(notification.content ?: "")
        Text(notification.timestamp ?: "", style = MaterialTheme.typography.bodySmall)
    }
}
// Preview
@Preview(showBackground = true)
@Composable
fun PreviewNotificationScreen() {
    NotificationScreen(
        mockNotifications = listOf(
            Notification(
                id = "1",
                title = "Đơn hàng đã giao",
                content = "Đơn hàng nước chanh đã giao tới bạn!",
                timestamp = "12:00",
                isRead = false),
            Notification(
                id = "2",
                title = "Đơn hàng bị hủy",
                content = "Đơn hàng nước chanh đã bị hủy bởi bạn!",
                timestamp = "09:00",
                isRead = false),
        )
    )
}



