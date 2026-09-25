package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vendors")
data class VendorEntity(
    @PrimaryKey
    val vendorId: String,
    val storeName: String,
    val ownerName: String,
    val email: String,
    val category: String,
    val status: String, // "Active", "Pending Approval", "Suspended"
    val totalRevenue: Double,
    val totalOrders: Int,
    val rating: Float,
    val joinedDate: String
)
