package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.MarketplaceSource

@Entity(tableName = "orders")
data class OrderEntity(
    @PrimaryKey
    val orderId: String,
    val dateFormatted: String,
    val totalAmount: Double,
    val status: String,
    val currentStepIndex: Int, // 0: Placed, 1: Supplier Confirmed, 2: Shipped, 3: Out for Delivery, 4: Delivered
    val shippingAddress: String,
    val recipientName: String,
    val paymentMethod: String,
    val deliveryMethod: String,
    val supplierTrackingNumber: String,
    val source: MarketplaceSource,
    val itemCount: Int,
    val itemsSummary: String,
    val estimatedDeliveryDate: String,
    val lastUpdatedTimestamp: Long = System.currentTimeMillis()
)
