package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.MarketplaceSource

@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val productId: String,
    val productTitle: String,
    val price: Double,
    val imageUrl: String,
    val source: MarketplaceSource,
    val selectedColor: String,
    val selectedSize: String,
    val quantity: Int
)
