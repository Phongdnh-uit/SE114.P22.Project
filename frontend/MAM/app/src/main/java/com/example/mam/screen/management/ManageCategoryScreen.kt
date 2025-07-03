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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.core.app.ActivityCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.mam.R
import com.example.mam.component.CircleIconButton
import com.example.mam.component.outerShadow
import com.example.mam.ui.theme.BrownDefault
import com.example.mam.ui.theme.ErrorColor
import com.example.mam.ui.theme.GreyDark
import com.example.mam.ui.theme.GreyDefault
import com.example.mam.ui.theme.OrangeDefault
import com.example.mam.ui.theme.OrangeLighter
import com.example.mam.ui.theme.Typography
import com.example.mam.viewmodel.management.ManageCategoryViewModel
import kotlinx.coroutines.launch
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun ManageCategoryScreen(
    viewModel: ManageCategoryViewModel,
    onBackClick: () -> Unit,
    isPreview : Boolean = false,
    isEdit: Boolean = false,
    isAdd: Boolean = false,
) {
    val categoryId = viewModel.categoryID.collectAsStateWithLifecycle().value
    val categoryName = viewModel.categoryName.collectAsStateWithLifecycle().value
    val categoryDescription = viewModel.categoryDescription.collectAsStateWithLifecycle().value
    val categoryImage = viewModel.categoryImage.collectAsStateWithLifecycle().value
    val createdAt = viewModel.createdAt.collectAsStateWithLifecycle().value
    val updatedAt = viewModel.updatedAt.collectAsStateWithLifecycle().value
    val isLoading = viewModel.isLoading.collectAsStateWithLifecycle().value
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val activity = context as? Activity
    val imagePicker = if (!isPreview) {
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                viewModel.setCategoryImage(it.toString())
                viewModel.setCategoryImageFile(context,uri)
            }
        }
    } else null
    LaunchedEffect(Unit) {
        if (isEdit) {
            viewModel.loadData()
        }
        if (isPreview) {
            viewModel.mockData()
        }

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
            val isButtonEnable = if (isAdd || isEdit) {
                viewModel.isCategoryNameValid().isEmpty() &&
                        viewModel.isCategoryDescriptionValid().isEmpty()
                        && categoryName.isNotEmpty() && categoryDescription.isNotEmpty() && categoryImage.isNotEmpty()
            } else true
            if (isButtonEnable) CircleIconButton(
                backgroundColor = OrangeLighter,
                foregroundColor = OrangeDefault,
                icon = if (isEdit || isAdd) Icons.Default.Done else Icons.Default.Edit,
                shadow = "outer",
                onClick = {
                    scope.launch {
                        if (isEdit) {
                            val result = viewModel.updateCategory()
                            Toast.makeText(
                                context,
                                when(result){
                                    1 -> "Chỉnh sửa thành công"
                                    else -> "Chỉnh sửa thất bại"
                                },
                                Toast.LENGTH_SHORT
                            ).show()
                            onBackClick()
                        } else if (isAdd) {
                            val result = viewModel.createCategory()
                            Toast.makeText(
                                context,
                                when(result){
                                    1 -> "Thêm thành công"
                                    else -> "Thêm thất bại"
                                },
                                Toast.LENGTH_SHORT
                            ).show()
                            onBackClick()
                        }
                    }
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(end = 16.dp, top = 16.dp)
            )
            Text(
                text = if (isAdd) "Thêm danh mục" else if (isEdit) "Chỉnh sửa danh mục" else "",
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
                        model = categoryImage,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(R.drawable.upload_image),
                        error = null,
                        modifier = Modifier
                            .widthIn(max = 150.dp)
                            .fillMaxWidth(0.8f)
                            // Set width to 80% of screen width
                            .aspectRatio(1f)
                            .padding(8.dp)
                            .clip(RoundedCornerShape(50))
                            .then(
                                if (isEdit || isAdd) {
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
                            append(categoryId.toString())
                        },
                        textAlign = TextAlign.Start,
                        color = GreyDefault,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth()
                    )
                    createdAt.atZone(ZoneId.systemDefault()).let {
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
                    updatedAt.atZone(ZoneId.systemDefault()).let {
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
                        OutlinedTextField(
                            value = categoryName,
                            onValueChange = {
                                viewModel.setCategoryName(it)
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
                                    text = "Tên danh mục",
                                    color = BrownDefault,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier
                                )
                            },
                            supportingText = {
                                if (viewModel.isCategoryNameValid().isNotEmpty() && categoryName.isNotEmpty()) {
                                    Text(
                                        text = viewModel.isCategoryNameValid(),
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
                            value = categoryDescription,
                            onValueChange = {
                                viewModel.setCategoryDescription(it)
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
                                    text = "Mô tả danh mục",
                                    color = BrownDefault,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier
                                )
                            },
                            supportingText = {
                                if (viewModel.isCategoryDescriptionValid().isNotEmpty() && categoryDescription.isNotEmpty()) {
                                    Text(
                                        text = viewModel.isCategoryDescriptionValid(),
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
                }
            }
        }
    }

