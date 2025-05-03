package com.example.mam.gui.screen.client

import android.widget.GridLayout
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.gestures.stopScroll
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mam.R
import com.example.mam.entity.CartItem
import com.example.mam.entity.Product
import com.example.mam.entity.VarianceOption
import com.example.mam.gui.component.CircleIconButton
import com.example.mam.gui.component.MultiChoiceOption
import com.example.mam.gui.component.OuterShadowFilledButton
import com.example.mam.gui.component.PizzaSizeOption
import com.example.mam.gui.component.QuantitySelectionButton
import com.example.mam.gui.component.RadioOption
import com.example.mam.gui.component.innerShadow
import com.example.mam.gui.component.outerShadow
import com.example.mam.ui.theme.BlackDefault
import com.example.mam.ui.theme.BrownDefault
import com.example.mam.ui.theme.GreyDark
import com.example.mam.ui.theme.OrangeDefault
import com.example.mam.ui.theme.OrangeLight
import com.example.mam.ui.theme.OrangeLighter
import com.example.mam.viewmodel.client.ItemViewModel
import kotlinx.coroutines.launch

@Composable
fun ItemScreen(
    onAddClick: (CartItem) -> Unit = {CartItem -> },
    onBackClicked: () -> Unit = {},
    onCartClicked: () -> Unit = {},
    viewModel: ItemViewModel,
    modifier: Modifier = Modifier,
){
    val item: Product = viewModel.item
    val cartItem: CartItem by viewModel.cartItem.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    var quantity by remember { mutableStateOf(1) }
    var spacerheight by remember { mutableStateOf(170) }
    val lazyState = rememberLazyListState()
    val coroutine = rememberCoroutineScope()
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
    Box(Modifier.fillMaxSize().background(OrangeDefault)){
        Image(
            painter = painterResource(item.img),
            contentScale = ContentScale.Crop,
            contentDescription = null,
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
                            onValueDecr = {cartItem.quantity = quantity--},
                            onValueIncr = {cartItem.quantity = quantity++},
                            modifier = Modifier
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
                        text = item.longDescription,
                        fontSize = 20.sp,
                        color = BrownDefault,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp)
                    )
                }
                items(viewModel.loadVariance(item.id)){ option ->
                    if (option.name in listOf( "Độ dày đế bánh", "Độ chín của thịt") ){
                        var selectedOption: VarianceOption
                        RadioOption(
                            title = option.name,
                            options = viewModel.loadOption(option.id),
                            onClick = { option ->
                                selectedOption = option
                                cartItem.options.removeIf { it.idVariance == selectedOption.idVariance }
                                cartItem.options.add(selectedOption)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp)
                        )
                    }
                    else if(option.name == "Kích cỡ bánh") {
                        var sizeOption: VarianceOption
                        PizzaSizeOption(
                            title = option.name,
                            options = viewModel.loadOption(option.id),
                            onClick = { option ->
                                sizeOption = option
                                cartItem.options.removeIf { it.idVariance == sizeOption.idVariance }
                                cartItem.options.add(sizeOption)
                            },
                            image = R.drawable.ic_size_pizza,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp)
                        )
                    }
                    else {
                        MultiChoiceOption(
                            title = option.name,
                            options = viewModel.loadOption(option.id),
                            onSelect = {
                                cartItem.options.add(it)},
                            onUnselect = { tmp ->
                                cartItem.options.removeIf { it.id == tmp.id }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp)
                        )
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
                        fontSize = 20.sp,
                        color = BrownDefault,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = cartItem.getPriceToString(),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = OrangeDefault,
                    )
                }
                OuterShadowFilledButton(
                    text = "Thêm",
                    icon = Icons.Default.AddShoppingCart,
                    onClick = { viewModel.addToCart() },
                    modifier = Modifier
                )
            }
        }
    }
}

@Preview
@Composable
fun ItemPreview(){
    val viewModel: ItemViewModel = viewModel()
    ItemScreen(
        viewModel = viewModel
    )
}