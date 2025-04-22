package com.example.mam.gui.screen.client

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mam.R
import com.example.mam.ui.theme.OrangeDefault

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier
){
    val scrollState = rememberScrollState()
    Box(

    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxSize()
                .background(color = OrangeDefault)
                .padding(WindowInsets.statusBars.asPaddingValues())
                //.padding(WindowInsets.ime.asPaddingValues())
                .verticalScroll(scrollState),
        ) {
            Box(modifier = Modifier) {

            }
            LazyColumn(

            ) {
                item {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.background(OrangeDefault)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_mam_foreground),
                            contentDescription = "MAM"
                        )
                    }
                }
                item {

                }
                item {
                    LazyRow(

                    ) {

                    }
                }
                item {
                    LazyColumn(

                    ) {

                    }
                }
            }
        }
        Row(

        ){

        }
    }
}

@Preview
@Composable
fun DashBoardPreview(){
    HomeScreen()
}