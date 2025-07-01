package com.example.mam.gui.screen.client

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentPasteOff
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.HowToReg
import androidx.compose.material.icons.outlined.Inventory
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import co.yml.charts.common.extensions.isNotNull
import coil.compose.AsyncImage
import com.example.mam.R
import com.example.mam.dto.cart.CartItemResponse
import com.example.mam.dto.order.OrderDetailResponse
import com.example.mam.dto.review.ReviewRequest
import com.example.mam.dto.review.ReviewResponse
import com.example.mam.gui.component.CircleIconButton
import com.example.mam.gui.component.OrderItemContainer
import com.example.mam.gui.component.OrderRatingDialog
import com.example.mam.gui.component.OuterShadowFilledButton
import com.example.mam.gui.component.QuantitySelectionButton
import com.example.mam.gui.component.outerShadow
import com.example.mam.gui.screen.management.OrderItem
import com.example.mam.ui.theme.BrownDefault
import com.example.mam.ui.theme.GreyAvaDefault
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
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun OrderScreen(
    onBackClicked: () -> Unit = {},
    viewModel: OrderViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val order = viewModel.order.collectAsStateWithLifecycle().value
    val shipper = viewModel.shipper.collectAsStateWithLifecycle().value
    val user = viewModel.user.collectAsStateWithLifecycle().value
    val review = viewModel.review.collectAsStateWithLifecycle().value
    val isLoading = viewModel.isLoading.collectAsStateWithLifecycle().value

    val isReviewing = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(isReviewing) {
        viewModel.loadReview()
    }
    LaunchedEffect(Unit) {
        viewModel.loadOrder()
    }
    if (isReviewing.value) {
        OrderRatingDialog(
            orderId = order.id,
            onDismiss = { isReviewing.value = false },
            onSubmit = { reviewRequest ->
                scope.launch {
                    val result = viewModel.createReview(reviewRequest)
                    if (result == 1) {
                        Toast.makeText(context, "MaM xin cảm ơn", Toast.LENGTH_SHORT).show()
                        isReviewing.value = false
                    } else {
                        Toast.makeText(context, "Không thể gửi đánh giá", Toast.LENGTH_SHORT).show()
                    }
                }
            },
        )
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
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 17.dp)
            )
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Column(
                Modifier.fillMaxWidth(0.9f)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.95f)
                        .background(OrangeLight, RoundedCornerShape(50))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .align(Alignment.Center)
                            .zIndex(1f)
                    ) {
                        CircleIconButton(
                            backgroundColor = when (order.orderStatus) {
                                "CONFIRMED", "PROCESSING", "SHIPPING", "COMPLETED" -> OrangeDefault
                                "CANCELED" -> GreyDefault
                                else -> WhiteDefault
                            },
                            foregroundColor = when (order.orderStatus) {
                                "CONFIRMED", "PROCESSING", "SHIPPING", "COMPLETED", "CANCELED" -> WhiteDefault
                                else -> OrangeDefault
                            },
                            icon = if (order.orderStatus == "CANCELED") Icons.Default.ContentPasteOff else Icons.Outlined.Inventory,
                            shadow = "outer",
                            onClick = onBackClicked,
                            modifier = Modifier
                                .padding(vertical = 5.dp)
                        )
                        CircleIconButton(
                            backgroundColor = when (order.orderStatus) {
                                "PROCESSING", "SHIPPING", "COMPLETED" -> OrangeDefault
                                else -> WhiteDefault
                            },
                            foregroundColor = when (order.orderStatus) {
                                "PROCESSING", "SHIPPING", "COMPLETED" -> WhiteDefault
                                else -> OrangeDefault
                            },
                            icon = Icons.Outlined.LocalFireDepartment,
                            shadow = "outer",
                            onClick = onBackClicked,
                            modifier = Modifier
                                .padding(vertical = 5.dp)
                        )
                        CircleIconButton(
                            backgroundColor = when (order.orderStatus) {
                                "SHIPPING", "COMPLETED" -> OrangeDefault
                                else -> WhiteDefault
                            },
                            foregroundColor = when (order.orderStatus) {
                                "SHIPPING", "COMPLETED" -> WhiteDefault
                                else -> OrangeDefault
                            },
                            icon = Icons.Outlined.LocalShipping,
                            shadow = "outer",
                            onClick = onBackClicked,
                            modifier = Modifier
                                .padding(vertical = 5.dp)
                        )
                        CircleIconButton(
                            backgroundColor = when (order.orderStatus) {
                                "COMPLETED" -> OrangeDefault
                                else -> WhiteDefault
                            },
                            foregroundColor = when (order.orderStatus) {
                                "COMPLETED" -> WhiteDefault
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
                            .fillMaxWidth(0.8f)
                            .height(10.dp)
                            .background(GreyLight, RoundedCornerShape(50))
                            .align(Alignment.Center)
                    ) {

                    }
                }
                Text(
                    text = when (order.orderStatus) {
                        "PENDING" -> "Đơn hàng chờ được xác nhận"
                        "CONFIRMED" -> "Đơn hàng đã được tiếp nhận"
                        "PROCESSING" -> "Đơn hàng đang được chế biến"
                        "SHIPPING" -> "Đơn hàng đang được giao tới bạn.\n Thời gian giao hàng dự kiến: " +
                                Instant.parse(order.expectedDeliveryTime)
                                    .atZone(ZoneId.systemDefault())
                            .format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy"))
                        "COMPLETED" -> "Đơn hàng đã được giao tới bạn.\n Thời gian giao hàng: " +
                                Instant.parse(order.actualDeliveryTime)
                                    .atZone(ZoneId.systemDefault())
                            .format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy"))
                        "CANCELED" -> "Đơn hàng đã bị hủy"
                        else -> "Đơn hàng không xác định"
                    },
                    fontSize = 16.sp,
                    color = WhiteDefault,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                        .padding(top = 10.dp, bottom = 10.dp, start = 10.dp, end = 10.dp)
                )
            }
            if (shipper != null) {
                Column(
                    Modifier.fillMaxWidth(0.9f)
                ) {
                    Text(
                        text = "Người giao hàng:",
                        fontSize = 16.sp,
                        color = WhiteDefault,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.padding(start = 10.dp).fillMaxWidth()
                    )
                    Text(
                        text = shipper.fullname + " - " + shipper.phone,
                        fontSize = 14.sp,
                        color = WhiteDefault,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.padding(start = 10.dp).fillMaxWidth()
                    )
                    Text(
                        text = "Biển số xe: " + shipper.licensePlate,
                        fontSize = 14.sp,
                        color = WhiteDefault,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.padding(start = 10.dp).fillMaxWidth()
                    )
                }
            }
        }
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
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
                Spacer(Modifier.height(20.dp))
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
                        text = user.fullname + " " + user.phone,
                        fontSize = 16.sp,
                        color = BrownDefault,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 10.dp)
                    )
                    Text(
                        text = "Địa chỉ giao hàng: " + order.shippingAddress,
                        fontSize = 14.sp,
                        color = BrownDefault,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(start = 10.dp)
                    )
                    Text(
                        text = "Phương thức thanh toán: " + order.paymentMethod,
                        fontSize = 14.sp,
                        color = BrownDefault,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(start = 10.dp)
                    )
                    Text(
                        text = "Tình trạng thanh toán: " + order.paymentStatus,
                        fontSize = 14.sp,
                        color = BrownDefault,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(start = 10.dp)
                    )
                    Text(
                        text = "Ghi chú: \n" + if (order.note.isNullOrEmpty()) {
                            "Không có ghi chú"
                        } else {
                            order.note
                        },
                        fontSize = 14.sp,
                        color = BrownDefault,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp)

                    )
                }
            }
            item {
                Box(
                    Modifier
                        .outerShadow()
                        .padding(bottom = 5.dp)
                        .fillMaxWidth(0.9f)
                        .wrapContentHeight()
                        .background(OrangeLight, shape = RoundedCornerShape(50))

                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                        modifier = Modifier.padding(10.dp).fillMaxWidth()

                    ) {
                        Column {
                            Text(
                                text = "Tổng cộng",
                                fontSize = 16.sp,
                                color = BrownDefault,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = order.getPriceToString(),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = OrangeDefault,
                            )
                        }
                        if (order.orderStatus == "SHIPPING") {
                            OuterShadowFilledButton(
                                text = "Xác nhận nhận hàng",
                                icon = Icons.Outlined.CheckCircle,
                                onClick = {
                                    scope.launch {
                                        if (viewModel.maskAsDeliveried() == 1) {
                                            Toast.makeText(
                                                context,
                                                "Đã xác nhận đã nhận hàng. Chúc bạn ngon miệng!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else {
                                            Toast.makeText(
                                                context,
                                                "Không thể xác nhận đã nhận hàng",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .wrapContentSize()
                            )
                        } else if (order.orderStatus == "PENDING") {
                            OuterShadowFilledButton(
                                text = "Hủy đơn hàng",
                                icon = Icons.Default.Delete,
                                onClick = {
                                    scope.launch {
                                        if (viewModel.cancelOrder() == 1) {
                                            Toast.makeText(
                                                context,
                                                "Đã hủy đơn hàng thành công",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else {
                                            Toast.makeText(
                                                context,
                                                "Không thể hủy đơn hàng này",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .wrapContentSize()
                            )
                        }
                    }
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
                        text = "Đánh giá của bạn",
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
                    OrderRating(
                        review = review,
                        onEditClick = { reviewRequest ->
                            scope.launch {
                                if (viewModel.updateReview(
                                        review.id,
                                        ReviewRequest(
                                            orderId = review.orderId,
                                            rate = reviewRequest.rate,
                                            content = reviewRequest.content,
                                        )) == 1) {
                                    Toast.makeText(
                                        context,
                                        "Đánh giá đã được cập nhật",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    viewModel.loadReview() // Reload review after update
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Không thể cập nhật đánh giá",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(0.9f)
                    )
                }
            } else if (order.orderStatus == "COMPLETED") {
                item {
                    OuterShadowFilledButton(
                        text = "Đánh giá đơn hàng",
                        icon = Icons.Default.Star,
                        onClick = { isReviewing.value = true },
                        modifier = Modifier.fillMaxWidth(0.9f)
                    )
                }
            }
        }
    }
}

@Composable
fun OrderRating(
    onEditClick: (ReviewRequest) -> Unit = {},
    review: ReviewResponse,
    modifier: Modifier = Modifier
){
    var isEdit by remember { mutableStateOf(false) }
    var rate by remember { mutableIntStateOf(review.rate) }
    var comment by remember { mutableStateOf(review.content)}
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
                            .clickable {
                                if (isEdit) rate = index + 1
                            }
                    )
                }
            }
            comment?.let {
                OutlinedTextField(
                    value = it,
                    onValueChange = { comment = it },
                    readOnly = !isEdit,
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
                        if (!isEdit && reply.isNullOrEmpty()) {
                            IconButton(
                                colors = IconButtonColors(
                                    containerColor = WhiteDefault,
                                    contentColor = BrownDefault,
                                    disabledContentColor = BrownDefault,
                                    disabledContainerColor = WhiteDefault
                                ),
                                onClick = { isEdit = true }) {
                                Icon(Icons.Default.Edit, contentDescription = "Chỉnh sửa đánh giá")
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
            if (isEdit && reply == null) {
                OuterShadowFilledButton(
                    text = "Lưu đánh giá",
                    onClick = {
                        isEdit = false
                        onEditClick(
                            ReviewRequest(
                                orderId = review.orderId,
                                rate = rate,
                                content = comment,
                                reply = reply
                            )
                        )
                    },
                    textColor = WhiteDefault,
                    color = OrangeDefault,
                    shadowColor = GreyDark,
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(5.dp)
                )
            }
            reply?.let {
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
                        value = it,
                        onValueChange = { },
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
                                text = "Phản hồi từ MAM",
                                color = BrownDefault,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier
                            )
                        },
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun OrderRatingPreview() {
    OrderRating(
        review = ReviewResponse(
            orderId = 1,
            rate = 4,
            content = "Món ăn rất ngon, giao hàng nhanh chóng!",
//            reply = "Cảm ơn bạn đã đánh giá!"
        )
    )
}