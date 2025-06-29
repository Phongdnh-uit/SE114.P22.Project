package com.example.mam.gui.screen.client

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mam.gui.component.OuterShadowFilledButton

@Composable
fun PaymentResultScreen(
    onBackHome: () -> Unit,
    uri: Uri?,
) {
    val responseCode = uri?.getQueryParameter("vnp_ResponseCode") ?: ""
    val transactionStatus = uri?.getQueryParameter("vnp_TransactionStatus") ?: ""

    val isSuccess = responseCode == "00" && transactionStatus == "00"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            //if (isSuccess) "Thanh toán thành công!" else "Thanh toán thất bại! $responseCode, $transactionStatus",
            uri.toString(),
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        OuterShadowFilledButton(
            text = "Quay lại trang chủ",
            modifier = Modifier.padding(horizontal = 16.dp),
            onClick = onBackHome
        )
    }
}