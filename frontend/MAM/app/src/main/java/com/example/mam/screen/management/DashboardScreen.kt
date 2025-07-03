package com.example.mam.screen.management

import android.graphics.Typeface
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIos
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.Moped
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Sell
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.yml.charts.axis.AxisData
import co.yml.charts.common.components.Legends
import co.yml.charts.common.extensions.formatToSinglePrecision
import co.yml.charts.common.model.PlotType
import co.yml.charts.common.model.Point
import co.yml.charts.common.utils.DataUtils
import co.yml.charts.ui.barchart.BarChart
import co.yml.charts.ui.barchart.models.BarChartData
import co.yml.charts.ui.barchart.models.BarData
import co.yml.charts.ui.barchart.models.BarStyle
import co.yml.charts.ui.barchart.models.SelectionHighlightData
import co.yml.charts.ui.piechart.charts.DonutPieChart
import co.yml.charts.ui.piechart.models.PieChartConfig
import co.yml.charts.ui.piechart.models.PieChartData
import co.yml.charts.ui.piechart.utils.proportion
import com.example.mam.component.CircleIconButton
import com.example.mam.component.outerShadow
import com.example.mam.ui.theme.BrownDark
import com.example.mam.ui.theme.BrownDefault
import com.example.mam.ui.theme.GreyDark
import com.example.mam.ui.theme.OrangeDefault
import com.example.mam.ui.theme.OrangeLight
import com.example.mam.ui.theme.OrangeLighter
import com.example.mam.ui.theme.Typography
import com.example.mam.ui.theme.WhiteDefault
import com.example.mam.viewmodel.management.DashboardViewModel
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import kotlin.random.Random

