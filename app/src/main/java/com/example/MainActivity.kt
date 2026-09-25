package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.UserRole
import com.example.ui.components.QuickViewBottomSheet
import com.example.ui.components.ZexoBottomBar
import com.example.ui.components.ZexoTopBar
import com.example.ui.screens.AdminSuperuserScreen
import com.example.ui.screens.CartScreen
import com.example.ui.screens.CheckoutScreen
import com.example.ui.screens.GlobalHubScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.OrderTrackingScreen
import com.example.ui.screens.ProductDetailScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.VendorDashboardScreen
import com.example.ui.screens.WishlistScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.Screen
import com.example.ui.viewmodel.ZexoViewModel
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                ZexoApp()
            }
        }
    }
}

@Composable
fun ZexoApp(viewModel: ZexoViewModel = viewModel()) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val currentUserRole by viewModel.currentUserRole.collectAsStateWithLifecycle()
    val currentCurrency by viewModel.currentCurrency.collectAsStateWithLifecycle()
    val cartCount by viewModel.cartTotalItemsCount.collectAsStateWithLifecycle()
    val wishlistItems by viewModel.wishlistItems.collectAsStateWithLifecycle()
    val orders by viewModel.orders.collectAsStateWithLifecycle()
    val quickViewProduct by viewModel.quickViewProduct.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.toastMessage.collectLatest { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    // Hardware and gesture back handler
    BackHandler(enabled = currentScreen !is Screen.Home) {
        viewModel.navigateBack()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            ZexoTopBar(
                viewModel = viewModel,
                currentScreen = currentScreen,
                currentUserRole = currentUserRole,
                currentCurrency = currentCurrency,
                cartItemCount = cartCount,
                onNavigateBack = { viewModel.navigateBack() },
                onOpenCart = { viewModel.navigateTo(Screen.Cart) }
            )
        },
        bottomBar = {
            val showBottomNav = currentScreen is Screen.Home ||
                    currentScreen is Screen.GlobalHub ||
                    currentScreen is Screen.Wishlist ||
                    currentScreen is Screen.Orders ||
                    currentScreen is Screen.Profile

            if (showBottomNav && currentUserRole == UserRole.CUSTOMER) {
                ZexoBottomBar(
                    currentScreen = currentScreen,
                    wishlistCount = wishlistItems.size,
                    activeOrdersCount = orders.count { it.currentStepIndex < 4 },
                    onNavigate = { screen -> viewModel.navigateTo(screen) }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            when (val screen = currentScreen) {
                is Screen.Home -> {
                    HomeScreen(
                        viewModel = viewModel,
                        onNavigateToProduct = { productId ->
                            viewModel.navigateTo(Screen.ProductDetail(productId))
                        }
                    )
                }

                is Screen.GlobalHub -> {
                    GlobalHubScreen(
                        viewModel = viewModel,
                        onNavigateToProduct = { productId ->
                            viewModel.navigateTo(Screen.ProductDetail(productId))
                        }
                    )
                }

                is Screen.Wishlist -> {
                    WishlistScreen(
                        viewModel = viewModel,
                        onNavigateToProduct = { productId ->
                            viewModel.navigateTo(Screen.ProductDetail(productId))
                        }
                    )
                }

                is Screen.Orders -> {
                    OrderTrackingScreen(
                        viewModel = viewModel,
                        selectedOrderId = null,
                        onBack = { viewModel.navigateBack() },
                        onSelectOrder = { orderId ->
                            viewModel.navigateTo(Screen.OrderTrackingDetail(orderId))
                        }
                    )
                }

                is Screen.OrderTrackingDetail -> {
                    OrderTrackingScreen(
                        viewModel = viewModel,
                        selectedOrderId = screen.orderId,
                        onBack = { viewModel.navigateBack() },
                        onSelectOrder = {}
                    )
                }

                is Screen.Profile -> {
                    ProfileScreen(viewModel = viewModel)
                }

                is Screen.ProductDetail -> {
                    ProductDetailScreen(
                        productId = screen.productId,
                        viewModel = viewModel,
                        onBack = { viewModel.navigateBack() }
                    )
                }

                is Screen.Cart -> {
                    CartScreen(
                        viewModel = viewModel,
                        onNavigateToCheckout = { viewModel.navigateTo(Screen.Checkout) },
                        onContinueShopping = { viewModel.navigateTo(Screen.Home) }
                    )
                }

                is Screen.Checkout -> {
                    CheckoutScreen(
                        viewModel = viewModel,
                        onBack = { viewModel.navigateBack() }
                    )
                }

                is Screen.VendorDashboard -> {
                    VendorDashboardScreen(viewModel = viewModel)
                }

                is Screen.AdminPanel -> {
                    AdminSuperuserScreen(viewModel = viewModel)
                }
            }

            // Quick View Modal Bottom Sheet
            quickViewProduct?.let { product ->
                QuickViewBottomSheet(
                    product = product,
                    viewModel = viewModel,
                    onDismiss = { viewModel.closeQuickView() }
                )
            }
        }
    }
}
