package com.example.data.repository

import com.example.data.local.ZexoDatabase
import com.example.data.local.entity.CartItemEntity
import com.example.data.local.entity.ExternalApiConfigEntity
import com.example.data.local.entity.OrderEntity
import com.example.data.local.entity.ProductEntity
import com.example.data.local.entity.VendorEntity
import com.example.data.local.entity.WishlistItemEntity
import com.example.data.model.MarketplaceSource
import com.example.data.model.ProductCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

class ZexoRepository(private val database: ZexoDatabase) {

    private val productDao = database.productDao()
    private val cartDao = database.cartDao()
    private val wishlistDao = database.wishlistDao()
    private val orderDao = database.orderDao()
    private val apiConfigDao = database.externalApiConfigDao()
    private val vendorDao = database.vendorDao()

    suspend fun ensureDatabaseSeeded() = withContext(Dispatchers.IO) {
        ZexoDatabase.populateDatabase(database)
    }

    // Products
    fun getAllProducts(): Flow<List<ProductEntity>> = productDao.getAllProducts()

    fun getProductsBySource(source: MarketplaceSource): Flow<List<ProductEntity>> {
        return if (source == MarketplaceSource.ALL) {
            productDao.getAllProducts()
        } else {
            productDao.getProductsBySource(source)
        }
    }

    fun getProductsByCategory(category: ProductCategory): Flow<List<ProductEntity>> {
        return if (category == ProductCategory.ALL) {
            productDao.getAllProducts()
        } else {
            productDao.getProductsByCategory(category)
        }
    }

    fun getFlashSaleProducts(): Flow<List<ProductEntity>> = productDao.getFlashSaleProducts()

    fun searchProducts(query: String): Flow<List<ProductEntity>> = productDao.searchProducts(query)

    fun getProductById(id: String): Flow<ProductEntity?> = productDao.getProductById(id)

    suspend fun insertProduct(product: ProductEntity) = withContext(Dispatchers.IO) {
        productDao.insertProduct(product)
    }

    suspend fun updateStock(id: String, stock: Int) = withContext(Dispatchers.IO) {
        productDao.updateStock(id, stock)
    }

    suspend fun deleteProduct(id: String) = withContext(Dispatchers.IO) {
        productDao.deleteProductById(id)
    }

    // Cart
    fun getCartItems(): Flow<List<CartItemEntity>> = cartDao.getAllCartItems()

    suspend fun addToCart(
        product: ProductEntity,
        selectedColor: String,
        selectedSize: String,
        quantity: Int
    ) = withContext(Dispatchers.IO) {
        val existingItems = cartDao.getAllCartItems().firstOrNull() ?: emptyList()
        val existing = existingItems.find {
            it.productId == product.id && it.selectedColor == selectedColor && it.selectedSize == selectedSize
        }
        if (existing != null) {
            cartDao.updateQuantity(existing.id, existing.quantity + quantity)
        } else {
            cartDao.insertCartItem(
                CartItemEntity(
                    productId = product.id,
                    productTitle = product.title,
                    price = product.price,
                    imageUrl = product.imageUrl,
                    source = product.source,
                    selectedColor = selectedColor,
                    selectedSize = selectedSize,
                    quantity = quantity
                )
            )
        }
    }

    suspend fun updateCartQuantity(id: Long, quantity: Int) = withContext(Dispatchers.IO) {
        if (quantity <= 0) {
            cartDao.deleteCartItem(id)
        } else {
            cartDao.updateQuantity(id, quantity)
        }
    }

    suspend fun removeFromCart(id: Long) = withContext(Dispatchers.IO) {
        cartDao.deleteCartItem(id)
    }

    suspend fun clearCart() = withContext(Dispatchers.IO) {
        cartDao.clearCart()
    }

    // Wishlist
    fun getWishlistItems(): Flow<List<WishlistItemEntity>> = wishlistDao.getAllWishlist()

    fun isInWishlist(productId: String): Flow<Boolean> = wishlistDao.isInWishlist(productId)

    suspend fun toggleWishlist(product: ProductEntity) = withContext(Dispatchers.IO) {
        val isWishlisted = wishlistDao.isInWishlist(product.id).firstOrNull() ?: false
        if (isWishlisted) {
            wishlistDao.deleteWishlist(product.id)
        } else {
            wishlistDao.insertWishlist(
                WishlistItemEntity(
                    productId = product.id,
                    title = product.title,
                    price = product.price,
                    originalPrice = product.originalPrice,
                    imageUrl = product.imageUrl,
                    source = product.source,
                    rating = product.rating
                )
            )
        }
    }

    suspend fun removeFromWishlist(productId: String) = withContext(Dispatchers.IO) {
        wishlistDao.deleteWishlist(productId)
    }

    // Orders
    fun getAllOrders(): Flow<List<OrderEntity>> = orderDao.getAllOrders()

    fun getOrderById(orderId: String): Flow<OrderEntity?> = orderDao.getOrderById(orderId)

    suspend fun placeOrder(order: OrderEntity) = withContext(Dispatchers.IO) {
        orderDao.insertOrder(order)
        cartDao.clearCart()
    }

