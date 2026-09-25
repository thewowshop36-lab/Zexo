package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.MarketplaceSource
import com.example.data.model.ProductCategory

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String,
    val price: Double,
    val originalPrice: Double,
    val rating: Float,
    val reviewCount: Int,
    val source: MarketplaceSource,
    val category: ProductCategory,
    val imageUrl: String,
    val additionalImages: String, // comma separated URLs
    val stock: Int,
    val sellerName: String,
    val isFlashSale: Boolean = false,
    val flashSaleDiscount: Int = 0,
    val colorsJson: String = "Midnight Black,Space Gray,Titanium Silver",
    val sizesJson: String = "Standard,Pro,Max",
    val externalAffiliateUrl: String = "",
    val shippingEstimate: String = "Free Global Delivery (7-12 days)",
    val moq: Int = 1, // Minimum Order Quantity for Alibaba wholesale
    val isFeatured: Boolean = false
) {
    val discountPercent: Int
        get() = if (originalPrice > price) {
            (((originalPrice - price) / originalPrice) * 100).toInt()
        } else {
            0
        }

    val colorsList: List<String>
        get() = colorsJson.split(",").map { it.trim() }.filter { it.isNotEmpty() }

    val sizesList: List<String>
        get() = sizesJson.split(",").map { it.trim() }.filter { it.isNotEmpty() }

    val imagesList: List<String>
        get() = (listOf(imageUrl) + additionalImages.split(",").map { it.trim() }.filter { it.isNotEmpty() }).distinct()
}
