package com.example

import com.example.data.local.DatabaseSeedData
import com.example.data.local.entity.OrderEntity
import com.example.data.local.entity.ProductEntity
import com.example.data.model.Currency
import com.example.data.model.MarketplaceSource
import com.example.data.model.ProductCategory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun testProductDiscountCalculation() {
        val product = ProductEntity(
            id = "TEST-01",
            title = "Test Item",
            description = "Test Description",
            price = 50.0,
            originalPrice = 100.0,
            rating = 4.5f,
            reviewCount = 10,
            source = MarketplaceSource.ALIEXPRESS,
            category = ProductCategory.GADGETS,
            imageUrl = "",
            additionalImages = "",
            stock = 20,
            sellerName = "Supplier"
        )
        assertEquals(50, product.discountPercent)
    }

    @Test
    fun testCurrencyRates() {
        val usdAmount = 100.0
        val eurConverted = usdAmount * Currency.EUR.rateFromUsd
        assertEquals(92.0, eurConverted, 0.01)

        val aedConverted = usdAmount * Currency.AED.rateFromUsd
        assertEquals(367.0, aedConverted, 0.01)
    }

    @Test
    fun testSeedDataIntegrity() {
        assertTrue(DatabaseSeedData.sampleProducts.isNotEmpty())
        assertTrue(DatabaseSeedData.defaultApiConfigs.isNotEmpty())
        assertTrue(DatabaseSeedData.sampleVendors.isNotEmpty())

        val amazonProducts = DatabaseSeedData.sampleProducts.filter { it.source == MarketplaceSource.AMAZON }
        assertTrue(amazonProducts.isNotEmpty())
    }
}
