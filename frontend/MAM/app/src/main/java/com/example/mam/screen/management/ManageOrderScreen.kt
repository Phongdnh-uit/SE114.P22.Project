package com.example.mam.screen.management

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentPasteOff
import androidx.compose.material.icons.filled.Reply
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.yml.charts.common.extensions.isNotNull
import coil.compose.AsyncImage
import com.example.mam.R
import com.example.mam.dto.order.OrderDetailResponse
import com.example.mam.dto.review.ReviewResponse
import com.example.mam.component.CircleIconButton
import com.example.mam.component.CustomDialog
import com.example.mam.component.OuterShadowFilledButton
import com.example.mam.component.outerShadow
import com.example.mam.ui.theme.BrownDefault
import com.example.mam.ui.theme.GreyAvaDefault
import com.example.mam.ui.theme.GreyDark
import com.example.mam.ui.theme.GreyDefault
import com.example.mam.ui.theme.OrangeDefault
import com.example.mam.ui.theme.OrangeLight
import com.example.mam.ui.theme.OrangeLighter
import com.example.mam.ui.theme.Typography
import com.example.mam.ui.theme.WhiteDefault
import com.example.mam.viewmodel.management.ManageOrderViewModel
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@SuppressLint("SuspiciousIndentation")
@Composable
fun ManageOrderScreen(
    viewModel: ManageOrderViewModel,
    onBackClick: () -> Unit,
) {
    val order = viewModel.order.collectAsStateWithLifecycle().value
    val shipper = viewModel.shipper.collectAsStateWithLifecycle().value
    val user = viewModel.user.collectAsStateWithLifecycle().value
    val orderStatus = viewModel.orderStatus.collectAsStateWithLifecycle().value
    val review = viewModel.review.collectAsStateWithLifecycle().value
    val isLoading = viewModel.isLoading.collectAsStateWithLifecycle().value
    val isStatusLoading = viewModel.isStatusLoading.collectAsStateWithLifecycle().value
    var isShowDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        viewModel.loadOrderStatus()
        viewModel.loadData()
        viewModel.loadReview()
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
            Text(
                text = "Chi tiết đơn hàng",
                style = Typography.titleLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 17.dp)
            )
            if (order.orderStatus == "PENDING")
                CircleIconButton(
                    backgroundColor = OrangeLighter,
                    foregroundColor = OrangeDefault,
                    icon = Icons.Default.ContentPasteOff,
                    shadow = "outer",
                    onClick = {
                        scope.launch {
                            viewModel.cancelOrder()
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(end = 16.dp, top = 16.dp)
                )
        }
        if (isShowDialog) {
            CustomDialog(
                title = "Xác nhận",
                message = "Bạn có chắc chắn muốn cập nhật trạng thái đơn hàng này không?",
                onDismiss = { isShowDialog = false },
                onConfirm = {
                    scope.launch {
                        isShowDialog = false
                        if (viewModel.updateStatus() == 0) {
                            Toast.makeText(
                                context,
                                "Cập nhật trạng thái đơn hàng thất bại",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        else
                        Toast.makeText(
                            context,
                            "Cập nhật trạng thái đơn hàng thành công",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            )
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
                if (isStatusLoading) {
                    CircularProgressIndicator(
                        color = OrangeDefault,
                        modifier = Modifier
                            .padding(16.dp)
                            .size(40.dp)
                    )
                } else if (orderStatus == "PENDING" || orderStatus == "CONFIRMED" || orderStatus == "PROCESSING") {
                    OuterShadowFilledButton(
                        text = getStatusUpdateMessage(order.orderStatus),
                        onClick = { isShowDialog = true },
                        textColor = WhiteDefault,
                        color = OrangeDefault,
                        shadowColor = GreyDark,
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .padding(5.dp)
                    )
                }

            }
            if (isLoading) {
                item {
                    CircularProgressIndicator(
                        color = OrangeDefault,
                        modifier = Modifier
                            .padding(16.dp)
                            .size(40.dp)
                    )
                }
            } else {
                if (shipper != null) {
                    item {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(5.dp),
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .background(
                                    OrangeLight,
                                    RoundedCornerShape(10.dp)
                                )
                                .padding(5.dp)
                        ) {
                            Text(
                                text = "Người giao hàng",
                                fontSize = 16.sp,
                                color = BrownDefault,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .padding(start = 10.dp)
                                    .fillMaxWidth()
                            )
                            Text(
                                text = shipper.fullname + " - " + shipper.phone,
                                fontSize = 14.sp,
                                color = BrownDefault,
                                textAlign = TextAlign.Start,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier
                                    .padding(start = 10.dp)
                                    .fillMaxWidth()
                            )
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(
                                        style = SpanStyle(
                                            color = BrownDefault,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    ) {
                                        append("Biển số xe: ")
                                    }
                                    append(shipper.licensePlate)
                                },
                                fontSize = 14.sp,
                                color = BrownDefault,
                                textAlign = TextAlign.Start,
                                modifier = Modifier
                                    .padding(start = 10.dp)
                                    .fillMaxWidth()
                            )
                        }
                    }
                }
                item {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(5.dp),
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .background(
                                OrangeLight,
                                RoundedCornerShape(10.dp)
                            )
                            .padding(5.dp)
                    ) {
                        Text(
                            text = "Người đặt hàng",
                            fontSize = 16.sp,
                            color = BrownDefault,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(start = 10.dp)
                                .fillMaxWidth()
                        )
                        Text(
                            text = user.fullname + " - " + user.phone,
                            fontSize = 14.sp,
                            color = BrownDefault,
                            textAlign = TextAlign.Start,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier
                                .padding(start = 10.dp)
                                .fillMaxWidth()
                        )
                        Text(
                            text = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        color = BrownDefault,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                ) {
                                    append("Địa chỉ: ")
                                }
                                append(order.shippingAddress)
                            },
                            fontSize = 14.sp,
                            color = BrownDefault,
                            textAlign = TextAlign.Start,
                            modifier = Modifier
                                .padding(start = 10.dp)
                                .fillMaxWidth()
                        )
                    }
                }
                item {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(5.dp),
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .background(
                                OrangeLight,
                                RoundedCornerShape(10.dp)
                            )
                            .padding(5.dp)
                    ) {
                        Text(
                            text = "Thông tin đơn hàng",
                            fontSize = 16.sp,
                            color = BrownDefault,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(start = 10.dp)
                                .fillMaxWidth()
                        )
                        Text(
                            text = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        color = BrownDefault,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                ) {
                                    append("Mã đơn hàng: ")
                                }
                                append(order.txnRef)
                            },
                            fontSize = 14.sp,
                            color = BrownDefault,
                            textAlign = TextAlign.Start,
                            modifier = Modifier
                                .padding(start = 10.dp)
                                .fillMaxWidth()
                        )
                        if (order.createdAt.isNotEmpty() && order.createdAt.isNotBlank())
                        Instant.parse(order.createdAt).atZone(ZoneId.systemDefault()).let {
                            Text(
                                text = "Ngày đặt: " + it.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")),
                                textAlign = TextAlign.Start,
                                color = OrangeDefault,
                                fontSize = 14.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.fillMaxWidth()
                                    .padding(start = 10.dp, end = 10.dp)
                            )
                        }
                        if (order.expectedDeliveryTime != null && order.expectedDeliveryTime.isNotEmpty())
                        Instant.parse(order.expectedDeliveryTime).atZone(ZoneId.systemDefault()).let {
                            Text(
                                text = "Thời gian giao hàng dự kiến: " + it.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")),
                                textAlign = TextAlign.Start,
                                color = OrangeDefault,
                                fontSize = 14.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.fillMaxWidth()
                                    .padding(start = 10.dp, end = 10.dp)
                            )
                        }
                        if( order.actualDeliveryTime != null && order.actualDeliveryTime.isNotEmpty())
                        Instant.parse(order.actualDeliveryTime).atZone(ZoneId.systemDefault()).let {
                            Text(
                                text = "Thời gian giao hàng thực tế: " + it.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")),
                                textAlign = TextAlign.Start,
                                color = OrangeDefault,
                                fontSize = 14.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.fillMaxWidth()
                                    .padding(start = 10.dp, end = 10.dp)
                            )
                        }

                        Text(
                            text = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        color = BrownDefault,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                ) {
                                    append("Hình thức thanh toán: ")
                                }
                                append(order.paymentMethod)
                            },
                            fontSize = 14.sp,
                            color = BrownDefault,
                            textAlign = TextAlign.Start,
                            modifier = Modifier
                                .padding(start = 10.dp)
                                .fillMaxWidth()
                        )
                        Text(
                            text = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        color = BrownDefault,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                ) {
                                    append("Tình trạng thanh toán: ")
                                }
                                append(order.paymentStatus)
                            },
                            fontSize = 14.sp,
                            color = BrownDefault,
                            textAlign = TextAlign.Start,
                            modifier = Modifier
                                .padding(start = 10.dp)
                                .fillMaxWidth()
                        )
                        Text(
                            text = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        color = BrownDefault,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                ) {
                                    append("Ghi chú: ")
                                }
                                append(order.note ?: "Không có ghi chú")
                            },
                            fontSize = 14.sp,
                            color = BrownDefault,
                            textAlign = TextAlign.Start,
                            modifier = Modifier
                                .padding(start = 10.dp)
                                .fillMaxWidth()
                        )
                        Text(
                            text = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        color = BrownDefault,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                ) {
                                    append("Tổng tiền: ")
                                }
                                append(order.totalPrice.toString() + " VNĐ")
                            },
                            fontSize = 14.sp,
                            color = BrownDefault,
                            textAlign = TextAlign.Start,
                            modifier = Modifier
                                .padding(start = 10.dp)
                                .fillMaxWidth()
                        )
                        Text(
                            text = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(
                                        color = BrownDefault,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                ) {
                                    append("Trạng thái đơn hàng: ")
                                }
                                append(order.orderStatus)
                            },
                            fontSize = 14.sp,
                            color = OrangeDefault,
                            textAlign = TextAlign.Start,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(start = 10.dp)
                                .fillMaxWidth()
                        )
                    }
                }
                item {
                    Text(
                        text = "Danh sách sản phẩm",
                        fontSize = 16.sp,
                        color = BrownDefault,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .fillMaxWidth()
                    )
                }
                items(order.orderDetails) { item ->
                   OrderItem(
                        item = item
                    )
                }

                if (review != null) {
                    item {
                        Text(
                            text = "Đánh giá của khách hàng",
                            fontSize = 16.sp,
                            color = BrownDefault,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(start = 10.dp)
                                .fillMaxWidth()
                        )
                    }
                    item {
                        OrderReview(
                            review = review,
                            onReplyClick = { reply ->
                                scope.launch {
                                    if (viewModel.reply(reply) == 0) {
                                        Toast.makeText(
                                            context,
                                            "Phản hồi đánh giá thất bại",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Phản hồi đánh giá thành công",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        viewModel.loadReview() // Reload review after replying
                                    }

                                }
                            },
                            modifier = Modifier.fillMaxWidth(0.9f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun OrderItem(
    item: OrderDetailResponse,
    modifier: Modifier = Modifier,
){
    Surface(
        shadowElevation = 4.dp, // Elevation applied here instead
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.padding(8.dp)
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = WhiteDefault
            ),
            modifier = Modifier
                .animateContentSize()
        ) {
            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier
            ) {
                AsyncImage(
                    model = item.getRealUrl(), // Đây là URL từ API
                    contentDescription = null,
                    placeholder = painterResource(R.drawable.ic_mam_logo),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(8.dp)
                        .size(80.dp)
                        .clip(CircleShape)
                )
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Column (
                        verticalArrangement = Arrangement.Top,
                        modifier = Modifier.fillMaxWidth().padding(0.dp, 8.dp, 8.dp, 8.dp)
                    ) {
                        Text(
                            text = item.productName + " *" + item.quantity,
                            textAlign = TextAlign.Start,
                            color = BrownDefault,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.fillMaxWidth()
                        )
                        if (item.variationInfo != null)
                            Text(
                                text = item.variationInfo,
                                textAlign = TextAlign.Start,
                                color = GreyDefault,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        Text(
                            text = item.getPrice(),
                            textAlign = TextAlign.End,
                            color = OrangeDefault,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
@Composable
fun OrderReview(
    review: ReviewResponse,
    onReplyClick: (String) -> Unit,
    modifier: Modifier = Modifier
){
    var isReply by remember { mutableStateOf(false) }
    val rate by remember { mutableIntStateOf(review.rate) }
    val comment by remember { mutableStateOf(review.content) }
    var reply by remember { mutableStateOf(review.reply) }
    Surface(
        color = WhiteDefault,
        shadowElevation = 4.dp, // Elevation applied here instead
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.padding(8.dp)
    ) {
        Column(
            modifier = modifier
                .padding(8.dp)
                .fillMaxWidth()
                .wrapContentSize(Alignment.Center),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                repeat(5) { index ->
                    Icon(
                        imageVector = if (index < rate) Icons.Filled.Star else Icons.Outlined.StarBorder,
                        contentDescription = null,
                        tint = OrangeDefault,
                        modifier = Modifier
                            .size(32.dp)
                    )
                }
            }
            comment?.let {
                OutlinedTextField(
                    value = it,
                    onValueChange = {},
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
                    label = {
                        Text(
                            text = "Nhận xét",
                            color = BrownDefault,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier
                        )
                    },
                    trailingIcon = {
                        if (reply == null) {
                            IconButton(
                                colors = IconButtonColors(
                                    containerColor = WhiteDefault,
                                    contentColor = BrownDefault,
                                    disabledContentColor = BrownDefault,
                                    disabledContainerColor = WhiteDefault
                                ),
                                onClick = { isReply = true }) {
                                Icon(Icons.Default.Reply, contentDescription = "Reply Review")
                            }
                        }
                    },
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                )
            }
            if (isReply || reply.isNotNull())
                Row {
                    Image(
                        painter = painterResource(id = R.drawable.ic_mam_logo),
                        contentDescription = "MAM Logo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .padding(8.dp)
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(GreyAvaDefault)
                    )
                    OutlinedTextField(
                        value = reply ?: "",
                        onValueChange = { reply = it },
                        readOnly = !isReply,
                        textStyle = TextStyle(
                            color = BrownDefault,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BrownDefault,
                            unfocusedBorderColor = GreyDefault,
                        ),
                        label = {
                            Text(
                                text = "Phản hồi từ MAM",
                                color = BrownDefault,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier
                            )
                        },
                        trailingIcon = {
                            if (reply.isNotNull() && isReply) {
                                IconButton(
                                    colors = IconButtonColors(
                                        containerColor = WhiteDefault,
                                        contentColor = BrownDefault,
                                        disabledContentColor = BrownDefault,
                                        disabledContainerColor = WhiteDefault
                                    ),
                                    onClick = {
                                        isReply = false
                                        reply?.let { onReplyClick(it) }
                                    }) {
                                    Icon(Icons.Default.Send, contentDescription = "Reply Review")
                                }
                            }
                            if (reply.isNullOrBlank() && isReply) {
                                IconButton(
                                    colors = IconButtonColors(
                                        containerColor = WhiteDefault,
                                        contentColor = BrownDefault,
                                        disabledContentColor = BrownDefault,
                                        disabledContainerColor = WhiteDefault
                                    ),
                                    onClick = { isReply = false }) {
                                    Icon(Icons.Default.Close, contentDescription = "Cancel Reply")
                                }
                            }
                        },
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        ),
                    )
                }

        }
    }
}
fun getStatusUpdateMessage(status: String): String {
    return when (status) {
        "PENDING" -> "Xác nhận đơn hàng"
        "CONFIRMED" -> "Chế biến"
        "PROCESSING" -> "Giao hàng"
        "SHIPPING" -> "Đang giao hàng"
        "COMPLETED" -> "Đã giao hàng"
        else -> "Không xác định"
    }
}

@Preview
@Composable
fun OrderReviewPreview() {
    OrderReview(
        onReplyClick = {},
        review = ReviewResponse(
            orderId = 1,
            rate = 4,
            content = "Sản phẩm rất tốt, giao hàng nhanh chóng!"
        )
    )
}