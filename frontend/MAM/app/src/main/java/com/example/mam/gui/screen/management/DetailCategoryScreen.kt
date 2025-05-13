package com.example.mam.gui.screen.management

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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mam.R
import com.example.mam.entity.Product
import com.example.mam.entity.ProductCategory
import com.example.mam.gui.component.CircleIconButton
import com.example.mam.gui.component.outerShadow
import com.example.mam.ui.theme.BrownDefault
import com.example.mam.ui.theme.GreyDark
import com.example.mam.ui.theme.GreyDefault
import com.example.mam.ui.theme.OrangeDefault
import com.example.mam.ui.theme.OrangeLight
import com.example.mam.ui.theme.OrangeLighter
import com.example.mam.ui.theme.Typography
import com.example.mam.ui.theme.WhiteDefault
import com.example.mam.viewmodel.management.DetailCategoryViewModel
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun DetailCategoryScreen(
    viewModel: DetailCategoryViewModel,
    onBackClick: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    onProductClick: (String) -> Unit = {},
    onEditCategoryClick: (String) -> Unit = {},
    onDeleteCategoryClick: (String) -> Unit = {},
    mockCategory: ProductCategory? = null,
    mockProducts: List<Product> ?= emptyList()
) {
    val category = if(mockCategory !=null ) remember { mutableStateOf(mockCategory) }
    else viewModel.category.collectAsStateWithLifecycle()
    val isLoading = viewModel.isLoading.collectAsStateWithLifecycle()
    val products = viewModel.products.collectAsStateWithLifecycle()
    val isLoadingProducts = viewModel.isLoadingProducts.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadCategory()
        viewModel.loadProducts()
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
            CircleIconButton(
                backgroundColor = OrangeLighter,
                foregroundColor = OrangeDefault,
                icon = Icons.Outlined.Home,
                shadow = "outer",
                onClick = onHomeClick,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 16.dp, top = 16.dp)
            )
            Text(
                text = "Chi tiết Danh mục",
                style = Typography.titleLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 17.dp)
            )
        }
        Spacer(modifier = Modifier.size(20.dp))
        LazyColumn(
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
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = WhiteDefault
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 4.dp
                    ),
                    modifier = Modifier.padding(16.dp)
                ) {
                    if(isLoading.value) {
                        CircularProgressIndicator(
                            color = OrangeDefault,
                            modifier = Modifier
                                .padding(16.dp)
                                .size(40.dp)
                        )
                    } else {
                        category.value?.let { category ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp, vertical = 8.dp).fillMaxWidth()
                                ) {
                                    Image(
                                        painter = painterResource(id = category.icon),
                                        contentDescription = null,
                                        modifier = Modifier.size(60.dp)
                                    )
                                    Column(
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.Start,
                                        modifier = Modifier
                                            .weight(1f)
                                    ) {
                                        Text(
                                            text = "#${category.id}",
                                            color = GreyDefault,
                                            fontSize = 14.sp,
                                            textAlign = TextAlign.Start,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            fontWeight = FontWeight.SemiBold,
                                            modifier = Modifier
                                                .padding(horizontal = 10.dp)
                                                .fillMaxWidth()
                                        )
                                        Text(
                                            text = category.name,
                                            textAlign = TextAlign.Start,
                                            color = BrownDefault,
                                            fontSize = 18.sp,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            fontWeight = FontWeight.SemiBold,
                                            modifier = Modifier
                                                .padding(horizontal =  10.dp)
                                                .fillMaxWidth()
                                        )
                                    }
                                }
                                Text(
                                    text = "Mô tả:",
                                    color = BrownDefault,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Start,
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp)
                                        .fillMaxWidth()
                                )
                                Text(
                                    text = category.description,
                                    color = BrownDefault,
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.Start,
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp)
                                        .fillMaxWidth()
                                )
                                category.createAt.atZone(ZoneId.systemDefault())?.let {
                                    Text(
                                        text = "Ngày tạo: ${it.format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy"))}",
                                        color = GreyDefault,
                                        fontSize = 16.sp,
                                        textAlign = TextAlign.Start,
                                        modifier = Modifier
                                            .padding(horizontal = 16.dp)
                                            .fillMaxWidth()
                                    )
                                }
                                category.updateAt.atZone(ZoneId.systemDefault())?.let {
                                    Text(
                                        text = "Ngày cập nhật: ${it.format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy"))}",
                                        color = GreyDefault,
                                        fontSize = 16.sp,
                                        textAlign = TextAlign.Start,
                                        modifier = Modifier
                                            .padding(horizontal = 16.dp)
                                            .fillMaxWidth()
                                    )
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.End,
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp, vertical = 8.dp)
                                        .fillMaxWidth()
                                ) {
                                    IconButton(onClick = { onEditCategoryClick(category.id) }) {
                                        Icon(
                                            Icons.Default.Edit,
                                            contentDescription = "Edit",
                                            tint = BrownDefault
                                        )
                                    }
                                    IconButton(onClick = { onDeleteCategoryClick(category.id) }) {
                                        Icon(
                                            Icons.Default.Delete,
                                            contentDescription = "Delete",
                                            tint = BrownDefault
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .background(
                            color = OrangeLight,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Sản phẩm trong danh mục",
                        color = BrownDefault,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth()
                    )
                    if (mockProducts != null) {
                        mockProducts.forEach() { product ->
                            ProductItem(
                                product = product,
                                isViewOnly = true,
                                onProductClick = { onProductClick(product.id) },
                                onEditProductClick = {},
                                onDeleteProductClick = {},
                            )
                        }
                    } else if (isLoadingProducts.value) {
                        CircularProgressIndicator(
                            color = OrangeDefault,
                            modifier = Modifier
                                .padding(16.dp)
                                .size(40.dp)
                        )
                    } else if (products.value.isEmpty()) {
                        Text(
                            text = "Chưa có sản phẩm nào trong danh mục này",
                            style = Typography.bodyLarge,
                            modifier = Modifier.padding(16.dp)
                        )
                    } else {
                        products.value.forEach() { product ->
                            ProductItem(
                                product = product,
                                isViewOnly = true,
                                onProductClick = { onProductClick(product.id) },
                                onEditProductClick = {},
                                onDeleteProductClick = {},
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun DetailCategoryScreenPreview() {
    DetailCategoryScreen(
        viewModel = DetailCategoryViewModel(
            savedStateHandle = SavedStateHandle(mapOf("idCategory" to "1"))
        ),
        onBackClick = {},
        onProductClick = {},
        onEditCategoryClick = {},
        onDeleteCategoryClick = {},
        mockCategory = ProductCategory(
            id = "1",
            name = "Danh mục 1",
            description = "Đây là danh mục 1",
            icon = R.drawable.ic_hamburger,
            createAt = Instant.now() ,
            updateAt = Instant.now(),
        ),
        mockProducts = listOf(
            Product(
                id = "1",
                name = "Sản phẩm 1",
                originalPrice = 10000,
                idCategory = "1",
                img = R.drawable.bacon_and_cheese_heaven
            ),
            Product(
                id = "2",
                name = "Sản phẩm 2",
                originalPrice = 20000,
                idCategory = "1",
                img = R.drawable.bacon_and_cheese_heaven
            )
        )
    )
}