@OptIn( ExperimentalLayoutApi::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    onProfileClicked: () -> Unit = {},
    onItemClicked: (String) -> Unit = {},
    onPreProcessOrderClick: () -> Unit = {},
    onProcessingOrderClick: () -> Unit = {}
) {
    // Your UI code here
    // You can use viewModel to access the data and state
    // For example:
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val isLoading = viewModel.isLoading.collectAsState()
    val processingOrderNum = viewModel.processingOrderNum.collectAsState().value
    val notProcessingOrderNum = viewModel.notProcessingOrderNum.collectAsState().value
    var processingCount by remember { mutableStateOf(0L) }
    var preProcessCount by remember { mutableStateOf(0L) }
    LaunchedEffect(key1 = Unit) {
        viewModel.loadData()
        val countStatus = viewModel.countOrderByStatus()
        processingCount = countStatus["CONFIRMED"]?.let { countStatus["PROCESSING"]?.plus(it) } ?: 0L
        preProcessCount = countStatus["PENDING"]?:0L
    }
    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
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
                icon = Icons.Outlined.Person,
                shadow = "outer",
                onClick = onProfileClicked,
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
        }

        Column(
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
                .verticalScroll(enabled = true,
                    state = rememberScrollState())
        )
        {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Card(
                        modifier = Modifier
                            .padding(16.dp),
                        colors = CardDefaults.cardColors(containerColor = WhiteDefault),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 4.dp,
                        ),
                        onClick = onProcessingOrderClick,
                        shape = RoundedCornerShape(20.dp),
                    ) {
                        Text(
                            text = "Đơn đang xử lý",
                            color = BrownDefault,
                            fontSize = 16.sp,
                            textAlign = TextAlign.Start,
                            modifier = Modifier
                                .padding(top = 10.dp, start = 10.dp, end = 10.dp, bottom = 5.dp)
                        )
                        Text(
                            text = processingCount.toString(),
                            color = OrangeDefault,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Start,
                            modifier = Modifier
                                .padding(top = 5.dp, start = 10.dp, end = 10.dp, bottom = 10.dp)
                        )
                    }
                    Card(
                        modifier = Modifier
                            .padding(16.dp),
                        colors = CardDefaults.cardColors(containerColor = WhiteDefault),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 4.dp,
                        ),
                        onClick = onPreProcessOrderClick,
                        shape = RoundedCornerShape(20.dp),
                    ) {
                        Text(
                            text = "Đơn chưa tiếp nhận",
                            color = BrownDefault,
                            fontSize = 16.sp,
                            maxLines = 1,
                            textAlign = TextAlign.Start,
                            modifier = Modifier
                                .padding(top = 10.dp, start = 10.dp, end = 10.dp, bottom = 5.dp)
                        )
                        Text(
                            text = preProcessCount.toString(),
                            color = OrangeDefault,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Start,
                            modifier = Modifier
                                .padding(top = 5.dp, start = 10.dp, end = 10.dp, bottom = 10.dp)
                        )
                    }
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(16.dp)
                        .background(
                            color = OrangeLight,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Quản lý",
                        color = BrownDefault,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp)
                    )
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.wrapContentWidth(align = Alignment.CenterHorizontally),
                    ) {
                        MyNav.entries.forEach {
                            Button(
                                onClick = {
                                    onItemClicked(it.label)
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = OrangeDefault,
                                    contentColor = WhiteDefault
                                ),
                                shape = RoundedCornerShape(20.dp),
                                modifier = Modifier
                            ) {
                                Column(
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .width(90.dp)

                                ) {
                                    Icon(
                                        imageVector = it.icon,
                                        contentDescription = "Item ${it.label}",
                                        //tint = WhiteDefault,
                                        modifier = Modifier
                                            .size(50.dp)
                                            .padding(8.dp)
                                    )
                                    Text(
                                        text = it.label,
                                        fontSize = 12.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .padding(
                                                bottom = 5.dp,
                                                top = 0.dp,
                                            )
                                            .fillMaxWidth()
                                    )
                                }
                            }
                        }
                    }
                }

                var isShowBarChart by remember { mutableStateOf(false) }
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(16.dp)
                        .background(
                            color = OrangeLight,
                            shape = RoundedCornerShape(20.dp)
                        )
                ) {
                    Text(
                        text = "Thống kê doanh thu",
                        color = BrownDefault,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth()
                            .clickable { isShowBarChart = !isShowBarChart }
                    )
                    if (isShowBarChart)
                    MonthRevenueChart(
                        viewModel = viewModel,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .height(400.dp)
                    )
                }

                var isShowPieChart by remember { mutableStateOf(false) }
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(16.dp)
                        .background(
                            color = OrangeLight,
                            shape = RoundedCornerShape(20.dp)
                        )
                ) {
                    Text(
                        text = "Tỉ lệ danh mục bán ra",
                        color = BrownDefault,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth()
                            .clickable { isShowPieChart = !isShowPieChart }
                    )
                    if (isShowPieChart)
                    SoldCategoryChart(
                        viewModel = viewModel,
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                            .wrapContentHeight()
                    )
                }
            }
        }
    }


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MonthRevenueChart(
    viewModel: DashboardViewModel,
    modifier: Modifier = Modifier,
) {
    var year by remember { mutableIntStateOf(Instant.now().atZone(ZoneId.systemDefault()).year) }
    var isMonthly by remember { mutableStateOf(true) }
    val pointsData = viewModel.revenueList.collectAsStateWithLifecycle().value
    val isLoading = viewModel.isLoadingRevenue.collectAsStateWithLifecycle().value
    val scope = rememberCoroutineScope()
    LaunchedEffect(key1 = year, key2 = isMonthly) {
            viewModel.loadRevenueMonthlyList(year, isMonthly)

    }
    if (isLoading) {
        // Show loading indicator
        CircularProgressIndicator(
            color = OrangeDefault,
            modifier = Modifier
                .padding(16.dp)
                .size(40.dp)
        )
    } else if (pointsData.isEmpty()) {
        // Show empty state
        Text(
            text = "Không có dữ liệu",
            color = BrownDefault,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        )
    } else{
        Column(
            modifier = modifier
                .background(
                    color = WhiteDefault,
                    shape = RoundedCornerShape(20.dp)
                )
                .clip(RoundedCornerShape(20.dp))
        ) {
            Box(
                contentAlignment = Alignment.CenterStart,
                modifier = Modifier
                    .background(WhiteDefault)
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clip(RoundedCornerShape(20.dp))
            ) {
                IconButton(
                    onClick = {
                        year--
                          scope.launch{
                              viewModel.loadRevenueMonthlyList(year, isMonthly)
                          }},
                    modifier = Modifier
                        .padding(top = 16.dp, start = 16.dp)
                        .size(40.dp)
                ) {
                    Icon(
                        Icons.Default.ArrowBackIos,
                        contentDescription = "Delete",
                        tint = BrownDefault
                    )
                }
                Text(
                    text = "Doanh thu năm ${year}\n" + if (isMonthly) {
                        "theo tháng"
                    } else {
                        "theo quý"
                    } + " (Đơn vị: triệu đồng)",
                    color = BrownDefault,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                )
                IconButton(
                    onClick = { year++
                        scope.launch{
                            viewModel.loadRevenueMonthlyList(year, isMonthly)
                        }},
                    modifier = Modifier
                        .padding(top = 16.dp, end = 16.dp)
                        .size(40.dp)
                        .align(Alignment.TopEnd)
                ) {
                    Icon(
                        Icons.Default.ArrowForwardIos,
                        contentDescription = "",
                        tint = BrownDefault
                    )
                }
            }

            val steps = 5
            val xAxisData = AxisData.Builder()
//        .axisStepSize(75.dp)
                .topPadding(105.dp)
                .steps(pointsData.size)
                .labelData { i -> (i + 1).toString() }
                .labelAndAxisLinePadding(15.dp)
                .axisLineColor(BrownDark)
                .axisLabelColor(BrownDefault)
                .startDrawPadding(45.dp)
                .build()
            val yAxisData = AxisData.Builder()
                .steps(5)
//        .axisStepSize(50.dp)
                .startDrawPadding(50.dp)
                .axisLineColor(BrownDark)
                .axisLabelColor(BrownDefault)
                .backgroundColor(WhiteDefault)
                .labelAndAxisLinePadding(20.dp)
                .labelData { i ->
                    // Add yMin to get the negative axis values to the scale
                    val yMin = 0
                    val yMax =
                        if (isMonthly) pointsData.take(12).maxOf { it.y } else pointsData.take(4)
                            .maxOf { it.y }
                    val yScale = (yMax - yMin) / steps
                    ((i * yScale) + yMin).formatToSinglePrecision()
                }
                .build()
            val data = BarChartData(
                chartData = ParsePointsListToBarData(pointsData).take(if (isMonthly) 12 else 4),
                backgroundColor = WhiteDefault,
                xAxisData = xAxisData,
                yAxisData = yAxisData,
                horizontalExtraSpace = 20.dp,
                paddingTop = 75.dp,
                paddingEnd = 0.dp,
                barStyle = BarStyle(
                    paddingBetweenBars = 30.dp,
                    selectionHighlightData = SelectionHighlightData(
                        highlightTextColor = BrownDefault,
                        highlightTextBackgroundColor = WhiteDefault,
                        highlightTextBackgroundAlpha = 1f,
                        highlightBarColor = BrownDefault,
                        highlightBarStrokeWidth = 2.dp,
                        highlightTextOffset = 10.dp,
                        isHighlightFullBar = true,
                        popUpLabel = { x, y ->
                            y.formatToSinglePrecision()
                        },
                    )
                ),

                )
            BarChart(
                barChartData = data,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(WhiteDefault)
            )
        }
    Row(
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .zIndex(1f)
    ) {
        Text(
            text = "Theo quý: ",
            color = BrownDefault,
            fontSize = 16.sp,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .padding(8.dp)
        )
        Switch(
            checked = !isMonthly,
            onCheckedChange = { isMonthly = !isMonthly },
            colors = SwitchDefaults.colors(
                checkedTrackColor = OrangeDefault,
                uncheckedTrackColor = WhiteDefault,
                checkedThumbColor = WhiteDefault,
                uncheckedThumbColor = OrangeDefault,
                uncheckedBorderColor = OrangeDefault,
            ),
            thumbContent = {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = if (!isMonthly) OrangeDefault else WhiteDefault,
                    modifier = Modifier
                        .size(20.dp)
                        .padding(2.dp)
                )
            },
            modifier = Modifier
                .padding(8.dp)
        )
    }
}
}

