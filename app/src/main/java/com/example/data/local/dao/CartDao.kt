package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.CartItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Query("SELECT * FROM cart_items ORDER BY id DESC")
    fun getAllCartItems(): Flow<List<CartItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(item: CartItemEntity): Long

    @Query("UPDATE cart_items SET quantity = :newQty WHERE id = :id")
    suspend fun updateQuantity(id: Long, newQty: Int)

    @Query("DELETE FROM cart_items WHERE id = :id")
    suspend fun deleteCartItem(id: Long)

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()
}
