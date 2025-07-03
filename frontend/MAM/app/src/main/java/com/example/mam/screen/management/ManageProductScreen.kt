package com.example.mam.screen.management

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.core.app.ActivityCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.mam.R
import com.example.mam.dto.variation.VariationOptionRequest
import com.example.mam.dto.variation.VariationRequest
import com.example.mam.component.CircleIconButton
import com.example.mam.component.outerShadow
import com.example.mam.ui.theme.BrownDefault
import com.example.mam.ui.theme.ErrorColor
import com.example.mam.ui.theme.GreenDefault
import com.example.mam.ui.theme.GreyDark
import com.example.mam.ui.theme.GreyDefault
import com.example.mam.ui.theme.GreyLight
import com.example.mam.ui.theme.OrangeDefault
import com.example.mam.ui.theme.OrangeLight
import com.example.mam.ui.theme.OrangeLighter
import com.example.mam.ui.theme.Typography
import com.example.mam.ui.theme.WhiteDefault
import com.example.mam.viewmodel.management.ManageProductViewModel
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ManageProductScreen(
    viewModel: ManageProductViewModel,
    onBackClick: () -> Unit,
    isPreview: Boolean = false,
    isAdd: Boolean = false,
    isEdit: Boolean = false,
) {
    val productId = viewModel.productID.collectAsStateWithLifecycle().value
    val createAt = viewModel.createdAt.collectAsStateWithLifecycle().value
    val updateAt = viewModel.updatedAt.collectAsStateWithLifecycle().value
    val productName = viewModel.productName.collectAsStateWithLifecycle().value
    val productShortDescription = viewModel.productShortDescription.collectAsStateWithLifecycle().value
    val productLongDescription = viewModel.productLongDescription.collectAsStateWithLifecycle().value
    val productPrice = viewModel.productPrice.collectAsStateWithLifecycle().value
    val productCategory = viewModel.productCategory.collectAsStateWithLifecycle().value
    val productImageUrl = viewModel.productImageUrl.collectAsStateWithLifecycle().value
    val isAvailable = viewModel.isAvailable.collectAsStateWithLifecycle().value
    val variants = viewModel.variants.collectAsStateWithLifecycle().value
    val isLoading = viewModel.isLoading.collectAsStateWithLifecycle().value
    val isSetting = viewModel.isSetting.collectAsStateWithLifecycle().value
    val categoryList = viewModel.categoryList.collectAsStateWithLifecycle().value

    var isEditMode by remember { mutableStateOf(isEdit) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val activity = context as? Activity
    val imagePicker = if (!isPreview) {
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                viewModel.setProductImageUrl(it.toString())
                viewModel.setProductImageFile(context, uri)
            }
        }
    } else null

    LaunchedEffect(key1 = variants) {
        viewModel.loadVariants(productId)
    }
    LaunchedEffect(key1 = isEditMode) {
        if (isPreview) {
            viewModel.mockData()
        }
        else if (!isAdd) {
            viewModel.loadData()
        }
        viewModel.setCategoryList()
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
                onClick = {
                    onBackClick()
                },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 16.dp, start = 16.dp)
            )
            val isButtonEnable = (isAdd || isEditMode) &&
                productName.isNotEmpty()
                        && (productPrice.isNotEmpty() && productPrice.toBigDecimal() > BigDecimal.ZERO)
                        && productShortDescription.isNotEmpty()
                        && productLongDescription.isNotEmpty()
                        && viewModel.isProductNameValid().isEmpty()
                        && viewModel.isProductPriceValid().isEmpty()
                        && viewModel.isProductShortDescriptionValid().isEmpty()
                        && viewModel.isProductLongDescriptionValid().isEmpty()
            CircleIconButton(
                backgroundColor = OrangeLighter,
                foregroundColor = OrangeDefault,
                icon = if(isEditMode || isAdd) Icons.Default.Done else Icons.Default.Edit,
                shadow = "outer",
                onClick = {
                    if (isEditMode && isButtonEnable) {
                        scope.launch {
                            if (viewModel.updateProduct() == 1){
                                Toast.makeText(
                                    context,
                                    "Sửa sản phẩm thành công",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            else{
                                Toast.makeText(
                                    context,
                                    "Sửa sản phẩm thất bại",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            isEditMode = !isEditMode
                        }
                    } else if (isAdd && isButtonEnable) {
                        scope.launch{
                            if (viewModel.addProduct() == 1){
                                Toast.makeText(
                                    context,
                                    "Thêm sản phẩm thành công",
                                    Toast.LENGTH_SHORT
                                ).show()
                                onBackClick()
                            }
                            else{
                                Toast.makeText(
                                    context,
                                    "Thêm sản phẩm thất bại",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                    else {
                        isEditMode = !isEditMode
                    }

                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 16.dp, top = 16.dp)
            )
            Text(
                text = if (isAdd) "Thêm sản phẩm" else if (isEditMode) "Chỉnh sửa sản phẩm" else "Chi tiết sản phẩm",
                style = Typography.titleLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 17.dp)
            )
        }
        Spacer(modifier = Modifier.size(20.dp))
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
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
            if (isLoading){
                item {
                    CircularProgressIndicator(
                        color = OrangeDefault,
                        modifier = Modifier
                            .padding(16.dp)
                            .size(40.dp)
                    )
                }
            }
            else {
                item {
                    AsyncImage(
                        model = productImageUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(R.drawable.upload_image),
                        error = null,
                        modifier = Modifier
                            .fillMaxWidth(0.8f) // Set width to 80% of screen width
                            .aspectRatio(1f)
                            .padding(8.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .then(
                                if (isEditMode || isAdd) {
                                    Modifier.clickable {
                                        val permission =
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                                android.Manifest.permission.READ_MEDIA_IMAGES
                                            } else {
                                                android.Manifest.permission.READ_EXTERNAL_STORAGE
                                            }
                                        if (context.checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED && activity != null) {
                                            ActivityCompat.requestPermissions(
                                                activity,
                                                arrayOf(permission),
                                                123
                                            )
                                        } else {
                                            imagePicker?.launch("image/*")
                                        }
                                    }
                                } else Modifier
                            )

                    )
                }
                if (!isAdd) item {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("ID: ")
                            }
                            append(productId.toString())
                        },
                        textAlign = TextAlign.Start,
                        color = GreyDefault,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth()
                    )
                    createAt.atZone(ZoneId.systemDefault()).let {
                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("Ngày tạo: ")
                                }
                                append(it.format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")))
                            },
                            textAlign = TextAlign.Start,
                            color = GreyDefault,
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .fillMaxWidth()

                        )
                    }
                    updateAt.atZone(ZoneId.systemDefault()).let {
                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("Ngày cập nhật: ")
                                }
                                append(it.format(DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")))
                            },
                            textAlign = TextAlign.Start,
                            color = GreyDefault,
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .fillMaxWidth()
                        )
                    }
                }
                item {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ){
                        Box(Modifier.zIndex(1f)
                        ) {
                            var categoryExpanded by remember { mutableStateOf(false) }
                            FilterChip(
                                selected = categoryExpanded,
                                onClick = { if (isEditMode || isAdd ) categoryExpanded = !categoryExpanded },
                                label = {
                                    Text(
                                        text = categoryList.find { it.first == productCategory }?.second
                                            ?: "Chọn danh mục",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier
                                    )
                                },
                                trailingIcon = {
                                    if (isEditMode || isAdd ) Icon(if (!categoryExpanded) Icons.Default.ArrowDropDown else Icons.Default.ArrowDropUp , contentDescription = "Expand")
                                },
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled = true,
                                    selected = categoryExpanded,
                                    borderWidth = 1.dp,
                                    borderColor = GreyDefault,
                                    selectedBorderColor = OrangeDefault
                                ),
                                colors = FilterChipDefaults.filterChipColors(
                                    containerColor = OrangeLighter,
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
                                expanded = categoryExpanded,
                                onDismissRequest = { categoryExpanded = false },
                                containerColor = WhiteDefault,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            ) {
                                categoryList.forEach { category ->
                                    DropdownMenuItem(
                                        text = { Text(category.second, color = BrownDefault) },
                                        onClick = {
                                            viewModel.setProductCategory(category.first)
                                            categoryExpanded = false
                                        },
                                    )
                                }
                            }
                        }
                        Box(Modifier
                            .zIndex(1f)
                        ) {
                            var availabelExpanded by remember { mutableStateOf(false) }
                            FilterChip(
                                selected = availabelExpanded,
                                onClick = { },
                                label = {
                                    Text(
                                        text =  "Có sẵn",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier
                                    )
                                },
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled = true,
                                    selected = availabelExpanded,
                                    borderWidth = 1.dp,
                                    borderColor = if (isAvailable) GreenDefault else ErrorColor,
                                    selectedBorderColor = OrangeDefault
                                ),
                                colors = FilterChipDefaults.filterChipColors(
                                    containerColor = if (isAvailable) GreenDefault else ErrorColor,
                                    labelColor = WhiteDefault,
                                    iconColor = WhiteDefault,
                                    selectedContainerColor = OrangeDefault,
                                    selectedLabelColor = WhiteDefault,
                                    selectedLeadingIconColor = WhiteDefault,
                                    selectedTrailingIconColor = WhiteDefault
                                ),
                                modifier = Modifier
                            )
                        }
                    }
                }
                item {
                    OutlinedTextField(
                        value = productName,
                        readOnly = !(isEditMode || isAdd),
                        onValueChange = {
                            viewModel.setProductName(it)
                        },
                        textStyle = TextStyle(
                            color = BrownDefault,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,

                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BrownDefault,
                            unfocusedBorderColor = GreyDefault,
                        ),
                        singleLine = true,
                        label = {
                            Text(
                                text = "Tên sản phẩm",
                                color = BrownDefault,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier
                            )
                        },
                        supportingText = {
                            if (viewModel.isProductNameValid().isNotEmpty() && productName.isNotEmpty()) {
                                Text(
                                    text = viewModel.isProductNameValid(),
                                    color = ErrorColor,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal,
                                    modifier = Modifier
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done),
                        shape = RoundedCornerShape(20.dp),

                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)

                    )
                }
                item {
                    OutlinedTextField(
                        value = productPrice,
                        readOnly = !(isEditMode || isAdd),
                        onValueChange = {
                            viewModel.setProductPrice(it)
                        },
                        textStyle = TextStyle(
                            color = BrownDefault,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,

                            ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BrownDefault,
                            unfocusedBorderColor = GreyDefault,
                        ),
                        singleLine = true,
                        label = {
                            Text(
                                text = "Giá sản phẩm (VND)",
                                color = BrownDefault,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier
                            )
                        },
                        supportingText = {
                            if (viewModel.isProductPriceValid().isNotEmpty()) {
                                Text(
                                    text = viewModel.isProductPriceValid(),
                                    color = ErrorColor,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal,
                                    modifier = Modifier
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)

                    )
                }
                item {
                    OutlinedTextField(
                        value = productShortDescription,
                        readOnly = !(isEditMode || isAdd),
                        onValueChange = {
                            viewModel.setProductShortDescription(it)
                        },
                        textStyle = TextStyle(
                            color = BrownDefault,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,

                            ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BrownDefault,
                            unfocusedBorderColor = GreyDefault,
                        ),
                        singleLine = false,
                        label = {
                            Text(
                                text = "Mô tả ngắn",
                                color = BrownDefault,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier
                            )
                        },
                        supportingText = {
                            if (viewModel.isProductShortDescriptionValid().isNotEmpty() && productShortDescription.isNotEmpty()) {
                                Text(
                                    text = viewModel.isProductShortDescriptionValid(),
                                    color = ErrorColor,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal,
                                    modifier = Modifier
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done),
                        shape = RoundedCornerShape(20.dp),

                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)

                    )
                }
                item {
                    OutlinedTextField(
                        value = productLongDescription,
                        readOnly = !(isEditMode || isAdd),
                        onValueChange = {
                            viewModel.setProductLongDescription(it)
                        },
                        textStyle = TextStyle(
                            color = BrownDefault,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,

                            ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BrownDefault,
                            unfocusedBorderColor = GreyDefault,
                        ),
                        singleLine = false,
                        label = {
                            Text(
                                text = "Mô tả chi tiết",
                                color = BrownDefault,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier
                            )
                        },
                        supportingText = {
                            if (viewModel.isProductLongDescriptionValid().isNotEmpty() && productLongDescription.isNotEmpty()) {
                                Text(
                                    text = viewModel.isProductLongDescriptionValid(),
                                    color = ErrorColor,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal,
                                    modifier = Modifier
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done),
                        shape = RoundedCornerShape(20.dp),

                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)

                    )
                }
                if(variants.isNotEmpty() || isEditMode) {
                    item {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
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
                                text = "Danh sách tùy chọn",
                                color = BrownDefault,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                            variants.forEach { (variance, options) ->
                                HorizontalDivider(
                                    color = BrownDefault,
                                    thickness = 1.dp,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                )
                                OutlinedTextField(
                                    value = variance.name,
                                    onValueChange = { },
                                    readOnly = true,
                                    textStyle = TextStyle(
                                        color = BrownDefault,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Normal,
                                    ),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = BrownDefault,
                                        unfocusedBorderColor = GreyDefault,
                                        focusedContainerColor = WhiteDefault,
                                        unfocusedContainerColor = WhiteDefault,
                                    ),
                                    trailingIcon = {
                                        if (isEditMode || isAdd) {
                                            IconButton(
                                                colors = IconButtonColors(
                                                    containerColor = WhiteDefault,
                                                    contentColor = BrownDefault,
                                                    disabledContentColor = GreyLight,
                                                    disabledContainerColor = WhiteDefault
                                                ),
                                                onClick = {
                                                    scope.launch {
                                                        if (viewModel.removeVariant(variance.id) == 1) {
                                                            Toast.makeText(
                                                                context,
                                                                "Đã xóa tùy chọn ${variance.name}",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                        }
                                                        else {
                                                            Toast.makeText(
                                                                context,
                                                                "Không thể xóa tùy chọn ${variance.name}",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                        }
                                                    }
                                                }) {
                                                Icon(Icons.Default.Remove, contentDescription = "Remove")
                                            }
                                        }
                                    },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions.Default.copy(
                                        keyboardType = KeyboardType.Text,
                                        imeAction = ImeAction.Done),
                                    shape = RoundedCornerShape(50),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 16.dp, end = 16.dp)
                                )
                                if (isEditMode || isAdd) Row (
                                    horizontalArrangement = Arrangement.End,
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 16.dp, end = 16.dp)
                                ) {
                                    Text(
                                        text = "Chọn nhiều giá trị",
                                        color = BrownDefault,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Normal,
                                        modifier = Modifier
                                            .padding(end = 8.dp)
                                    )
                                    Switch(
                                        checked = variance.isMultipleChoice,
                                        onCheckedChange = {
                                            scope.launch {
                                                if(viewModel.updateVariantIsMultipleChoice(variance)==1){
                                                    Toast.makeText(
                                                        context,
                                                        "Đã cập nhật tùy chọn ${variance.name} thành ${if (it) "Chọn nhiều giá trị" else "Chọn một giá trị"}",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                                else {
                                                    Toast.makeText(
                                                        context,
                                                        "Không thể cập nhật tùy chọn ${variance.name}",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }
                                        },
                                        colors = SwitchDefaults.colors(
                                            checkedTrackColor = OrangeDefault,
                                            uncheckedTrackColor = GreyLight,
                                            checkedThumbColor = WhiteDefault,
                                            uncheckedThumbColor = WhiteDefault
                                        ),
                                    )
                                }
                                options.forEach(){ option ->
                                    OutlinedTextField(
                                        value = option.value,
                                        onValueChange = { },
                                        readOnly = true,
                                        textStyle = TextStyle(
                                            color = BrownDefault,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Normal,
                                            ),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = BrownDefault,
                                            unfocusedBorderColor = GreyDefault,
                                            focusedContainerColor = WhiteDefault,
                                            unfocusedContainerColor = WhiteDefault,
                                        ),
                                        trailingIcon = {
                                            if (isEditMode || isAdd) {
                                                IconButton(
                                                    colors = IconButtonColors(
                                                        containerColor = WhiteDefault,
                                                        contentColor = BrownDefault,
                                                        disabledContentColor = GreyLight,
                                                        disabledContainerColor = WhiteDefault
                                                    ),
                                                    onClick = {
                                                        scope.launch {
                                                            if(viewModel.removeVariantOption(option.id) ==1){
                                                                Toast.makeText(
                                                                    context,
                                                                    "Đã xóa giá trị tùy chọn ${option.value}",
                                                                    Toast.LENGTH_SHORT
                                                                ).show()
                                                            } else {
                                                                Toast.makeText(
                                                                    context,
                                                                    "Không thể xóa giá trị tùy chọn ${option.value}",
                                                                    Toast.LENGTH_SHORT
                                                                ).show()
                                                            }
                                                        }
                                                    }) {
                                                    Icon(Icons.Default.Remove, contentDescription = "Remove")
                                                }
                                            } else {
                                                null
                                            }
                                       },
                                        singleLine = true,
                                        keyboardOptions = KeyboardOptions.Default.copy(
                                            keyboardType = KeyboardType.Text,
                                            imeAction = ImeAction.Done),
                                        shape = RoundedCornerShape(50),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(start = 32.dp, end = 16.dp)
                                    )
                                }

                                if (isEditMode || isAdd) {
                                    var variantOpion by remember { mutableStateOf("") }
                                    OutlinedTextField(
                                        value = variantOpion,
                                        onValueChange = {
                                            variantOpion = it
                                        },
                                        textStyle = TextStyle(
                                            color = BrownDefault,
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Normal,

                                            ),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedBorderColor = BrownDefault,
                                            unfocusedBorderColor = GreyDefault,
                                            focusedContainerColor = WhiteDefault,
                                            unfocusedContainerColor = WhiteDefault,
                                            ),
                                        singleLine = true,
                                        placeholder = {
                                            Text(
                                                text = "Thêm giá trị tùy chọn",
                                                color = GreyDefault,
                                                fontWeight = FontWeight.Normal
                                            )
                                        },
                                        trailingIcon = {
                                            IconButton(
                                                colors = IconButtonColors(
                                                    containerColor = WhiteDefault,
                                                    contentColor = BrownDefault,
                                                    disabledContentColor = GreyLight,
                                                    disabledContainerColor = WhiteDefault
                                                ),
                                                enabled = variantOpion.isNotEmpty(),
                                                onClick = {
                                                    scope.launch {
                                                        if(viewModel.addVariantOption(
                                                            VariationOptionRequest(
                                                                value = variantOpion,
                                                                additionalPrice = 0.0,
                                                                variationId = variance.id
                                                            )
                                                        ) == 1){
                                                            Toast.makeText(
                                                                context,
                                                                "Đã thêm giá trị tùy chọn ${variantOpion}",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                            variantOpion = ""
                                                        } else {
                                                            Toast.makeText(
                                                                context,
                                                                "Không thể thêm giá trị tùy chọn ${variantOpion}",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                        }

                                                    }
                                                }) {
                                                Icon(Icons.Default.Add, contentDescription = "Add")
                                            }
                                        },
                                        keyboardOptions = KeyboardOptions.Default.copy(
                                            keyboardType = KeyboardType.Text,
                                            imeAction = ImeAction.Done),
                                        shape = RoundedCornerShape(50),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(start = 32.dp, end = 16.dp)
                                    )
                                }
                            }
                            if (isEditMode || isAdd) {
                                HorizontalDivider(
                                    color = BrownDefault,
                                    thickness = 1.dp,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                )
                                var variant by remember { mutableStateOf("") }
                                OutlinedTextField(
                                    value = variant,
                                    onValueChange = {
                                        variant = it
                                    },
                                    textStyle = TextStyle(
                                        color = BrownDefault,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Normal,

                                        ),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = BrownDefault,
                                        unfocusedBorderColor = GreyDefault,
                                        focusedContainerColor = WhiteDefault,
                                        unfocusedContainerColor = WhiteDefault,

                                    ),
                                    singleLine = true,
                                    placeholder = {
                                        Text(
                                            text = "Thêm tùy chọn",
                                            color = GreyDefault,
                                            fontWeight = FontWeight.Normal
                                        )
                                    },
                                    trailingIcon = {
                                        IconButton(
                                            colors = IconButtonColors(
                                                containerColor = WhiteDefault,
                                                contentColor = BrownDefault,
                                                disabledContentColor = GreyLight,
                                                disabledContainerColor = WhiteDefault
                                            ),
                                            enabled = variant.isNotEmpty(),
                                            onClick = {
                                                scope.launch {
                                                    if (viewModel.addVariant(
                                                        VariationRequest(
                                                            name = variant,
                                                            isMultipleChoice = false,
                                                            productId = productId
                                                        )
                                                    ) == 1) {
                                                        Toast.makeText(
                                                            context,
                                                            "Đã thêm tùy chọn ${variant}",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                        variant = ""
                                                    } else {
                                                        Toast.makeText(
                                                            context,
                                                            "Không thể thêm tùy chọn ${variant}",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                                }

                                            }) {
                                            Icon(Icons.Default.Add, contentDescription = "Add")
                                        }
                                    },
                                    keyboardOptions = KeyboardOptions.Default.copy(
                                        keyboardType = KeyboardType.Text,
                                        imeAction = ImeAction.Done),
                                    shape = RoundedCornerShape(50),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp)

                                )
                            }
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.size(50.dp))
                }
            }
        }
    }
}

