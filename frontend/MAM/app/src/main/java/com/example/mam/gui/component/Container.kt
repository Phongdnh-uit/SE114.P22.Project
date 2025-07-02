package com.example.mam.gui.component

import CustomShape
import android.annotation.SuppressLint
import android.util.Log
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.core.os.HandlerCompat.postDelayed
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.mam.R
import com.example.mam.dto.cart.CartItemResponse
import com.example.mam.dto.order.OrderDetailResponse
import com.example.mam.dto.product.CategoryResponse
import com.example.mam.dto.product.ProductResponse
import com.example.mam.ui.theme.BrownDefault
import com.example.mam.ui.theme.GreyDefault
import com.example.mam.ui.theme.GreyLight
import com.example.mam.ui.theme.OrangeDefault
import com.example.mam.ui.theme.OrangeLight
import com.example.mam.ui.theme.WhiteDefault
import com.example.mam.viewmodel.client.HomeScreenViewModel


@Composable
fun ProductContainer(
   category: CategoryResponse,
   onClick: (ProductResponse) -> Unit = {ProductResponse -> },
   products: List<ProductResponse> = emptyList(),
   modifier: Modifier = Modifier
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .widthIn(min = 300.dp)
            .heightIn(min = 150.dp)
            .clip(shape = CustomShape()) // Điều chỉnh độ cong với giá trị curveHeight
            .background(brush = Brush.verticalGradient(
                colors = listOf( OrangeLight,WhiteDefault),
                // Light orange hex code
                )
            )

    ) {
        Spacer(modifier = Modifier.height(20.dp))

        AsyncImage(
            model = if(category.name == "Đề xuất") category.imageUrl else category.getRealURL(), // Đây là URL từ API
            contentDescription = null,
            placeholder = painterResource(R.drawable.ic_mam_logo),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(50.dp)
        )
        Text(
            text = category.name,
            fontSize = 22.sp,
            color = BrownDefault,
            modifier = Modifier
        )
        Spacer(Modifier.height(10.dp))
        if(products.isEmpty()) {
            CircularProgressIndicator(
                color = OrangeDefault,
                modifier = Modifier.padding(16.dp))
        } else
        products.forEach { product ->
                product?.let {
                    ProductClientListItem(
                        item = product,
                        onClick = {
                            onClick(product)
                        },
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                    )
                    Spacer(Modifier.height(10.dp))
                }
            }
        }
        Spacer(Modifier.height(10.dp))
}


