package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.MarketplaceSource
import com.example.ui.components.ProductCard
import com.example.ui.theme.AliExpressRed
import com.example.ui.theme.AlibabaOrange
import com.example.ui.theme.AmazonOrange
import com.example.ui.theme.CyanPrimary
import com.example.ui.theme.LocalVendorGreen
import com.example.ui.theme.VioletAccent
import com.example.ui.viewmodel.Screen
import com.example.ui.viewmodel.ZexoViewModel

@Composable
fun GlobalHubScreen(
    viewModel: ZexoViewModel,
    onNavigateToProduct: (String) -> Unit
) {
    val allProducts by viewModel.allProducts.collectAsStateWithLifecycle()
    val wishlistItems by viewModel.wishlistItems.collectAsStateWithLifecycle()
    val wishlistedIds = wishlistItems.map { it.productId }.toSet()

    val amazonProducts = allProducts.filter { it.source == MarketplaceSource.AMAZON }
    val aliProducts = allProducts.filter { it.source == MarketplaceSource.ALIEXPRESS }
    val babaProducts = allProducts.filter { it.source == MarketplaceSource.ALIBABA }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .navigationBarsPadding()
            .testTag("global_hub_screen"),
        contentPadding = PaddingValues(top = 12.dp, bottom = 90.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Header Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                listOf(CyanPrimary.copy(alpha = 0.15f), VioletAccent.copy(alpha = 0.1f))
                            )
                        )
                        .padding(18.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Public, contentDescription = null, tint = CyanPrimary, modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Global Sourcing & Dropship Hub",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Connect directly with overseas manufacturers, certified AliExpress suppliers, and Amazon global catalog with automated currency conversion and landed cost calculations.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }

        // Supplier Channels Grid
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Connected Supply Channels",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    SupplyPill(
                        name = "Amazon",
                        sub = "Prime PA-API",
                        color = AmazonOrange,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            viewModel.setMarketplaceSource(MarketplaceSource.AMAZON)
                            viewModel.navigateTo(Screen.Home)
                        }
                    )
                    SupplyPill(
                        name = "AliExpress",
                        sub = "Direct Dropship",
                        color = AliExpressRed,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            viewModel.setMarketplaceSource(MarketplaceSource.ALIEXPRESS)
                            viewModel.navigateTo(Screen.Home)
                        }
                    )
                    SupplyPill(
                        name = "Alibaba",
                        sub = "B2B Wholesale",
                        color = AlibabaOrange,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            viewModel.setMarketplaceSource(MarketplaceSource.ALIBABA)
                            viewModel.navigateTo(Screen.Home)
                        }
                    )
                }
            }
        }

        // Section: Amazon Global Best Sellers
        if (amazonProducts.isNotEmpty()) {
            item {
                SupplierSection(
                    title = "Amazon Global Fast Dispatch",
                    tag = "Prime PA-API Verified",
                    badgeColor = AmazonOrange,
                    products = amazonProducts,
                    viewModel = viewModel,
                    wishlistedIds = wishlistedIds,
                    onNavigateToProduct = onNavigateToProduct
                )
            }
        }

        // Section: AliExpress Trending Dropship Items
        if (aliProducts.isNotEmpty()) {
            item {
                SupplierSection(
                    title = "AliExpress Trending Gadgets",
                    tag = "Automated Dropship Bridge",
                    badgeColor = AliExpressRed,
                    products = aliProducts,
                    viewModel = viewModel,
                    wishlistedIds = wishlistedIds,
                    onNavigateToProduct = onNavigateToProduct
                )
            }
        }

        // Section: Alibaba Wholesale B2B
        if (babaProducts.isNotEmpty()) {
            item {
                SupplierSection(
                    title = "Alibaba B2B Wholesale Bulk",
                    tag = "Factory MOQ Tiered Pricing",
                    badgeColor = AlibabaOrange,
                    products = babaProducts,
                    viewModel = viewModel,
                    wishlistedIds = wishlistedIds,
                    onNavigateToProduct = onNavigateToProduct
                )
            }
        }
    }
}

@Composable
fun SupplyPill(
    name: String,
    sub: String,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(text = name, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = color)
            Text(text = sub, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun SupplierSection(
    title: String,
    tag: String,
    badgeColor: Color,
    products: List<com.example.data.local.entity.ProductEntity>,
    viewModel: ZexoViewModel,
    wishlistedIds: Set<String>,
    onNavigateToProduct: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(text = tag, fontSize = 11.sp, color = badgeColor, fontWeight = FontWeight.SemiBold)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            items(products) { item ->
                Box(modifier = Modifier.width(180.dp)) {
                    ProductCard(
                        product = item,
                        viewModel = viewModel,
                        isWishlisted = wishlistedIds.contains(item.id),
                        onProductClick = { onNavigateToProduct(item.id) },
                        onQuickViewClick = { viewModel.openQuickView(item) }
                    )
                }
            }
        }
    }
}
