package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.WishlistItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WishlistDao {
    @Query("SELECT * FROM wishlist_items ORDER BY addedTimestamp DESC")
    fun getAllWishlist(): Flow<List<WishlistItemEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM wishlist_items WHERE productId = :productId)")
    fun isInWishlist(productId: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWishlist(item: WishlistItemEntity)

    @Query("DELETE FROM wishlist_items WHERE productId = :productId")
    suspend fun deleteWishlist(productId: String)

    @Query("DELETE FROM wishlist_items")
    suspend fun clearWishlist()
}
