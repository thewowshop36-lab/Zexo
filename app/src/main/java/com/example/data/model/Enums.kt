package com.example.data.model

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.AliExpressRed
import com.example.ui.theme.AlibabaOrange
import com.example.ui.theme.AmazonOrange
import com.example.ui.theme.CyanPrimary
import com.example.ui.theme.LocalVendorGreen

enum class MarketplaceSource(
    val displayName: String,
    val shortName: String,
    val badgeColor: Color,
    val deliveryDays: String,
    val iconName: String
) {
    ALL("All Global", "All", CyanPrimary, "Fast Global", "globe"),
    AMAZON("Amazon Global", "Amazon", AmazonOrange, "2-4 Days Prime", "amazon"),
    ALIEXPRESS("AliExpress Direct", "AliExpress", AliExpressRed, "7-12 Days Tracked", "aliexpress"),
    ALIBABA("Alibaba Wholesale", "Alibaba", AlibabaOrange, "10-15 Days Cargo", "alibaba"),
    LOCAL("Local Verified", "Local", LocalVendorGreen, "1-2 Days Express", "storefront")
}

enum class UserRole(val title: String, val badge: String) {
    CUSTOMER("Customer", "Buyer"),
    VENDOR("Seller / Vendor", "Partner"),
    ADMIN("Admin Superuser", "Super Admin")
}

enum class ProductCategory(val displayName: String, val iconRes: String) {
    ALL("All Items", "dashboard"),
    ELECTRONICS("Electronics", "devices"),
    FASHION("Fashion", "checkroom"),
    HOME("Home & Living", "cottage"),
    BEAUTY("Beauty & Care", "spa"),
    GADGETS("Gadgets & Gear", "precision_manufacturing"),
    WHOLESALE("Wholesale B2B", "inventory_2")
}

enum class Currency(val code: String, val symbol: String, val rateFromUsd: Double) {
    USD("USD", "$", 1.0),
    EUR("EUR", "€", 0.92),
    GBP("GBP", "£", 0.79),
    AED("AED", "AED ", 3.67),
    INR("INR", "₹", 83.5)
}
