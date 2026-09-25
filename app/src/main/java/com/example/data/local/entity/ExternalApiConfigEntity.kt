package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "api_configs")
data class ExternalApiConfigEntity(
    @PrimaryKey
    val platformKey: String, // "amazon", "aliexpress", "alibaba"
    val platformName: String,
    val apiKey: String,
    val apiSecret: String,
    val trackingOrAssociateId: String,
    val isConnected: Boolean,
    val autoSyncStock: Boolean = true,
    val profitMarginPercent: Int = 15,
    val lastSyncStatus: String = "Idle",
    val lastSyncTime: String = "Never",
    val totalSyncedProducts: Int = 0
)
