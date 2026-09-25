package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.VendorEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VendorDao {
    @Query("SELECT * FROM vendors ORDER BY totalRevenue DESC")
    fun getAllVendors(): Flow<List<VendorEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVendors(vendors: List<VendorEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVendor(vendor: VendorEntity)

    @Query("UPDATE vendors SET status = :newStatus WHERE vendorId = :id")
    suspend fun updateVendorStatus(id: String, newStatus: String)

    @Query("UPDATE vendors SET totalRevenue = totalRevenue + :amount, totalOrders = totalOrders + 1 WHERE vendorId = :id")
    suspend fun recordSale(id: String, amount: Double)
}
