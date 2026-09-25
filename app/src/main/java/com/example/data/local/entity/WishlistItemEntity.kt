package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.MarketplaceSource

@Entity(tableName = "wishlist_items")
data class WishlistItemEntity(
    @PrimaryKey
    val productId: String,
    val title: String,
    val price: Double,
    val originalPrice: Double,
    val imageUrl: String,
    val source: MarketplaceSource,
    val rating: Float,
    val addedTimestamp: Long = System.currentTimeMillis()
)
