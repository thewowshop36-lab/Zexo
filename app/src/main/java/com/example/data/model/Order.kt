package com.example.data.model

import com.google.gson.annotations.SerializedName

data class SupplierOrderRequest(
    @SerializedName("orderNumber")
    val yourInternalOrderId: String,

    @SerializedName("shippingCountryCode")
    val countryCode: String,

    @SerializedName("shippingProvince")
    val province: String,

    @SerializedName("shippingCity")
    val city: String,

    @SerializedName("shippingAddress")
    val addressLine: String,

    @SerializedName("shippingCustomerName")
    val customerName: String,

    @SerializedName("shippingPhone")
    val phone: String,

    @SerializedName("shippingZip")
    val zipCode: String?,

    @SerializedName("fromCountryCode")
    val shipFromCountry: String = "CN",

    @SerializedName("logisticName")
    val shippingMethod: String = "CJPacket",

    @SerializedName("products")
    val items: List<SupplierOrderItem>
)

data class SupplierOrderItem(
    @SerializedName("vid")
    val variantId: String,

    @SerializedName("quantity")
    val quantity: Int
)

data class SupplierOrderResponse(
    @SerializedName("code")
    val code: Int,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: SupplierOrderResult?
)

data class SupplierOrderResult(
    @SerializedName("orderId")
    val supplierOrderId: String,

    @SerializedName("orderNum")
    val orderNumber: String
)
