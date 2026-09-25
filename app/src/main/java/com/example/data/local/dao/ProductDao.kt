package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.ProductEntity
import com.example.data.model.MarketplaceSource
import com.example.data.model.ProductCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM products ORDER BY isFeatured DESC, rating DESC")
    fun getAllProducts(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE source = :source ORDER BY rating DESC")
    fun getProductsBySource(source: MarketplaceSource): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE category = :category ORDER BY rating DESC")
    fun getProductsByCategory(category: ProductCategory): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE isFlashSale = 1 ORDER BY flashSaleDiscount DESC")
    fun getFlashSaleProducts(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE id = :id LIMIT 1")
    fun getProductById(id: String): Flow<ProductEntity?>

    @Query("SELECT * FROM products WHERE title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%'")
    fun searchProducts(query: String): Flow<List<ProductEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<ProductEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity)

    @Query("DELETE FROM products WHERE id = :id")
    suspend fun deleteProductById(id: String)

    @Query("UPDATE products SET stock = :newStock WHERE id = :id")
    suspend fun updateStock(id: String, newStock: Int)

    @Query("UPDATE products SET price = :newPrice, stock = :newStock WHERE id = :id")
    suspend fun updatePriceAndStock(id: String, newPrice: Double, newStock: Int)

    @Query("SELECT COUNT(*) FROM products")
    suspend fun getProductCount(): Int
}
