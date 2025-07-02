package com.example.mam.gui.screen.client

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clipScrollableContainer
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mam.R
import com.example.mam.dto.product.CategoryResponse
import com.example.mam.dto.product.ProductResponse
import com.example.mam.gui.component.BasicOutlinedButton
import com.example.mam.gui.component.CircleIconButton
import com.example.mam.gui.component.ProductContainer
import com.example.mam.gui.component.outerShadow
import com.example.mam.ui.theme.GreyDark
import com.example.mam.ui.theme.OrangeDefault
import com.example.mam.ui.theme.OrangeLight
import com.example.mam.ui.theme.OrangeLighter
import com.example.mam.ui.theme.Typography
import com.example.mam.viewmodel.client.HomeScreenViewModel
import kotlinx.coroutines.launch

@SuppressLint("MissingPermission")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    onItemClicked: (ProductResponse) -> Unit = {ProductResponse -> },
    onNotificationClicked: () -> Unit = {},
    onCartClicked: () -> Unit = {},
    onShippingClicked: () -> Unit = {},
    onProfileClicked: () -> Unit = {},
    onSearchClicked: () -> Unit = {},
    viewmodel: HomeScreenViewModel = viewModel(),
    modifier: Modifier = Modifier
){
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val categories: List<CategoryResponse> = viewmodel.getListCategory()
    val cartCount = viewmodel.cartCount.collectAsState().value
    val notificationCount = viewmodel.notificationCount.collectAsState().value
    val productMap by viewmodel
        .productMap
        .collectAsState()

    val childListState = rememberLazyListState()
    val rowListState = rememberLazyListState()
    val isRowScrolled = remember {
        derivedStateOf {
            val lastItemIndex = childListState.layoutInfo.totalItemsCount
            val firstVisibleItemIndex = childListState.firstVisibleItemIndex
            val visibleItems = childListState.layoutInfo.visibleItemsInfo
            val lastItemOffset = visibleItems.lastOrNull()?.offset ?: 0

            if (visibleItems.any { it.index == lastItemIndex -1}) {
                lastItemIndex - 3 // Nếu item cuối cùng đang hiển thị, trả về index cuối cùng
            } else {
                firstVisibleItemIndex // Nếu chưa lướt đến cuối, trả về index của item đầu tiên hiển thị
            }
        }
    }
    val permissionRequest = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            if (!permissions.values.all { it }) {
                //handle permission denied
            }
            else {

            }
        }
    )
    LaunchedEffect(Unit) {
        try {
            // Use Android's FusedLocationProviderClient for location retrieval
            val fusedLocationClient = com.google.android.gms.location.LocationServices.getFusedLocationProviderClient(context)
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    viewmodel.setAdressAndCoordinates(
                        address = getAddressFromCoordinates(context, it.latitude, it.longitude),
                        longitude = it.longitude,
                        latitude = it.latitude
                    )
                } ?: run {
                    // Handle case where location is null
                }
            }.addOnFailureListener {
                // Handle failure to retrieve location
            }
        } catch (e: Exception) {
            when (e) {
                is SecurityException -> {
                    permissionRequest.launch(
                        arrayOf(
                            android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                }
                else -> {
                    Toast(context).apply {
                        setText("Không thể lấy vị trí hiện tại")
                        show()
                    }
                }
            }
        }
    }

    LaunchedEffect(key1 = childListState) {
        snapshotFlow { childListState.firstVisibleItemIndex }
            .collect { index ->
                // Scroll the LazyRow to the corresponding category
                if (index >= 3 && index < categories.size + 3) { // Adjust for header and image
                        rowListState.animateScrollToItem(index - 3) // Adjust index for categories
                }
            }
    }

    LaunchedEffect(LocalLifecycleOwner.current) {
        viewmodel.loadListCategory()
        viewmodel.loadAdditionalProduct()
        viewmodel.loadCartCount()
        viewmodel.loadNotificationCount()
    }
    LaunchedEffect(key1 = categories, key2 = LocalLifecycleOwner.current) {
        if(categories.isNotEmpty() && productMap.isEmpty()){
            viewmodel.loadAllProductsFromCategories(categories)
        }
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .clipScrollableContainer(Orientation.Vertical)
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
                    icon = Icons.Filled.NotificationsNone,
                    shadow = "outer",
                    badgesCount = notificationCount,
                    isBadges = true,
                    onClick = onNotificationClicked,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(top = 16.dp, start = 16.dp)
                )
                Text(
                    text = "Trang chủ",
                    style = Typography.titleLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 17.dp)
                )
                CircleIconButton(
                    backgroundColor = OrangeLighter,
                    foregroundColor = OrangeDefault,
                    icon = Icons.Default.Search,
                    shadow = "outer",
                    onClick = onSearchClicked,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 16.dp, end = 16.dp)
                )
            }
            LazyColumn(
                state = childListState,
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
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .padding(top = 20.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_mam_foreground),
                            contentDescription = "MAM",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth(0.9f)
                                .aspectRatio(3f)
                                .clip(shape = RoundedCornerShape(10.dp))
                                .background(OrangeDefault)

                        )
                    }
                }
                stickyHeader{
                    Spacer(Modifier
                        .height(10.dp)
                        .background(OrangeLighter)
                        .fillMaxWidth())
                    LazyRow(
                        state = rowListState,
                        modifier = Modifier
                            .clipToBounds()
                            .background(OrangeLighter)
                    ) {

                        items(categories) { category ->
                            Spacer(Modifier.width(5.dp))
                            BasicOutlinedButton(
                                text = category.name,
                                url = category.getRealURL(),
                                onClick = {
                                    scope.launch {
                                    val targetIndex = categories.indexOf(category)
                                    childListState.scrollToItem(targetIndex + 3)}

                                },
                                isEnable = !(categories.indexOf(category) == isRowScrolled.value -3),
                                modifier = Modifier
                            )
                            Spacer(Modifier.width(5.dp))
                        }
                    }
                }
                item{
                    ProductContainer(
                        category = CategoryResponse(
                            name = "Đề xuất",
                            description = "Danh sách sản phẩm đề xuất",
                            imageUrl = "https://em-content.zobj.net/source/apple/81/glowing-star_1f31f.png"
                        ),
                        products = viewmodel.recommendedProducts.collectAsState().value,
                        onClick = { product -> onItemClicked(product) },
                        modifier = Modifier.fillMaxWidth(0.9f)
                    )
                }
                items(categories) { category ->
                    val products = productMap[category.id] ?: emptyList()
                    Spacer(Modifier.height(70.dp))
                    ProductContainer(
                        category = category,
                        products = products,
                        onClick = { product -> onItemClicked(product) },
                        modifier = Modifier.fillMaxWidth(0.9f)
                    )

                }
                item{Spacer(Modifier.height(60.dp))}
            }
        }
        Box (Modifier
            .padding(bottom = 10.dp)
            .align(Alignment.BottomCenter)
            .outerShadow()
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth(0.7f)
                    .background(color = OrangeLight, shape = RoundedCornerShape(50.dp))
                    .padding(horizontal = 10.dp)

            ) {
                CircleIconButton(
                    backgroundColor = OrangeLight,
                    foregroundColor = OrangeDefault,
                    icon = Icons.Outlined.Home,
                    onClick = {
                        scope.launch {
                            childListState.scrollToItem(0)
                            viewmodel.loadListCategory()
                            viewmodel.loadAllProductsFromCategories(categories)
                        }
                    },
                    modifier = Modifier
                )
                CircleIconButton(
                    backgroundColor = OrangeLight,
                    foregroundColor = OrangeDefault,
                    icon = Icons.Outlined.ShoppingCart,
                    badgesCount = cartCount,
                    isBadges = true,
                    onClick = onCartClicked,
                    modifier = Modifier
                )
                CircleIconButton(
                    backgroundColor = OrangeLight,
                    foregroundColor = OrangeDefault,
                    icon = Icons.Outlined.ShoppingBag,
                    onClick = onShippingClicked,
                    modifier = Modifier
                )
                CircleIconButton(
                    backgroundColor = OrangeLight,
                    foregroundColor = OrangeDefault,
                    icon = Icons.Outlined.Person,
                    onClick = onProfileClicked,
                    modifier = Modifier
                )
            }
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview(){
    HomeScreen()
}