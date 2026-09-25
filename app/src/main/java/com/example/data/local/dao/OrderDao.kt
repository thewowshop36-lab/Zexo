package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.OrderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {
    @Query("SELECT * FROM orders ORDER BY lastUpdatedTimestamp DESC")
    fun getAllOrders(): Flow<List<OrderEntity>>

    @Query("SELECT * FROM orders WHERE orderId = :orderId LIMIT 1")
    fun getOrderById(orderId: String): Flow<OrderEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrderEntity)

    @Query("UPDATE orders SET currentStepIndex = :newStepIndex, status = :newStatus, lastUpdatedTimestamp = :timestamp WHERE orderId = :orderId")
    suspend fun updateOrderStatus(orderId: String, newStepIndex: Int, newStatus: String, timestamp: Long = System.currentTimeMillis())

    @Query("SELECT COUNT(*) FROM orders")
    suspend fun getOrderCount(): Int
}
