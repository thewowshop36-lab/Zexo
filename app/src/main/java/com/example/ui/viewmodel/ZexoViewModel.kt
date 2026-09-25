package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.ZexoDatabase
import com.example.data.local.entity.CartItemEntity
import com.example.data.local.entity.ExternalApiConfigEntity
import com.example.data.local.entity.OrderEntity
import com.example.data.local.entity.ProductEntity
import com.example.data.local.entity.VendorEntity
import com.example.data.local.entity.WishlistItemEntity
import com.example.data.model.Currency
import com.example.data.model.MarketplaceSource
import com.example.data.model.ProductCategory
import com.example.data.model.UserRole
import com.example.data.repository.ZexoRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

sealed class Screen(val title: String) {
    object Home : Screen("Zexo Global")
    object GlobalHub : Screen("Global Dropship & Sourcing")
    object Wishlist : Screen("My Wishlist")
    object Orders : Screen("Order Tracking")
    object Profile : Screen("Account & Settings")
    data class ProductDetail(val productId: String) : Screen("Product Details")
    object Cart : Screen("Shopping Cart")
    object Checkout : Screen("Secure Checkout")
    data class OrderTrackingDetail(val orderId: String) : Screen("Live Tracking")
    object VendorDashboard : Screen("Vendor Portal")
    object AdminPanel : Screen("Superuser Control Panel")
}

data class CheckoutFormData(
    val fullName: String = "Alex Mercer",
    val streetAddress: String = "742 Evergreen Terrace, Apt 4B",
    val city: String = "New York",
    val zipCode: String = "10001",
    val country: String = "United States",
    val phone: String = "+1 (555) 234-5678",
    val deliveryMethod: String = "AliExpress Tracked Air (7-10 Days)",
    val deliveryCost: Double = 0.0,
    val paymentMethod: String = "Credit / Debit Card",
    val cardNumber: String = "4532 •••• •••• 8912",
    val cardExpiry: String = "08/29",
    val cardCvv: String = "891"
)

class ZexoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ZexoRepository

    init {
        val database = ZexoDatabase.getDatabase(application, viewModelScope)
        repository = ZexoRepository(database)
        viewModelScope.launch {
            repository.ensureDatabaseSeeded()
        }
        startFlashSaleTimer()
    }

    // Role state
    private val _currentUserRole = MutableStateFlow(UserRole.CUSTOMER)
    val currentUserRole: StateFlow<UserRole> = _currentUserRole.asStateFlow()

    // Navigation & Screen state
    private val _currentScreen = MutableStateFlow<Screen>(Screen.Home)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val _screenHistory = mutableListOf<Screen>(Screen.Home)

    // Currency
    private val _currentCurrency = MutableStateFlow(Currency.USD)
    val currentCurrency: StateFlow<Currency> = _currentCurrency.asStateFlow()

    // Filters & Search
    private val _selectedSource = MutableStateFlow(MarketplaceSource.ALL)
    val selectedSource: StateFlow<MarketplaceSource> = _selectedSource.asStateFlow()

    private val _selectedCategory = MutableStateFlow(ProductCategory.ALL)
    val selectedCategory: StateFlow<ProductCategory> = _selectedCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedSort = MutableStateFlow("Featured")
    val selectedSort: StateFlow<String> = _selectedSort.asStateFlow()

    // Quick View product
    private val _quickViewProduct = MutableStateFlow<ProductEntity?>(null)
    val quickViewProduct: StateFlow<ProductEntity?> = _quickViewProduct.asStateFlow()

    // Flash sale countdown in seconds (e.g. 14 hours 22 minutes remaining)
    private val _flashSaleSeconds = MutableStateFlow(51732L)
    val flashSaleSeconds: StateFlow<Long> = _flashSaleSeconds.asStateFlow()

    // Snackbars / Toast notifications
    private val _toastMessage = MutableSharedFlow<String>()
    val toastMessage: SharedFlow<String> = _toastMessage.asSharedFlow()

    // Checkout & Promo
    private val _promoCode = MutableStateFlow("")
    val promoCode: StateFlow<String> = _promoCode.asStateFlow()

    private val _appliedDiscountPercent = MutableStateFlow(0)
    val appliedDiscountPercent: StateFlow<Int> = _appliedDiscountPercent.asStateFlow()

    private val _checkoutForm = MutableStateFlow(CheckoutFormData())
    val checkoutForm: StateFlow<CheckoutFormData> = _checkoutForm.asStateFlow()

    private val _isSubmittingOrder = MutableStateFlow(false)
    val isSubmittingOrder: StateFlow<Boolean> = _isSubmittingOrder.asStateFlow()

    private val _lastPlacedOrderId = MutableStateFlow<String?>(null)
    val lastPlacedOrderId: StateFlow<String?> = _lastPlacedOrderId.asStateFlow()

    // Admin & API Sync state
    private val _isSyncingApi = MutableStateFlow<String?>(null)
    val isSyncingApi: StateFlow<String?> = _isSyncingApi.asStateFlow()

    // Reactive streams from Repository
    val allProducts: StateFlow<List<ProductEntity>> = repository.getAllProducts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val flashSaleProducts: StateFlow<List<ProductEntity>> = repository.getFlashSaleProducts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val cartItems: StateFlow<List<CartItemEntity>> = repository.getCartItems()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val wishlistItems: StateFlow<List<WishlistItemEntity>> = repository.getWishlistItems()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val orders: StateFlow<List<OrderEntity>> = repository.getAllOrders()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val apiConfigs: StateFlow<List<ExternalApiConfigEntity>> = repository.getApiConfigs()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val vendors: StateFlow<List<VendorEntity>> = repository.getAllVendors()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Combined filtered products
    val displayedProducts: StateFlow<List<ProductEntity>> = combine(
        allProducts,
        _selectedSource,
        _selectedCategory,
        _searchQuery,
        _selectedSort
    ) { products, source, category, query, sort ->
        products.filter { product ->
            val matchesSource = (source == MarketplaceSource.ALL) || (product.source == source)
            val matchesCategory = (category == ProductCategory.ALL) || (product.category == category)
            val matchesQuery = query.isEmpty() ||
                    product.title.contains(query, ignoreCase = true) ||
                    product.description.contains(query, ignoreCase = true) ||
                    product.sellerName.contains(query, ignoreCase = true)
            matchesSource && matchesCategory && matchesQuery
        }.let { list ->
            when (sort) {
                "Price: Low to High" -> list.sortedBy { it.price }
                "Price: High to Low" -> list.sortedByDescending { it.price }
                "Top Rated" -> list.sortedByDescending { it.rating }
                else -> list.sortedWith(compareByDescending<ProductEntity> { it.isFeatured }.thenByDescending { it.rating })
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Cart calculations
    val cartSubtotal: StateFlow<Double> = combine(cartItems) { itemsArray ->
        itemsArray[0].sumOf { it.price * it.quantity }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val cartTotalItemsCount: StateFlow<Int> = combine(cartItems) { itemsArray ->
        itemsArray[0].sumOf { it.quantity }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Navigation methods
    fun navigateTo(screen: Screen) {
        if (_currentScreen.value != screen) {
            _screenHistory.add(screen)
            _currentScreen.value = screen
        }
    }

    fun navigateBack(): Boolean {
        return if (_screenHistory.size > 1) {
            _screenHistory.removeAt(_screenHistory.size - 1)
            val previous = _screenHistory.last()
            _currentScreen.value = previous
            true
        } else if (_currentScreen.value != Screen.Home) {
            _currentScreen.value = Screen.Home
            _screenHistory.clear()
            _screenHistory.add(Screen.Home)
            true
        } else {
            false
        }
    }

    // Role switching
    fun setUserRole(role: UserRole) {
        _currentUserRole.value = role
        when (role) {
            UserRole.CUSTOMER -> navigateTo(Screen.Home)
            UserRole.VENDOR -> navigateTo(Screen.VendorDashboard)
            UserRole.ADMIN -> navigateTo(Screen.AdminPanel)
        }
        showToast("Switched to ${role.title} role")
    }

    // Currency toggle
    fun setCurrency(currency: Currency) {
        _currentCurrency.value = currency
        showToast("Currency set to ${currency.code} (${currency.symbol})")
    }

    fun formatPrice(usdPrice: Double): String {
        val curr = _currentCurrency.value
        val converted = usdPrice * curr.rateFromUsd
        return "${curr.symbol}${String.format(Locale.US, "%.2f", converted)}"
    }

    // Filter methods
    fun setMarketplaceSource(source: MarketplaceSource) {
        _selectedSource.value = source
    }

    fun setCategory(category: ProductCategory) {
        _selectedCategory.value = category
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSort(sort: String) {
        _selectedSort.value = sort
    }

    fun openQuickView(product: ProductEntity) {
        _quickViewProduct.value = product
    }

    fun closeQuickView() {
        _quickViewProduct.value = null
    }

    // Cart actions
    fun addToCart(
        product: ProductEntity,
        color: String = product.colorsList.firstOrNull() ?: "Default",
        size: String = product.sizesList.firstOrNull() ?: "Standard",
        quantity: Int = 1
    ) {
        viewModelScope.launch {
            repository.addToCart(product, color, size, quantity)
            showToast("Added ${product.title.take(20)}... to Cart!")
        }
    }

    fun updateCartQuantity(id: Long, qty: Int) {
        viewModelScope.launch {
            repository.updateCartQuantity(id, qty)
        }
    }

    fun removeFromCart(id: Long) {
        viewModelScope.launch {
            repository.removeFromCart(id)
            showToast("Item removed from cart")
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            repository.clearCart()
        }
    }

    // Wishlist actions
    fun toggleWishlist(product: ProductEntity) {
        viewModelScope.launch {
            repository.toggleWishlist(product)
            showToast("Wishlist updated")
        }
    }

    fun removeFromWishlist(productId: String) {
        viewModelScope.launch {
            repository.removeFromWishlist(productId)
            showToast("Item removed from wishlist")
        }
    }

    // Promo code
    fun applyPromoCode(code: String) {
        _promoCode.value = code.trim().uppercase()
        when (_promoCode.value) {
            "ZEXO10" -> {
                _appliedDiscountPercent.value = 10
                showToast("Promo 'ZEXO10' applied! 10% discount")
            }
            "VIP20" -> {
                _appliedDiscountPercent.value = 20
                showToast("VIP Promo applied! 20% discount")
            }
            "GLOBAL50" -> {
                _appliedDiscountPercent.value = 25
                showToast("Global Megasale code applied! 25% discount")
            }
            else -> {
                _appliedDiscountPercent.value = 0
                showToast("Invalid promo code. Try 'ZEXO10' or 'GLOBAL50'")
            }
        }
    }

    // Checkout & Order Placement
    fun updateCheckoutForm(update: CheckoutFormData) {
        _checkoutForm.value = update
    }

    fun placeOrder() {
        val items = cartItems.value
        if (items.isEmpty()) {
            showToast("Cart is empty")
            return
        }

        viewModelScope.launch {
            _isSubmittingOrder.value = true
            delay(1200) // Simulated secure checkout gateway communication
            val subtotal = cartSubtotal.value
            val discount = (subtotal * _appliedDiscountPercent.value) / 100.0
            val shipping = _checkoutForm.value.deliveryCost
            val finalAmount = (subtotal - discount + shipping).coerceAtLeast(0.0)

            val orderCode = "ZXO-2026-${Random.nextInt(1000, 9999)}"
            val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            val dateStr = dateFormat.format(Date())

            val primarySource = items.firstOrNull()?.source ?: MarketplaceSource.LOCAL
            val trackingNumber = when (primarySource) {
                MarketplaceSource.AMAZON -> "TBA-${Random.nextLong(1000000000, 9999999999)}"
                MarketplaceSource.ALIEXPRESS -> "AE-US-${Random.nextLong(100000000, 999999999)}"
                MarketplaceSource.ALIBABA -> "BABA-CARGO-${Random.nextInt(10000, 99999)}"
                MarketplaceSource.LOCAL -> "ZXO-EXP-${Random.nextInt(10000, 99999)}"
                MarketplaceSource.ALL -> "GLB-${Random.nextInt(10000, 99999)}"
            }

            val newOrder = OrderEntity(
                orderId = orderCode,
                dateFormatted = dateStr,
                totalAmount = finalAmount,
                status = "Order Placed",
                currentStepIndex = 0,
                shippingAddress = "${_checkoutForm.value.streetAddress}, ${_checkoutForm.value.city}, ${_checkoutForm.value.country}",
                recipientName = _checkoutForm.value.fullName,
                paymentMethod = _checkoutForm.value.paymentMethod,
                deliveryMethod = _checkoutForm.value.deliveryMethod,
                supplierTrackingNumber = trackingNumber,
                source = primarySource,
                itemCount = items.sumOf { it.quantity },
                itemsSummary = items.joinToString(", ") { "${it.quantity}x ${it.productTitle.take(24)}" },
                estimatedDeliveryDate = "Estimated delivery in 5-9 business days"
            )

            repository.placeOrder(newOrder)
            _lastPlacedOrderId.value = orderCode
            _isSubmittingOrder.value = false
            showToast("Order $orderCode placed successfully!")
            navigateTo(Screen.OrderTrackingDetail(orderCode))
        }
    }

    fun advanceOrderStatus(orderId: String) {
        viewModelScope.launch {
            val newStatus = repository.advanceOrderStatus(orderId)
            showToast("Order status updated to: $newStatus")
        }
    }

    // External API Sync (Admin Superuser)
    fun syncMarketplaceApi(platformKey: String) {
        viewModelScope.launch {
            _isSyncingApi.value = platformKey
            val (success, message) = repository.syncExternalMarketplace(platformKey)
            _isSyncingApi.value = null
            showToast(message)
        }
    }

    fun updateApiConfig(config: ExternalApiConfigEntity) {
        viewModelScope.launch {
            repository.saveApiConfig(config)
            showToast("Configuration for ${config.platformName} updated")
        }
    }

    // Vendor actions
    fun addLocalProduct(
        title: String,
        description: String,
        price: Double,
        category: ProductCategory,
        stock: Int,
        imageUrl: String
    ) {
        viewModelScope.launch {
            val newProduct = ProductEntity(
                id = "LOC-${Random.nextInt(100, 999)}",
                title = title,
                description = description,
                price = price,
                originalPrice = price * 1.25,
                rating = 5.0f,
                reviewCount = 1,
                source = MarketplaceSource.LOCAL,
                category = category,
                imageUrl = if (imageUrl.isNotBlank()) imageUrl else "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=800&auto=format&fit=crop&q=80",
                additionalImages = "",
                stock = stock,
                sellerName = "My Verified Local Store",
                isFlashSale = false,
                externalAffiliateUrl = "",
                shippingEstimate = "Local Express 1-2 Days"
            )
            repository.insertProduct(newProduct)
            showToast("Product added to your inventory!")
        }
    }

    fun updateVendorStatus(vendorId: String, newStatus: String) {
        viewModelScope.launch {
            repository.updateVendorStatus(vendorId, newStatus)
            showToast("Vendor $vendorId status changed to $newStatus")
        }
    }

    private fun showToast(msg: String) {
        viewModelScope.launch {
            _toastMessage.emit(msg)
        }
    }

    private var flashSaleTimerJob: Job? = null
    private fun startFlashSaleTimer() {
        flashSaleTimerJob?.cancel()
        flashSaleTimerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                if (_flashSaleSeconds.value > 0) {
                    _flashSaleSeconds.value -= 1
                } else {
                    _flashSaleSeconds.value = 86400L // Reset to 24h
                }
            }
        }
    }
}