    suspend fun advanceOrderStatus(orderId: String): String = withContext(Dispatchers.IO) {
        val order = orderDao.getOrderById(orderId).firstOrNull() ?: return@withContext "Order not found"
        val stages = listOf(
            "Order Placed",
            "Supplier Confirmed",
            "Shipped (International Cargo)",
            "Out for Delivery",
            "Delivered"
        )
        val nextIndex = (order.currentStepIndex + 1).coerceAtMost(stages.size - 1)
        val newStatus = stages[nextIndex]
        orderDao.updateOrderStatus(orderId, nextIndex, newStatus)
        newStatus
    }

    // External API Sync & Dropshipping Integration
    fun getApiConfigs(): Flow<List<ExternalApiConfigEntity>> = apiConfigDao.getAllConfigs()

    suspend fun saveApiConfig(config: ExternalApiConfigEntity) = withContext(Dispatchers.IO) {
        apiConfigDao.insertConfig(config)
    }

    suspend fun syncExternalMarketplace(platformKey: String): Pair<Boolean, String> = withContext(Dispatchers.IO) {
        // Real simulation of calling external API with rate limits & item imports
        delay(1200) // simulated network request to Amazon/AliExpress/Alibaba
        val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())

        when (platformKey) {
            "amazon" -> {
                // Import / update dynamic Amazon product with live stock check
                val importedAmazonItem = ProductEntity(
                    id = "AMZ-SYNC-${Random.nextInt(100, 999)}",
                    title = "Amazon Echo Show 8 (3rd Gen) HD Smart Touchscreen",
                    description = "Spatial audio smart display with Alexa and 13 MP camera. Centered auto-framing for video calls and smart home hub built-in.",
                    price = 149.99,
                    originalPrice = 179.99,
                    rating = 4.7f,
                    reviewCount = 1140,
                    source = MarketplaceSource.AMAZON,
                    category = ProductCategory.GADGETS,
                    imageUrl = "https://images.unsplash.com/photo-1543512214-318c7553f230?w=800&auto=format&fit=crop&q=80",
                    additionalImages = "",
                    stock = Random.nextInt(15, 60),
                    sellerName = "Amazon Sourced PA-API",
                    isFlashSale = true,
                    flashSaleDiscount = 17,
                    externalAffiliateUrl = "https://www.amazon.com/dp/B0BLS3Y632?tag=zexo-affiliate-20",
                    shippingEstimate = "Amazon Global Expedited (3-5 days)"
                )
                productDao.insertProduct(importedAmazonItem)
                apiConfigDao.updateSyncStatus("amazon", "Synced: Live Price & Stock Updated", currentTime, 321)
                Pair(true, "Amazon PA-API v5.0 synced! Product prices and stock levels updated successfully.")
            }
            "aliexpress" -> {
                val importedAliItem = ProductEntity(
                    id = "ALI-SYNC-${Random.nextInt(100, 999)}",
                    title = "Ultra-Fast GaN 140W Multi-Port Desktop Charger",
                    description = "Compact 4-port Gallium Nitride fast charger for laptops, tablets, and phones. Multi-voltage PD 3.1 protocol with real-time temperature safety control.",
                    price = 44.50,
                    originalPrice = 79.00,
                    rating = 4.8f,
                    reviewCount = 3840,
                    source = MarketplaceSource.ALIEXPRESS,
                    category = ProductCategory.ELECTRONICS,
                    imageUrl = "https://images.unsplash.com/photo-1583863788434-e58a36330cf0?w=800&auto=format&fit=crop&q=80",
                    additionalImages = "",
                    stock = Random.nextInt(40, 180),
                    sellerName = "BaseFast Dropship Direct (AliExpress)",
                    isFlashSale = true,
                    flashSaleDiscount = 44,
                    externalAffiliateUrl = "https://www.aliexpress.com/item/1005008912.html?aff_platform=zexo",
                    shippingEstimate = "AliExpress Direct Tracked (6-9 days)"
                )
                productDao.insertProduct(importedAliItem)
                apiConfigDao.updateSyncStatus("aliexpress", "Synced: Dropship Feed Synced", currentTime, 841)
                Pair(true, "AliExpress Dropshipping Bridge connected! New trending items & tracking synced.")
            }
            "alibaba" -> {
                apiConfigDao.updateSyncStatus("alibaba", "Synced: Wholesale Tier Margins Updated", currentTime, 215)
                Pair(true, "Alibaba B2B Wholesale Gateway synced! Tiered volume pricing verified.")
            }
            else -> {
                Pair(false, "Unknown external marketplace key")
            }
        }
    }

    // Vendors
    fun getAllVendors(): Flow<List<VendorEntity>> = vendorDao.getAllVendors()

    suspend fun registerVendor(vendor: VendorEntity) = withContext(Dispatchers.IO) {
        vendorDao.insertVendor(vendor)
    }

    suspend fun updateVendorStatus(vendorId: String, newStatus: String) = withContext(Dispatchers.IO) {
        vendorDao.updateVendorStatus(vendorId, newStatus)
    }
}
