package com.example.mam.gui.component

import CustomShape
import android.util.Log
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mam.R
import com.example.mam.entity.CartItem
import com.example.mam.entity.OrderItem
import com.example.mam.entity.Product
import com.example.mam.entity.ProductCategory
import com.example.mam.entity.VarianceOption
import com.example.mam.ui.theme.BrownDefault
import com.example.mam.ui.theme.BrownLight
import com.example.mam.ui.theme.GreyDefault
import com.example.mam.ui.theme.GreyLight
import com.example.mam.ui.theme.OrangeDefault
import com.example.mam.ui.theme.Transparent
import com.example.mam.ui.theme.WhiteDefault
import com.example.mam.viewmodel.client.HomeScreenViewModel


@Composable
fun ProductContainer(
   category: ProductCategory,
   products: List<Product>,
   onClick: (Product) -> Unit = {Product -> },
   modifier: Modifier = Modifier
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .widthIn(min = 300.dp)
            .heightIn(min = 150.dp)
            .clip(shape = CustomShape()) // Điều chỉnh độ cong với giá trị curveHeight
            .background(WhiteDefault)

    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Image(
            painter = painterResource(category.icon),
            contentDescription = null,
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
        products.forEach { product ->
            ProductListItem(
                item = product,
                onClick = {
                    Log.d("ProductContainer", "Clicked on: ${product.name}")
                    onClick(product)},
                modifier = Modifier
                    .fillMaxWidth(0.9f)
            )
            Spacer(Modifier.height(10.dp))
        }
        Spacer(Modifier.height(10.dp))
    }
}

@Composable
fun CartItemContainer(
    onQuantityIncr: () -> Unit = {},
    onQuantityDesc: () -> Unit = {},
    onDeleteClicked: () -> Unit = {},
    cartItem: CartItem,
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
            topEnd = 0.dp,
            bottomStart = 0.dp,
            bottomEnd = 50.dp
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        modifier = modifier
            .height(160.dp)
            .fillMaxWidth(0.95f)
    ){
        Row(
            Modifier.fillMaxSize()
        ){
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(0.4f)) {
                Image(
                    painter = painterResource(cartItem.product.img),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                        .size(110.dp)
                        .clip(CircleShape)
                )
                Text(
                    text = cartItem.getPriceToString(),
                    textAlign = TextAlign.Start,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(bottom = 5.dp))
            }
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(0.dp)
            ) {
                Column(
                    modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()) {
                    Text(
                        text = cartItem.product.name,
                        textAlign = TextAlign.Start,
                        maxLines = 2,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .align(Alignment.Start)
                            .fillMaxWidth()
                    )
                    Text(
                        text = cartItem.getOptionsToString(),
                        textAlign = TextAlign.Start,
                        maxLines = 2,
                        fontSize = 18.sp,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .align(Alignment.Start)
                            .fillMaxWidth()
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp)
                ){
                    QuantitySelectionButton(
                        count = cartItem.quantity,
                        onValueDecr = onQuantityDesc,
                        onValueIncr = onQuantityIncr,
                        modifier = Modifier.height(40.dp)
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
                            .fillMaxWidth(0.8f)

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
    item: OrderItem,
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
            .height(130.dp)
            .fillMaxWidth(0.95f)
//            .clip(
//                RoundedCornerShape(
//                    topStart = 50.dp,
//                    topEnd = 0.dp,
//                    bottomStart = 0.dp,
//                    bottomEnd = 50.dp
//                )
//            )
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
                Image(
                    painter = painterResource(item.product.img),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(vertical = 10.dp)
                        .size(105.dp)
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
                        text = item.product.name,
                        textAlign = TextAlign.Start,
                        maxLines = 2,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .align(Alignment.Start)
                            .fillMaxWidth()
                    )
                    Text(
                        text = item.options,
                        textAlign = TextAlign.Start,
                        maxLines = 3,
                        fontSize = 18.sp,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .align(Alignment.Start)
                            .fillMaxWidth()
                    )
                }
                Text(
                    text = item.getPriceToString(),
                    textAlign = TextAlign.End,
                    fontSize = 18.sp,
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
    item: Product,
    onClick: (Product) -> Unit = {},
){
    Box(
        modifier = modifier
            .width(120.dp)
            .height(160.dp)
    )
    {
        Image(
            painter = painterResource(item.img),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier =Modifier
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
                )
                OuterShadowFilledButton(
                    text = item.getPriceToString(),
                    fontSize = 14.sp,
                    shadowColor = WhiteDefault,
                    onClick = { onClick(item) },
                    modifier = Modifier.padding(5.dp)
                )

            }
        }
    }
}

@Preview
@Composable
fun ContainerPreview(){
    Column(
        Modifier.fillMaxSize()
    ) {
        CartItemContainer(
            cartItem = CartItem(
                Product(
                    "P003",
                    "Pizza truyền thống",
                    "",
                    "Sốt BBQ đặc trưng, gà nướng, hành tây, ớt chuông, lá basil và phô mai Mozzarella. ",
                    100000,
                    true,
                    "PC001",
                    R.drawable.bacon_and_cheese_heaven
                ),
                1,
                mutableListOf(
                    VarianceOption("V002P003", "V001", "Thường", 0),
                    VarianceOption("V004P003", "V004", "Hành tây", 0),
                    VarianceOption("V005P003", "V004", "Ớt chuông", 0),
                    VarianceOption("V008P003", "V007", "25cm", 0),
                )
            )
        )
        AdditionalProduct(
            item = Product(
                "P003",
                "Pizza truyền thống",
                "",
                "Sốt BBQ đặc trưng, gà nướng, hành tây, ớt chuông, lá basil và phô mai Mozzarella. ",
                100000,
                true,
                "PC001",
                R.drawable.bacon_and_cheese_heaven
            )
        )
        val cartItem = CartItem(
            Product(
                "P003",
                "Pizza truyền thống",
                "",
                "Sốt BBQ đặc trưng, gà nướng, hành tây, ớt chuông, lá basil và phô mai Mozzarella. ",
                100000,
                true,
                "PC001",
                R.drawable.bacon_and_cheese_heaven
            ),
            1,
            mutableListOf(
                VarianceOption("V002P003", "V001", "Thường", 0),
                VarianceOption("V004P003", "V004", "Hành tây", 0),
                VarianceOption("V005P003", "V004", "Ớt chuông", 0),
                VarianceOption("V008P003", "V007", "25cm", 0),
            )
        )
        OrderItemContainer(
            item = OrderItem(
                Product(
                    "P003",
                    "Pizza truyền thống",
                    "",
                    "Sốt BBQ đặc trưng, gà nướng, hành tây, ớt chuông, lá basil và phô mai Mozzarella. ",
                    100000,
                    true,
                    "PC001",
                    R.drawable.bacon_and_cheese_heaven
                ),
                1,
                cartItem.getOptionsToString(),
                cartItem.getPrice()
            )
        )
    }
}