@Composable
fun SoldCategoryChart(
    viewModel: DashboardViewModel,
    modifier: Modifier = Modifier,
) {
    var year by remember { mutableStateOf<Int>(Instant.now().atZone(ZoneId.systemDefault()).year) }
    var month by remember { mutableStateOf<Int>(Instant.now().atZone(ZoneId.systemDefault()).monthValue) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val chartData = viewModel.categorySoldList.collectAsStateWithLifecycle().value

    LaunchedEffect(key1 = month) {
            viewModel.loadCategorySoldMonthlyList(month, year)

    }
    val isLoading = viewModel.isLoadingCategory.collectAsStateWithLifecycle().value
    val pieChartData = chartData.mapIndexed { index, pair ->
        val red = Random.nextInt(256)
        val green = Random.nextInt(256)
        val blue = Random.nextInt(256)
        PieChartData.Slice(
            label = pair.first,
            value = pair.second,
            color = Color(red, green, blue)
        )
    }
    val data = PieChartData(
        slices = pieChartData,
        plotType = PlotType.Donut,
    )
    // Sum of all the values
    val sumOfValues = chartData.sumOf { it.second.toDouble() }

    // Calculate each proportion value
    val proportions = data.slices.proportion(sumOfValues.toFloat())
    val pieChartConfig =
        PieChartConfig(
            labelVisible = true,
            strokeWidth = 120f,
            labelColor = BrownDefault,
            activeSliceAlpha = .9f,
            isEllipsizeEnabled = true,
            labelTypeface = Typeface.defaultFromStyle(Typeface.BOLD),
            isAnimationEnable = true,
            chartPadding = 25,
            labelFontSize = 42.sp,
        )
    if (isLoading) {
        // Show loading indicator
        CircularProgressIndicator(
            color = OrangeDefault,
            modifier = Modifier
                .padding(16.dp)
                .size(40.dp)
        )
    } else {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .background(
                    color = WhiteDefault,
                    shape = RoundedCornerShape(20.dp)
                )
                .wrapContentHeight()
                .clip(RoundedCornerShape(20.dp))

        ) {
            Box(
                contentAlignment = Alignment.CenterStart,
                modifier = Modifier
                    .background(WhiteDefault)
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clip(RoundedCornerShape(20.dp))
            ) {
                IconButton(
                    onClick = {
                        scope.launch {
                            month--
                            if (month <= 0) {
                                year--
                                month = 12
                            }
                            viewModel.loadCategorySoldMonthlyList(month, year)
                        }
                    },
                    modifier = Modifier
                        .padding(top = 16.dp, start = 16.dp)
                        .size(40.dp)
                ) {
                    Icon(
                        Icons.Default.ArrowBackIos,
                        contentDescription = "Delete",
                        tint = BrownDefault
                    )
                }
                Text(
                    text = "Tỉ lệ danh mục bán ra\n theo tháng ($month/$year)",
                    color = BrownDefault,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                )
                IconButton(
                    onClick = {
                        scope.launch {
                            month++
                            if (month > 12) {
                                year++
                                month = 1
                            }
                            viewModel.loadCategorySoldMonthlyList(month, year)
                        }
                    },
                    modifier = Modifier
                        .padding(top = 16.dp, end = 16.dp)
                        .size(40.dp)
                        .align(Alignment.TopEnd)
                ) {
                    Icon(
                        Icons.Default.ArrowForwardIos,
                        contentDescription = "",
                        tint = BrownDefault
                    )
                }
            }
            if (chartData.all { it.second == 0f }) {
                // Show empty state
                Text(
                    text = "Không có dữ liệu",
                    color = BrownDefault,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                )
            } else {
                Legends(
                    legendsConfig = DataUtils.getLegendsConfigFromPieChartData(
                        pieChartData = data,
                        3
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .height(150.dp),
                )
                DonutPieChart(
                    modifier = Modifier
                        .size(300.dp),
                    data,
                    pieChartConfig
                ) { slice ->
                    Toast.makeText(context, slice.label, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}


fun ParsePointsListToBarData(
    pointsData: List<Point>,
): List<BarData> {
    return pointsData.mapIndexed { index, point ->
        BarData(
            point = point,
            label = "${point.y} triệu đồng",
            color = OrangeDefault
        )
    }
}

enum class MyNav(val label: String, val icon: ImageVector) {
    Catergory("Danh mục", Icons.Default.Inventory),
    Product("Sản phẩm", Icons.Default.Fastfood),
    User("Người dùng", Icons.Default.Person),
    Shipper("Shipper", Icons.Default.Moped),
    Notification("Thông báo", Icons.Default.NotificationsActive),
    Promotion("Khuyến mãi", Icons.Default.Sell),
    Order("Đơn hàng", Icons.Default.ShoppingBag)
}



