package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.local.dao.CartDao
import com.example.data.local.dao.ExternalApiConfigDao
import com.example.data.local.dao.OrderDao
import com.example.data.local.dao.ProductDao
import com.example.data.local.dao.VendorDao
import com.example.data.local.dao.WishlistDao
import com.example.data.local.entity.CartItemEntity
import com.example.data.local.entity.ExternalApiConfigEntity
import com.example.data.local.entity.OrderEntity
import com.example.data.local.entity.ProductEntity
import com.example.data.local.entity.VendorEntity
import com.example.data.local.entity.WishlistItemEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        ProductEntity::class,
        CartItemEntity::class,
        WishlistItemEntity::class,
        OrderEntity::class,
        ExternalApiConfigEntity::class,
        VendorEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ZexoDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun cartDao(): CartDao
    abstract fun wishlistDao(): WishlistDao
    abstract fun orderDao(): OrderDao
    abstract fun externalApiConfigDao(): ExternalApiConfigDao
    abstract fun vendorDao(): VendorDao

    companion object {
        @Volatile
        private var INSTANCE: ZexoDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): ZexoDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ZexoDatabase::class.java,
                    "zexo_ecommerce_database"
                )
                    .addCallback(DatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database)
                    }
                }
            }
        }

        suspend fun populateDatabase(database: ZexoDatabase) {
            val productDao = database.productDao()
            if (productDao.getProductCount() == 0) {
                productDao.insertProducts(DatabaseSeedData.sampleProducts)
                database.externalApiConfigDao().insertConfigs(DatabaseSeedData.defaultApiConfigs)
                database.vendorDao().insertVendors(DatabaseSeedData.sampleVendors)
                for (order in DatabaseSeedData.sampleOrders) {
                    database.orderDao().insertOrder(order)
                }
            }
        }
    }
}
