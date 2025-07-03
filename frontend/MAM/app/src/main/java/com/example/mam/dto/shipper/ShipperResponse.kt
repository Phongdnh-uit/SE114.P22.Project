package com.example.mam.dto.shipper

import com.example.mam.dto.BaseResponse
import java.time.Instant

data class ShipperResponse(
    val fullname: String,
    val phone: String,
    val licensePlate: String,
    val isAvailable: Boolean,
): BaseResponse()
