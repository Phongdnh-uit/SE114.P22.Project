package com.example.mam.screen.client

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Text
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.mam.R
import com.example.mam.component.CircleIconButton
import com.example.mam.component.MultiChoiceOption
import com.example.mam.component.OuterShadowFilledButton
import com.example.mam.component.PizzaSizeOption
import com.example.mam.component.QuantitySelectionButton
import com.example.mam.component.RadioOption
import com.example.mam.component.outerShadow
import com.example.mam.ui.theme.BrownDefault
import com.example.mam.ui.theme.OrangeDefault
import com.example.mam.ui.theme.OrangeLight
import com.example.mam.ui.theme.OrangeLighter
import com.example.mam.viewmodel.client.ItemViewModel
import kotlinx.coroutines.launch

@Composable
fun ItemScreen(
    onAddClick: () -> Unit = {},
    onBackClicked: () -> Unit = {},
    onCartClicked: () -> Unit = {},
    viewModel: ItemViewModel,
    modifier: Modifier = Modifier,
){
    val item = viewModel.item.collectAsStateWithLifecycle().value
    val quantity = viewModel.quantity.collectAsStateWithLifecycle().value
    val variances = viewModel.variances.collectAsStateWithLifecycle().value
    val optionsMap = viewModel.optionsMap.collectAsStateWithLifecycle().value
    val selectedOptions = viewModel.selectedOptions.collectAsStateWithLifecycle().value
    val cartCount = viewModel.cartCount.collectAsStateWithLifecycle().value
    var total by remember { mutableStateOf("") }

    LaunchedEffect(selectedOptions, quantity) {
        total = viewModel.getTotalPrice()
    }
    LaunchedEffect(Unit){
        viewModel.loadItemDetails()
        viewModel.loadVariances()
        viewModel.loadOptions()
        total = viewModel.getTotalPrice()
    }

    LaunchedEffect(Unit) {
        viewModel.loadCartCount()
    }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val lazyState = rememberLazyListState()
    val coroutine = rememberCoroutineScope()
    var spacerheight by remember { mutableStateOf(170) }
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                if (spacerheight != 0) coroutine.launch {
                    lazyState.dispatchRawDelta(0f)
                }
                if (lazyState.firstVisibleItemIndex == 0) {
                    val delta = available.y.toInt()
                    val newSpacerHeight = spacerheight + delta
                    val oldSpacerHeight = spacerheight
                    spacerheight = newSpacerHeight.coerceIn(0, 170)
                    return Offset(0f, (oldSpacerHeight - spacerheight).toFloat())
                }
                return Offset(0f, 0f)
            }
        }
    }
    Box(Modifier.fillMaxSize().background(OrangeDefault).padding(WindowInsets.statusBars.asPaddingValues())){
        AsyncImage(
            model = item.getRealURL(),
            contentDescription = null,
            placeholder = painterResource(R.drawable.ic_mam_logo),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .fillMaxSize()
                .scrollable(scrollState, Orientation.Vertical)
                .nestedScroll(nestedScrollConnection)
        ){
            Row(
                horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ){
                CircleIconButton(
                    backgroundColor = OrangeLighter,
                    foregroundColor = OrangeDefault,
                    icon = Icons.Filled.ArrowBack,
                    shadow = "outer",
                    onClick = onBackClicked,
                    modifier = Modifier
                        .padding(top = 16.dp, start = 16.dp)
                )
                CircleIconButton(
                    backgroundColor = OrangeLighter,
                    foregroundColor = OrangeDefault,
                    icon = Icons.Outlined.ShoppingCart,
                    isBadges = true,
                    badgesCount = cartCount,
                    shadow = "outer",
                    onClick = onCartClicked,
                    modifier = Modifier
                        .padding(top = 16.dp, end = 16.dp)
                )
            }
            Spacer(Modifier.height(spacerheight.dp))
            LazyColumn(
                state = lazyState,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(
                        topStart = 50.dp,
                        topEnd = 50.dp,
                        bottomStart = 0.dp,
                        bottomEnd = 0.dp))
                    .background(OrangeLighter, RoundedCornerShape(
                        topStart = 50.dp,
                        topEnd = 50.dp,
                        bottomStart = 0.dp,
                        bottomEnd = 0.dp))
            ) {
                item{
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .fillMaxWidth(0.8f)
                            .wrapContentHeight()
                    ) {
                        QuantitySelectionButton(
                            count = quantity,
                            onValueDecr = {viewModel.setQuantity(quantity - 1)},
                            onValueIncr = {
                                if (quantity < 99) viewModel.setQuantity(quantity + 1)
                                  else Toast.makeText(
                                        context,
                                        "Số lượng tối đa là 99",
                                        Toast.LENGTH_SHORT
                                    ).show(
                                  )},
                            modifier = Modifier.height(40.dp)
                        )
                        Text(
                            text = item.getPriceToString(),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = OrangeDefault,
                        )
                    }
                }
                item{
                    Text(
                        text = item.name,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = BrownDefault,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp)
                    )
                    Text(
                        text = item.detailDescription,
                        fontSize = 16.sp,
                        color = BrownDefault,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp)
                    )
                }
                items(variances){ option ->
                    if(option.name == "Kích cỡ" && item.categoryName == "Pizza") {
                        optionsMap[option.id]?.let {
                            LaunchedEffect(it) {
                                viewModel.selectRatioOption( it.first())
                            }
                            PizzaSizeOption(
                                title = option.name,
                                options = it,
                                onClick = { option ->
                                    viewModel.selectRatioOption(option)
                                },
                                image = R.drawable.ic_size_pizza,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp)
                            )
                        }
                    }
                    else if (!option.isMultipleChoice){
                        optionsMap[option.id]?.let {
                            LaunchedEffect(it) {
                                viewModel.selectRatioOption( it.first())
                            }
                            RadioOption(
                                title = option.name,
                                options = it,
                                onClick = { option ->
                                    viewModel.selectRatioOption(option)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp)
                            )
                        }
                    }
                    else {
                        optionsMap[option.id]?.let {
                            MultiChoiceOption(
                                title = option.name,
                                options = it,
                                onSelect = {option ->
                                    viewModel.selectOption(option)
                                },
                                onUnselect = {option ->
                                    viewModel.deselectOption(option)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp)
                            )
                        }
                    }
                }
                item{
                    Spacer(Modifier.height(100.dp))
                }
            }
        }
        Box (Modifier
            .padding(bottom = 10.dp)
            .align(Alignment.BottomCenter)
            .outerShadow()
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
                    text = "Thêm",
                    icon = Icons.Default.AddShoppingCart,
                    onClick = {
                        scope.launch{
                            if(viewModel.addToCart() == 1){
                                Toast.makeText(
                                    context,
                                    "Đã thêm sản phẩm vào giỏ hàng",
                                    Toast.LENGTH_SHORT
                                ).show()
                                onAddClick()
                                viewModel.loadCartCount()
                            } else {
                                Toast.makeText(
                                    context,
                                    "Không thể thêm sản phẩm vào giỏ hàng",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } ,
                    modifier = Modifier
                )
            }
        }
    }
}

