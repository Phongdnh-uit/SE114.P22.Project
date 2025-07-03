package com.example.mam.screen.client

import android.annotation.SuppressLint
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.mam.R
import com.example.mam.dto.cart.CartItemResponse
import com.example.mam.component.AdditionalProduct
import com.example.mam.component.CircleIconButton
import com.example.mam.component.OuterShadowFilledButton
import com.example.mam.component.QuantitySelectionButton
import com.example.mam.component.outerShadow
import com.example.mam.ui.theme.BrownDefault
import com.example.mam.ui.theme.GreyDark
import com.example.mam.ui.theme.GreyDefault
import com.example.mam.ui.theme.GreyLight
import com.example.mam.ui.theme.OrangeDefault
import com.example.mam.ui.theme.OrangeLight
import com.example.mam.ui.theme.OrangeLighter
import com.example.mam.ui.theme.Typography
import com.example.mam.ui.theme.WhiteDefault
import com.example.mam.viewmodel.client.CartViewModel
import kotlinx.coroutines.launch

//@SuppressLint("MutableCollectionMutableState")
@SuppressLint("SuspiciousIndentation")
@Composable
fun CartScreen(
    onBackClicked: () -> Unit = {},
    onCheckOutClicked: () -> Unit = {},
    onAdditionalProductClicked: (Long) -> Unit = {},
    viewModel: CartViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val cart = viewModel.cart.collectAsStateWithLifecycle().value
    val recommendedProducts = viewModel.recommendedProducts.collectAsStateWithLifecycle().value
    val total = viewModel.total.collectAsStateWithLifecycle().value
    val isLoading = viewModel.isLoading.collectAsStateWithLifecycle().value

    val scope = rememberCoroutineScope()
    LaunchedEffect(LocalLifecycleOwner.current) {
        viewModel.getCart()
        viewModel.loadAdditionalProduct()
    }
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
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
                    text = "Giỏ hàng",
                    style = Typography.titleLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 17.dp)
                )
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
                item { Spacer(Modifier.height(20.dp))}
                if (cart.cartItems.isEmpty() ) {
                    item {
                        Text(
                            text = "Giỏ hàng của bạn đang trống",
                            fontSize = 18.sp,
                            color = BrownDefault,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }
                else
                items(cart.cartItems) { item ->
                    CartItem(
                        cartItem = item,
                        onQuantityIncr = {
                            scope.launch {
                                viewModel.incrItemQuantity(item)
                            }
                        },
                        onQuantityDesc = {
                            scope.launch {
                                viewModel.descItemQuantity(item)
                            }
                        },
                        onDeleteClicked = {
                            scope.launch {
                                viewModel.deleteItem(item.id)
                            }
                        },
                    )
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
                }
                if (recommendedProducts.isNotEmpty()) {
                    item {
                        Text(
                            text = "Đề xuất món ngon",
                            textAlign = TextAlign.Start,
                            maxLines = 2,
                            fontSize = 16.sp,
                            color = BrownDefault,
                            fontWeight = FontWeight.Medium,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .padding(start = 10.dp, top = 10.dp)
                                .align(Alignment.Start)
                                .fillMaxWidth()
                        )
                    }
                    item {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier
                                .padding(10.dp)
                        ) {
                            items(recommendedProducts){ item ->
                                AdditionalProduct(
                                    item = item,
                                    onClick = { onAdditionalProductClicked(item.id)},
                                )
                            }
                        }
                    }
                }
                item{Spacer(Modifier.height(60.dp))}
            }
        }
        Box (Modifier
            .padding(bottom = 10.dp)
            .align(Alignment.BottomCenter)
            .outerShadow(bordersRadius = 50.dp)
            .fillMaxWidth(0.8f)
            .wrapContentHeight()
            .background(OrangeLight, shape = RoundedCornerShape(50))

        ){
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()

            ) {
                Column {
                    Text(
                        text = "Tổng cộng",
                        fontSize = 16.sp,
                        color = BrownDefault,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = total,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = OrangeDefault,
                    )
                }
                OuterShadowFilledButton(
                    text = "Thanh toán",
                    icon = Icons.Default.MonetizationOn,
                    isEnable = cart.cartItems.isNotEmpty()&& cart.cartItems.all{ it.available },
                    onClick =  onCheckOutClicked ,
                    modifier = Modifier
                )
            }
        }
    }
}

@Composable
fun CartItem(
    onQuantityIncr: () -> Unit = {},
    onQuantityDesc: () -> Unit = {},
    onDeleteClicked: () -> Unit = {},
    cartItem: CartItemResponse,
    modifier: Modifier = Modifier,
){
    Surface(
        shadowElevation = 4.dp, // Elevation applied here instead
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.padding(8.dp)
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = if(cartItem.available) WhiteDefault else GreyLight,
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
                    Column (
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
                            text = if(cartItem.available)cartItem.getPrice() else "Sản phẩm không còn khả dụng",
                            textAlign = TextAlign.End,
                            color = if(cartItem.available)OrangeDefault else GreyDefault,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .padding(bottom = 8.dp)
                    ){
                        if(cartItem.available) {
                            QuantitySelectionButton(
                                count = cartItem.quantity.toInt(),
                                onValueDecr = onQuantityDesc,
                                onValueIncr = onQuantityIncr,
                                modifier = Modifier.padding(end = 10.dp, start = 10.dp)
                                    .height(40.dp)
                            )
                        }
                        IconButton(
                            onClick = onDeleteClicked,
                            colors = IconButtonColors(
                                containerColor = OrangeDefault,
                                contentColor = WhiteDefault,
                                disabledContainerColor = GreyLight,
                                disabledContentColor = WhiteDefault,
                            ),
                            shape = RoundedCornerShape(
                                topStart = 50.dp,
                                bottomStart = 50.dp
                            ),
                            modifier = Modifier
                                .width(75.dp)
                        ) {
                            Icon(Icons.Filled.Delete, "")
                        }
                    }
                }
            }
        }
    }
}