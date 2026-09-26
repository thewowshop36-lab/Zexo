package com.example.data.model

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("pid")
    val supplierProductId: String,

    @SerializedName("productName")
    val name: String,

    @SerializedName("productNameEn")
    val nameEnglish: String?,

    @SerializedName("productImage")
    val imageUrl: String,

    @SerializedName("sellPrice")
    val price: Double,

    @SerializedName("productSku")
    val sku: String,

    @SerializedName("categoryName")
    val category: String?,

    @SerializedName("variants")
    val variants: List<ProductVariant> = emptyList(),

    var localId: Long = 0,
    var supplierName: String = "cjdropshipping",
    var isActive: Boolean = true,
    var lastSyncedAt: Long = System.currentTimeMillis()
)

data class ProductVariant(
    @SerializedName("vid")
    val variantId: String,

    @SerializedName("variantName")
    val variantName: String,

    @SerializedName("variantSku")
    val variantSku: String,

    @SerializedName("variantSellPrice")
    val price: Double,

    @SerializedName("variantStandard")
    val attributes: String?,

    @SerializedName("variantImage")
    val imageUrl: String?
)

data class ProductListResponse(
    @SerializedName("code")
    val code: Int,

    @SerializedName("message")
    val message: String,

    @SerializedName("data")
    val data: ProductListData?
)

data class ProductListData(
    @SerializedName("list")
    val list: List<Product>,

    @SerializedName("total")
    val total: Int,

    @SerializedName("pageNum")
    val pageNum: Int
)