@Composable
fun CartItemContainer(
    onQuantityIncr: () -> Unit = {},
    onQuantityDesc: () -> Unit = {},
    onDeleteClicked: () -> Unit = {},
    cartItem: CartItemResponse,
    modifier: Modifier = Modifier,
){
    Card(
        colors = CardColors(
            containerColor = WhiteDefault,
            contentColor = BrownDefault,
            disabledContainerColor = GreyLight,
            disabledContentColor = GreyDefault
        ),
        shape = RoundedCornerShape(
            topStart = 50.dp,
            topEnd = 5.dp,
            bottomStart = 5.dp,
            bottomEnd = 50.dp
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        modifier = modifier
            .height(120.dp)
            .fillMaxWidth(0.95f)
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier =
            Modifier.fillMaxWidth()
        ){
            AsyncImage(
                model = cartItem.getRealUrl(), // Đây là URL từ API
                contentDescription = null,
                placeholder = painterResource(R.drawable.ic_mam_logo),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(10.dp)
                    .size(80.dp)
                    .clip(CircleShape)
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 10.dp, start = 10.dp)
            ) {
                Text(
                    text = cartItem.productName,
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .fillMaxWidth()
                )
                Text(
                    text = "",
                    textAlign = TextAlign.Start,
                    maxLines = 2,
                    fontSize = 14.sp,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .fillMaxWidth()
                )
                Text(
                    text = cartItem.getPrice(),
                    textAlign = TextAlign.End,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    color = OrangeDefault,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 5.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .fillMaxWidth()
                ){
                    QuantitySelectionButton(
                        count = cartItem.quantity.toInt(),
                        onValueDecr = onQuantityDesc,
                        onValueIncr = onQuantityIncr,
                        modifier = Modifier.padding(end = 10.dp, start = 10.dp)
                    )
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

@Composable
fun OrderItemContainer(
    item: OrderDetailResponse,
    modifier: Modifier = Modifier,
){
    Card(
        colors = CardColors(
            containerColor = WhiteDefault,
            contentColor = BrownDefault,
            disabledContainerColor = GreyLight,
            disabledContentColor = GreyDefault
        ),
        shape = RoundedCornerShape(50.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        modifier = modifier
            .height(100.dp)
            .fillMaxWidth(0.95f)
            .clip(
                RoundedCornerShape(
                    topStart = 50.dp,
                    topEnd = 0.dp,
                    bottomStart = 0.dp,
                    bottomEnd = 50.dp
                )
            )
    ){
        Row(
            Modifier.fillMaxSize()
        ){
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxHeight()
                    .wrapContentWidth()
                    .padding(horizontal = 10.dp)) {
                AsyncImage(
                    model = item.getRealUrl(),
                    placeholder = painterResource(R.drawable.ic_mam_logo),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                        .size(80.dp)
                        .clip(CircleShape)
                )

            }
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp)
            ) {
                Column {
                    Text(
                        text = item.productName,
                        textAlign = TextAlign.Start,
                        maxLines = 2,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .align(Alignment.Start)
                            .fillMaxWidth()
                    )
                    Text(
                        text = item.variationInfo,
                        textAlign = TextAlign.Start,
                        maxLines = 3,
                        fontSize = 14.sp,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .align(Alignment.Start)
                            .fillMaxWidth()
                    )
                }
                Text(
                    text = item.getPrice(),
                    textAlign = TextAlign.End,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    color = OrangeDefault,
                    modifier = Modifier
                        .padding(end = 5.dp)
                        .fillMaxWidth())

            }

        }
    }
}

@Composable
fun AdditionalProduct(
    modifier: Modifier = Modifier,
    item: ProductResponse,
    onClick: (Long) -> Unit = {},
){
    Box(
        modifier = modifier
            .width(120.dp)
            .height(160.dp)
    )
    {
        AsyncImage(
            model = item.getRealURL(), // Đây là URL từ API
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .size(80.dp)
                .clip(CircleShape)
                .zIndex(1f)
        )
        Card(
            colors = CardColors(
                containerColor = WhiteDefault,
                contentColor = BrownDefault,
                disabledContainerColor = GreyLight,
                disabledContentColor = GreyDefault
            ),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
            modifier = Modifier
            .size(120.dp)
            .align(Alignment.BottomCenter)
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
            ) {
                Text(
                    text = item.name,
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp,
                    maxLines = 2,
                    fontWeight = FontWeight.Medium,
                    overflow = TextOverflow.Ellipsis,
                    color = BrownDefault,
                    modifier = Modifier
                        .padding(top = 45.dp, start = 10.dp, end = 10.dp)
                        .fillMaxWidth()
                        .weight(1f)
                )
                OuterShadowFilledButton(
                    text = item.getPriceToString(),
                    fontSize = 12.sp,
                    shadowColor = WhiteDefault,
                    onClick = { onClick(item.id) },
                    modifier = Modifier.padding(5.dp).height(30.dp)
                )

            }
        }
    }
}
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPaymentScreen(paymentUrl: String) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Thanh toán") }) }
    ) {
        PaymentWebViewScreen(paymentUrl = paymentUrl)
    }
}
@Composable
fun PaymentWebViewScreen(
    paymentUrl: String,
    modifier: Modifier = Modifier,
) {
    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                settings.useWideViewPort = true
                settings.loadWithOverviewMode = true
                settings.javaScriptCanOpenWindowsAutomatically = true

                webViewClient = WebViewClient()

                // Cookie config
//                CookieManager.getInstance().apply {
//                    setAcceptCookie(true)
//                    setAcceptThirdPartyCookies(this@apply, true)
//                }

                // Delay 1.5s rồi load URL
                postDelayed({
                    loadUrl(paymentUrl)
                }, 1500)
            }
        }
    )
}

