package com.example.mam.gui.screen.management

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.mam.R
import com.example.mam.dto.order.OrderResponse
import com.example.mam.dto.user.UserResponse
import com.example.mam.gui.component.CircleIconButton
import com.example.mam.gui.component.outerShadow
import com.example.mam.ui.theme.BrownDefault
import com.example.mam.ui.theme.ErrorColor
import com.example.mam.ui.theme.GreyDark
import com.example.mam.ui.theme.GreyDefault
import com.example.mam.ui.theme.GreyLight
import com.example.mam.ui.theme.OrangeDefault
import com.example.mam.ui.theme.OrangeLighter
import com.example.mam.ui.theme.Typography
import com.example.mam.ui.theme.WhiteDefault
import com.example.mam.viewmodel.management.ListOrderViewModel
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun ListOrderScreen(
    viewModel: ListOrderViewModel,
    onBackClick: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    onEditOrderClick: (Long) -> Unit = {},
    isPreProcessing: Boolean = false,
    isProcessing: Boolean = false,
    mockData: List<OrderResponse>? = null,
) {
    val sortOptions = viewModel.sortingOptions.collectAsStateWithLifecycle().value
    val selectedSortingOption = viewModel.selectedSortingOption.collectAsStateWithLifecycle().value
    val searchQuery = viewModel.searchQuery.collectAsStateWithLifecycle()
    val orderList = viewModel.orders.collectAsLazyPagingItems()
    val searchHistory = viewModel.searchHistory.collectAsStateWithLifecycle().value
    val asc = viewModel.asc.collectAsStateWithLifecycle().value

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    LaunchedEffect(Unit){
        orderList.refresh() // Load the initial data
        viewModel.setOrderState(
            isProcessing = isProcessing,
            isPreProcessing = isPreProcessing
        )
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.statusBars.asPaddingValues())
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(OrangeDefault)
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
                    text = "Đơn hàng" + if(isProcessing) " đang xử lý" else if(isPreProcessing) " chưa xử lý" else "",
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
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                    ) {
                        val expanded = remember { mutableStateOf(false) }
                        val focusManager = LocalFocusManager.current
                        OutlinedTextField(
                            value = searchQuery.value,
                            onValueChange = {
                                viewModel.setSearch(it)
                                if (searchHistory.isNotEmpty()) {
                                    expanded.value = true // Chỉ mở nếu có lịch sử tìm kiếm
                                }
                            },
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = WhiteDefault,  // Màu nền khi focus
                                unfocusedContainerColor = WhiteDefault, // Màu nền khi không focus
                                focusedIndicatorColor = WhiteDefault,  // Màu viền khi focus
                                unfocusedIndicatorColor = WhiteDefault,  // Màu viền khi không focus
                                focusedTextColor = BrownDefault,       // Màu chữ khi focus
                                unfocusedTextColor = BrownDefault,      // Màu chữ khi không focus
                                cursorColor = BrownDefault             // Màu con trỏ nhập liệu
                            ),
                            singleLine = true,
                            placeholder = {
                                Text(
                                    text = "Tìm kiếm đơn hàng",
                                    color = GreyDefault,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal
                                )
                            },
                            trailingIcon = {
                                Row {
                                    IconButton(
                                        colors = IconButtonColors(
                                            containerColor = WhiteDefault,
                                            contentColor = BrownDefault,
                                            disabledContentColor = BrownDefault,
                                            disabledContainerColor = WhiteDefault
                                        ),
                                        onClick = {
                                            viewModel.setSearch("")
                                        }) {
                                        Icon(Icons.Default.Close, contentDescription = "Clear")
                                    }
                                    IconButton(
                                        colors = IconButtonColors(
                                            containerColor = GreyLight,
                                            contentColor = BrownDefault,
                                            disabledContentColor = BrownDefault,
                                            disabledContainerColor = WhiteDefault
                                        ),
                                        onClick = {
                                            viewModel.search()
                                            focusManager.clearFocus()
                                        }) {
                                        Icon(Icons.Default.Search, contentDescription = "Search")
                                    }
                                }
                            },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Done),
                            shape = RoundedCornerShape(50.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .onFocusChanged {
                                    if (it.isFocused && searchHistory.isNotEmpty()) expanded.value =
                                        true
                                },
                        )
                        DropdownMenu(
                            expanded = expanded.value,
                            onDismissRequest = { expanded.value = false },
                            containerColor = WhiteDefault,
                            properties = PopupProperties(focusable = false),
                            modifier = Modifier
                                .zIndex(1f)
                                .fillMaxWidth()
                        ) {
                            searchHistory.forEach { query ->
                                DropdownMenuItem(
                                    text = { Text(query, color = BrownDefault) } ,
                                    leadingIcon = {
                                        Icon(Icons.Default.History, contentDescription = "History", tint = BrownDefault) },
                                    contentPadding = PaddingValues(3.dp),
                                    onClick = {
                                        viewModel.setSearch(query)
                                        expanded.value = false
                                    },
                                )
                            }
                        }
                    }
                }
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .padding(start = 8.dp)) {
                        Box() {
                            var sortExpanded by remember { mutableStateOf(false) }
                            FilterChip(
                                selected = sortExpanded,
                                onClick = { sortExpanded = !sortExpanded },
                                label = { Text(selectedSortingOption) },
                                leadingIcon = {
                                    Icon(Icons.Default.Sort, contentDescription = "Sort")
                                },
                                trailingIcon = {
                                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Expand")
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    containerColor = WhiteDefault,
                                    labelColor = BrownDefault,
                                    iconColor = BrownDefault,
                                    selectedContainerColor = OrangeDefault,
                                    selectedLabelColor = WhiteDefault,
                                    selectedLeadingIconColor = WhiteDefault,
                                    selectedTrailingIconColor = WhiteDefault
                                ),
                                modifier = Modifier
                            )

                            DropdownMenu(
                                expanded = sortExpanded,
                                onDismissRequest = { sortExpanded = false },
                                containerColor = WhiteDefault,
                                modifier = Modifier
                            ) {
                                sortOptions.forEach { option ->
                                    DropdownMenuItem(
                                        text = { Text(option, color = BrownDefault) },
                                        onClick = {
                                            viewModel.setSelectedSortingOption(option)
                                            viewModel.sort()
                                            sortExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                        IconButton(
                            colors = IconButtonColors(
                                containerColor = WhiteDefault,
                                contentColor = BrownDefault,
                                disabledContentColor = BrownDefault,
                                disabledContainerColor = WhiteDefault
                            ),
                            onClick = {
                                scope.launch {
                                    viewModel.setASC()
                                    viewModel.sort()
                                }
                            },
                            modifier = Modifier.size(30.dp)
                        ) {
                            Icon(if(asc)Icons.Default.ArrowUpward else Icons.Default.ArrowDownward, contentDescription = "ASC/DESC")
                        }
                    }
                }
                if (orderList.itemCount == 0) {
                    item {
                        Text(
                            text = "Không có đơn hàng " + if(isProcessing) "đang xử lý nào" else if(isPreProcessing) "chưa xử lý nào" else   "nào",
                            color = GreyDefault,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                } else {
                    items(orderList.itemCount) { index ->
                        val order= orderList[index]
                        order?.let {
                            OrderItem(
                                order = order,
                                onEditClick = onEditOrderClick,
                                viewModel = viewModel
                            )
                        }
                    }
                    item{
                        Spacer(Modifier.height(100.dp))
                    }
                    orderList.apply {
                        when {
                            loadState.append is LoadState.Loading -> {
                                item { CircularProgressIndicator(
                                    color = OrangeDefault,
                                    modifier = Modifier.padding(16.dp)) }
                            }
                            loadState.append is LoadState.Error -> {
                                item { Text("Lỗi khi tải thêm", color = ErrorColor) }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OrderItem(
    order: OrderResponse,
    viewModel: ListOrderViewModel,
    onEditClick: (Long) -> Unit,
) {
    var owner by remember { mutableStateOf(UserResponse()) }
    var rate by remember { mutableStateOf(0) }
    LaunchedEffect(Unit) {
        owner = viewModel.loadOwnerOfOrder(order.userId)
        rate = viewModel.loadReviewOfOrder(order.id).rate
    }
    var expand by remember { mutableStateOf(false) }
    Card(
        onClick = { onEditClick(order.id) },
        colors = CardDefaults.cardColors(
            containerColor = WhiteDefault
        ),
        modifier = Modifier.padding(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 8.dp)
        ) {
            AsyncImage(
                model = owner.getRealURL(),
                contentDescription = null,
                placeholder = painterResource(R.drawable.ic_mam_logo),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(50))
                    .background(GreyLight)
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .weight(1f)
                    .padding(10.dp)
            ) {
                Text(
                    text = order.orderStatus,
                    textAlign = TextAlign.Start,
                    color = OrangeDefault,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = "#" + order.txnRef,
                    textAlign = TextAlign.Start,
                    color = OrangeDefault,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )
                Text(
                    text = owner.fullname,
                    textAlign = TextAlign.Start,
                    color = BrownDefault,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )
                Instant.parse(order.createdAt).atZone(ZoneId.systemDefault())?.let {
                    Text(
                        text = it.format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")),
                        textAlign = TextAlign.Start,
                        color = BrownDefault,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Text(
                    text = "Tổng tiền: " + order.getPriceToString(),
                    textAlign = TextAlign.Start,
                    color = BrownDefault,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            if (order.orderStatus == "PENDING" ||
                order.orderStatus == "CONFIRMED" ||
                order.orderStatus == "PROCESSING")
                IconButton(onClick = { onEditClick(order.id) }) {
                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = BrownDefault)
            }
            if (order.orderStatus == "COMPLETED" && rate != 0) {
                Text(
                    text = rate.toString(),
                    textAlign = TextAlign.Start,
                    color = BrownDefault,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                )
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "History",
                    tint = OrangeDefault,
                    modifier = Modifier
                        .padding(start = 2.dp)
                        .size(24.dp)
                        .align(Alignment.CenterVertically)
                )
            }
        }
    }
}

