package com.example.mam.gui.screen.client

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clipScrollableContainer
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mam.entity.Cart
import com.example.mam.entity.CartItem
import com.example.mam.gui.component.AdditionalProduct
import com.example.mam.gui.component.CartItemContainer
import com.example.mam.gui.component.CircleIconButton
import com.example.mam.gui.component.OuterShadowFilledButton
import com.example.mam.gui.component.outerShadow
import com.example.mam.ui.theme.BrownDefault
import com.example.mam.ui.theme.GreyDark
import com.example.mam.ui.theme.OrangeDefault
import com.example.mam.ui.theme.OrangeLight
import com.example.mam.ui.theme.OrangeLighter
import com.example.mam.ui.theme.Typography
import com.example.mam.viewmodel.client.CartViewModel

//@SuppressLint("MutableCollectionMutableState")
@Composable
fun CartScreen(
    onBackClicked: () -> Unit = {},
    onCheckOutClicked: () -> Unit = {},
    onAdditionalProductClicked: (String) -> Unit = {string ->},
    viewModel: CartViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val cart: Cart by viewModel.cart.collectAsStateWithLifecycle()
    val isScreenActive = rememberUpdatedState(newValue = true)
    LaunchedEffect(isScreenActive.value) {
        viewModel.getCart("")
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
                items(cart.items) { item ->
                    CartItemContainer(
                        cartItem = item,
                        onQuantityDesc = { viewModel.descItemQuantity(cart.items.indexOf(item)) },
                        onQuantityIncr = { viewModel.incrItemQuantity(cart.items.indexOf(item)) },
                        onDeleteClicked = {viewModel.deleteItem(cart.items.indexOf(item))},
                        modifier = Modifier,
                    )
                }
                item {
                    Text(
                        text = "Đề xuất món ngon",
                        textAlign = TextAlign.Start,
                        maxLines = 2,
                        fontSize = 24.sp,
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
                        items(viewModel.loadAdditionalProduct()){ item ->
                            AdditionalProduct(
                                item = item,
                                onClick = { onAdditionalProductClicked(item.id)},
                            )

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
                modifier = Modifier.padding(10.dp).fillMaxWidth()

            ) {
                Column {
                    Text(
                        text = "Tổng cộng",
                        fontSize = 20.sp,
                        color = BrownDefault,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = cart.getTotalToString(),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = OrangeDefault,
                    )
                }
                OuterShadowFilledButton(
                    text = "Thanh toán",
                    icon = Icons.Default.AddShoppingCart,
                    onClick =  onCheckOutClicked ,
                    modifier = Modifier
                )
            }
        }
    }
}

@Preview
@Composable
fun CartPreview(){
    val viewModel: CartViewModel = viewModel()
    CartScreen(
        viewModel = viewModel
    )
}