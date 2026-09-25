package com.example.data.local

import androidx.room.TypeConverter
import com.example.data.model.MarketplaceSource
import com.example.data.model.ProductCategory

class Converters {
    @TypeConverter
    fun fromMarketplaceSource(source: MarketplaceSource): String = source.name

    @TypeConverter
    fun toMarketplaceSource(name: String): MarketplaceSource {
        return try {
            MarketplaceSource.valueOf(name)
        } catch (e: Exception) {
            MarketplaceSource.LOCAL
        }
    }

    @TypeConverter
    fun fromProductCategory(category: ProductCategory): String = category.name

    @TypeConverter
    fun toProductCategory(name: String): ProductCategory {
        return try {
            ProductCategory.valueOf(name)
        } catch (e: Exception) {
            ProductCategory.ALL
        }
    }
}
