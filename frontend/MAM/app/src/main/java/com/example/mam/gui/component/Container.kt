package com.example.mam.gui.component

import CustomShape
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mam.entity.CartItem
import com.example.mam.entity.Product
import com.example.mam.entity.ProductCategory
import com.example.mam.ui.theme.BrownDefault
import com.example.mam.ui.theme.OrangeDefault
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

){
    
}

@Preview
@Composable
fun ContainerPreview(){
    val viewModel: HomeScreenViewModel = viewModel()
    viewModel.loadListCategory()
    viewModel.loadListProduct()
    ProductContainer(category = viewModel.getListCategory()[1], products = viewModel.getListProduct(viewModel.getListCategory()[1].id), onClick = {})
}