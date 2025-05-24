package com.example.mam.gui.screen.management


import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mam.R
import com.example.mam.entity.ProductCategory
import com.example.mam.gui.component.CircleIconButton
import com.example.mam.gui.component.outerShadow
import com.example.mam.ui.theme.BrownDefault
import com.example.mam.ui.theme.GreyDark
import com.example.mam.ui.theme.GreyDefault
import com.example.mam.ui.theme.GreyLight
import com.example.mam.ui.theme.OrangeDefault
import com.example.mam.ui.theme.OrangeLighter
import com.example.mam.ui.theme.Typography
import com.example.mam.ui.theme.WhiteDefault
import com.example.mam.viewmodel.management.ListCategoryViewModel
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun ListCategoryScreen(
    viewModel: ListCategoryViewModel,
    onBackClick: () -> Unit = {},
    onHomeClick: () -> Unit = {},

    onAddCategoryClick: () -> Unit = {},
    onEditCategoryClick: (String) -> Unit = {},
    mockData: List<ProductCategory> ?= null
) {
    val sortOptions = viewModel.sortingOptions.collectAsStateWithLifecycle().value
    val selectedSortingOption = viewModel.selectedSortingOption.collectAsStateWithLifecycle()
    val searchQuery = viewModel.searchQuery.collectAsStateWithLifecycle()
    val categoryList = viewModel.category.collectAsStateWithLifecycle().value
    val isLoading = viewModel.isLoading.collectAsStateWithLifecycle()
    val searchHistory = viewModel.searchHistory.collectAsStateWithLifecycle().value

    LaunchedEffect(Unit) {
        viewModel.loadSortingOptions()
        viewModel.loadData()
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
                    text = "Danh mục",
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
                                    text = "Tìm kiếm danh mục",
                                    color = GreyDefault,
                                    fontSize = 16.sp,
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
                                            viewModel.searchCategory()
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
                                .onFocusChanged { if(it.isFocused && searchHistory.isNotEmpty()) expanded.value = true },
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
                    Box(Modifier
                        .fillMaxWidth(0.9f)
                        .padding(start = 8.dp)) {
                        var sortExpanded by remember { mutableStateOf(false) }
                        FilterChip(
                            selected = sortExpanded,
                            onClick = { sortExpanded = !sortExpanded },
                            label = { selectedSortingOption.value },
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
                                        sortExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
                if (mockData != null) {
                    items(mockData) { category ->
                        CategoryItem(
                            category = category,
                            onEditClick = onEditCategoryClick,
                            onDeleteClick = { }
                        )
                    }
                }
                else if (isLoading.value) {
                    item {
                        CircularProgressIndicator(
                            color = OrangeDefault,
                            modifier = Modifier
                                .padding(16.dp)
                                .size(40.dp)
                        )
                    }
                }
                else if (categoryList.isEmpty()) {
                    item {
                        Text(
                            text = "Không có danh mục nào",
                            color = GreyDefault,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
                else items(categoryList) { category ->
                    CategoryItem(
                        category = category,
                        onEditClick = onEditCategoryClick,
                        onDeleteClick = { }
                    )
                }

            }
        }
        IconButton(
            onClick = onAddCategoryClick,
            modifier = Modifier
                .padding(16.dp)
                .size(40.dp)
                .align(Alignment.BottomEnd)
                .background(OrangeDefault, RoundedCornerShape(50))
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add",
                tint = WhiteDefault
            )
        }
    }
}

@Composable
fun CategoryItem(
    category: ProductCategory,
    onEditClick: (String) -> Unit,
    onDeleteClick: (String) -> Unit,
) {
    var expand by remember { mutableStateOf(false) }
    Surface(
        shadowElevation = 4.dp, // Elevation applied here instead
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.padding(8.dp)
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = WhiteDefault
            ),
            modifier = Modifier
                .animateContentSize()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Image(
                    painter = painterResource(id = category.icon),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp).padding(end = 8.dp)
                )
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(
                        text = category.name,
                        textAlign = TextAlign.Start,
                        color = BrownDefault,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = "ID: " + category.id,
                        textAlign = TextAlign.Start,
                        color = GreyDefault,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                IconButton(onClick = { onEditClick(category.id) }) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit", tint = BrownDefault)
                }
                IconButton(onClick = { onDeleteClick(category.id) }) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = BrownDefault)
                }
                IconButton(onClick = { expand = !expand }) {
                    if (!expand) Icon(
                        Icons.Default.ExpandMore,
                        contentDescription = "Expand",
                        tint = BrownDefault
                    )
                    else Icon(
                        Icons.Default.ExpandLess,
                        contentDescription = "Collapse",
                        tint = BrownDefault
                    )
                }
            }
            if (expand) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
                            append("Mô tả: ")
                        }
                        append(category.description)},
                    color = BrownDefault,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                )
                category.createAt.atZone(ZoneId.systemDefault())?.let {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("Ngày tạo: ")
                            }
                            append(it.format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")))
                        },
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
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("Ngày cập nhật: ")
                            }
                            append(it.format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")))
                        },
                        color = GreyDefault,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .padding(start = 16.dp, bottom = 16.dp, end = 16.dp)
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun CategoryItemPreview() {
    MaterialTheme {
        CategoryItem(
            category = ProductCategory(
                id = "1",
                name = "Hamburger",
                description = "Món ăn nhanh",
                icon = R.drawable.ic_hamburger
            ),
            onEditClick = {},
            onDeleteClick = {}
        )
    }
}

@Preview
@Composable
fun CategoryScreenPreview() {
    ListCategoryScreen(
        viewModel = ListCategoryViewModel(),
        onBackClick = {},
        onAddCategoryClick = {},
        onEditCategoryClick = {},
        mockData = listOf(
            ProductCategory(
                id = "1",
                name = "Hamburger",
                icon = R.drawable.ic_hamburger
            ),
            ProductCategory(
                id = "2",
                name = "Pizza",
                icon = R.drawable.ic_pizza
            ),
            ProductCategory(
                id = "3",
                name = "Chicken",
                icon = R.drawable.ic_chicken
            )
        )
    )
}
