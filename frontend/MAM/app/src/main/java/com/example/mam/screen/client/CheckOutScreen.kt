package com.example.mam.screen.client

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.animateContentSize
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.mam.R
import com.example.mam.dto.cart.CartItemResponse
import com.example.mam.component.CircleIconButton
import com.example.mam.component.OuterShadowFilledButton
import com.example.mam.component.PaymentLoadingAlertDialog
import com.example.mam.component.outerShadow
import com.example.mam.ui.theme.BrownDefault
import com.example.mam.ui.theme.GreyDark
import com.example.mam.ui.theme.GreyDefault
import com.example.mam.ui.theme.OrangeDefault
import com.example.mam.ui.theme.OrangeLight
import com.example.mam.ui.theme.OrangeLighter
import com.example.mam.ui.theme.Typography
import com.example.mam.ui.theme.WhiteDefault
import com.example.mam.viewmodel.client.CheckOutViewModel
import kotlinx.coroutines.launch

@Composable
fun CheckOutScreen(
    onBackClicked: () -> Unit = {},
    onHomeClicked: () -> Unit = {},
    onCheckOutClicked: (Long) -> Unit = {},
    onChangeAddressClicked: () -> Unit = { },
    viewModel: CheckOutViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val user = viewModel.user.collectAsStateWithLifecycle().value
    val cartItems = viewModel.cart.collectAsStateWithLifecycle().value.cartItems
    val address = viewModel.address.collectAsStateWithLifecycle().value
    val discountList = viewModel.discountList.collectAsStateWithLifecycle().value
    val selectedDiscount = viewModel.discount.collectAsStateWithLifecycle().value
    val paymentOptions = viewModel.paymentOptions.collectAsStateWithLifecycle()
    val selectedPaymentOption = viewModel.paymentOption.collectAsStateWithLifecycle().value
    val note = viewModel.note.collectAsStateWithLifecycle().value
    val total = viewModel.total.collectAsStateWithLifecycle().value
    val orderTotal = viewModel.orderTotal.collectAsStateWithLifecycle().value

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var paymentExpanded by remember { mutableStateOf(false) }
    val isPaying = viewModel.isPaying.collectAsStateWithLifecycle().value

    LaunchedEffect(LocalLifecycleOwner.current) {
        viewModel.loadCart()
        viewModel.loadPaymentOptions()
        viewModel.loadUser()
        viewModel.loadAddress()
        viewModel.loadDiscounts()
    }

    if (isPaying) {
        PaymentLoadingAlertDialog()
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
                text = "Thanh toán",
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
            item { Spacer(Modifier.height(20.dp)) }
            items(cartItems) { item ->
                CheckOutItem(
                    cartItem = item,
                )
            }
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .background(OrangeLight,
                            RoundedCornerShape(10.dp))
                        .padding(5.dp)
                ) {
                    Text(
                        text = user.fullname + " - " + user.phone,
                        fontSize = 16.sp,
                        color = BrownDefault,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 10.dp)
                    )
                    Text(
                        text = "Địa chỉ: " + if(address.isEmpty()) "Không xác định" else address,
                        fontSize = 16.sp,
                        color = BrownDefault,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(start = 10.dp)
                    )
                    OuterShadowFilledButton(
                        text = "Chọn địa chỉ khác",
                        fontSize = 14.sp,
                        onClick = onChangeAddressClicked,
                        modifier = Modifier.wrapContentWidth().align(Alignment.End).padding(end = 10.dp)
                    )

                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .align(Alignment.CenterHorizontally),
                        color = BrownDefault
                    )
                    Text(
                        text = "Mã giảm giá:",
                        fontSize = 16.sp,
                        color = BrownDefault,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp)

                    )

                    if (discountList.isEmpty()) {
                        Text(
                            text = "Không có mã giảm giá khả dụng nào",
                            textAlign = TextAlign.Center,
                            fontSize = 16.sp,
                            color = GreyDefault,
                            modifier = Modifier.padding(horizontal = 10.dp).fillMaxWidth()
                        )
                    }
                    else {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .padding(8.dp)
                                .wrapContentHeight()
                        ) {
                            items(discountList) { promo ->
                                val isSelected = selectedDiscount?.code == promo.code
                                Card(
                                    onClick = {
                                        if (isSelected) {
                                            viewModel.setDiscount(null)
                                        } else {
                                            viewModel.setDiscount(promo)
                                        }
                                    },
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (isSelected) OrangeDefault else WhiteDefault
                                    ),
                                    elevation = CardDefaults.cardElevation(
                                        defaultElevation = if (isSelected) 8.dp else 4.dp
                                    ),
                                    modifier = Modifier
                                        .height(50.dp)
                                        .wrapContentWidth()
                                ) {
                                    Text(
                                        text = promo.code,
                                        textAlign = TextAlign.Start,
                                        color = if (isSelected) WhiteDefault else BrownDefault,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.weight(1f).padding(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 4.dp)
                                    )
                                    Text(
                                        text = promo.getDiscountAmount(),
                                        textAlign = TextAlign.End,
                                        color = if (isSelected) WhiteDefault else OrangeDefault,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.weight(1f).padding(start = 8.dp, end = 8.dp, bottom = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .align(Alignment.CenterHorizontally),
                        color = BrownDefault
                    )
                    Text(
                        text = "Phương thức thanh toán:",
                        fontSize = 16.sp,
                        color = BrownDefault,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(start = 10.dp)
                    )
                    Box(
                        Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                    ) {
                        FilterChip(
                            selected = paymentExpanded,
                            onClick = { paymentExpanded = !paymentExpanded },
                            label = { Text(selectedPaymentOption, fontSize = 16.sp, textAlign = TextAlign.Center) },
                            trailingIcon = {
                                Icon(Icons.Default.ArrowDropDown, contentDescription = "Expand")
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = OrangeDefault,
                                labelColor = WhiteDefault,
                                iconColor = WhiteDefault,
                                selectedContainerColor = OrangeDefault,
                                selectedLabelColor = WhiteDefault,
                                selectedLeadingIconColor = WhiteDefault,
                                selectedTrailingIconColor = WhiteDefault
                            ),
                            shape = RoundedCornerShape(50),
                            modifier = Modifier.fillMaxWidth(0.9f).height(40.dp).align(Alignment.Center)

                        )

                        DropdownMenu(
                            expanded = paymentExpanded,
                            onDismissRequest = { paymentExpanded = false },
                            containerColor = WhiteDefault,
                            modifier = Modifier.padding(start = 8.dp).fillMaxWidth(0.9f)
                        ) {
                            paymentOptions.value.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option, color = BrownDefault) },
                                    onClick = {
                                        viewModel.setupPaymentOption(option)
                                        paymentExpanded = false
                                    }
                                )
                            }
                        }
                    }
                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .align(Alignment.CenterHorizontally),
                        color = BrownDefault
                    )
                    Text(
                        text = "Ghi chú:",
                        fontSize = 16.sp,
                        color = BrownDefault,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp)

                    )
                    OutlinedTextField(
                        value = note?:"",
                        onValueChange = { viewModel.setNote(it) },
                        textStyle = TextStyle(fontSize = 14.sp, color = BrownDefault),
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
                        modifier = Modifier.padding(2.dp).fillMaxWidth(0.9f).align(Alignment.CenterHorizontally),
                    )
                }
            }
            item {
                Box(
                    Modifier
                        .padding(bottom = 10.dp)
                        .outerShadow(bordersRadius = 20.dp)
                        .fillMaxWidth(0.8f)
                        .wrapContentHeight()
                        .background(OrangeLight, shape = RoundedCornerShape(20.dp))
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(10.dp).fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                            modifier = Modifier.padding(10.dp).fillMaxWidth()

                        ) {
                            Text(
                                text = "Tạm tính:",
                                fontSize = 16.sp,
                                color = BrownDefault,
                            )
                            Text(
                                text = total,
                                fontSize = 20.sp,
                                color = OrangeDefault,
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                            modifier = Modifier.padding(10.dp).fillMaxWidth()

                        ) {
                            Text(
                                text = "Giảm giá:",
                                fontSize = 16.sp,
                                color = BrownDefault,
                            )
                            Text(
                                text = selectedDiscount?.getDiscountAmount() ?: "0 VND",
                                fontSize = 20.sp,
                                color = OrangeDefault,
                            )
                        }
                        HorizontalDivider(
                            modifier = Modifier,
                            color = BrownDefault
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                            modifier = Modifier.padding(10.dp).fillMaxWidth()
                        ) {
                            Text(
                                text = "Tổng cộng:",
                                fontSize = 16.sp,
                                color = BrownDefault,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = orderTotal,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = OrangeDefault,
                            )
                        }
                        OuterShadowFilledButton(
                            text = "Đặt hàng",
                            onClick = {
                                if (address.isNotEmpty())
                                scope.launch {
                                    val result = viewModel.checkOut()
                                    if (result != -1) {
                                        Toast.makeText(context, "Đặt hàng thành công!", Toast.LENGTH_SHORT).show()
                                        if (selectedPaymentOption == "VNPAY"){
                                            val vnpayUrl = viewModel.createPayment(result)
                                            if (!vnpayUrl.isNullOrBlank()) {
                                                openInBrowser(context, vnpayUrl)
                                            } else {
                                                Toast.makeText(context, "Không thể tạo liên kết thanh toán. Vui lòng đặt lại.", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                        onCheckOutClicked(result.toLong())
                                    } else {
                                        Toast.makeText(context, "Đặt hàng thất bại. Vui lòng thử lại sau.", Toast.LENGTH_SHORT).show()
                                        onHomeClicked()
                                    }

                                }else {
                                    Toast.makeText(context, "Đặt hàng thất bại. Vui lòng chọn địa chỉ.", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier.fillMaxWidth(0.8f)
                        )
                    }
                }
            }
        }
    }
}
fun openInBrowser(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(intent)
}
@Composable
fun CheckOutItem(
    cartItem: CartItemResponse,
    modifier: Modifier = Modifier,
) {
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
                    model = cartItem.getRealUrl(), // Đây là URL từ API
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
                    Column(
                        verticalArrangement = Arrangement.Top,
                        modifier = Modifier.fillMaxWidth().padding(0.dp, 8.dp, 8.dp, 8.dp)
                    ) {
                        Text(
                            text = cartItem.productName,
                            textAlign = TextAlign.Start,
                            color = BrownDefault,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.fillMaxWidth()
                        )
                        cartItem.variationOptionInfo?.let {
                            Text(
                                text = it,
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
                            text = buildAnnotatedString {
                                append(cartItem.getPrice())
                                withStyle(
                                    SpanStyle(
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Normal,
                                        color = GreyDark
                                    )
                                ) {
                                    append(" x${cartItem.quantity}")
                                }
                            },
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
}

