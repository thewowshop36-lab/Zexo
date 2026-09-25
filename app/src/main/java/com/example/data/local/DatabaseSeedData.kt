package com.example.data.local

import com.example.data.local.entity.ExternalApiConfigEntity
import com.example.data.local.entity.OrderEntity
import com.example.data.local.entity.ProductEntity
import com.example.data.local.entity.VendorEntity
import com.example.data.model.MarketplaceSource
import com.example.data.model.ProductCategory

object DatabaseSeedData {

    val sampleProducts = listOf(
        // Amazon Global Products
        ProductEntity(
            id = "AMZ-001",
            title = "Sony WH-1000XM5 Wireless Noise Canceling Headphones",
            description = "Industry-leading noise canceling with two processors and 8 microphones. Ultra-comfortable lightweight design with soft fit leather. Up to 30-hour battery life with quick charging.",
            price = 348.00,
            originalPrice = 399.99,
            rating = 4.8f,
            reviewCount = 1420,
            source = MarketplaceSource.AMAZON,
            category = ProductCategory.ELECTRONICS,
            imageUrl = "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=800&auto=format&fit=crop&q=80",
            additionalImages = "https://images.unsplash.com/photo-1484704849700-f032a568e944?w=800&auto=format&fit=crop&q=80,https://images.unsplash.com/photo-1546435770-a3e426bf472b?w=800&auto=format&fit=crop&q=80",
            stock = 45,
            sellerName = "Amazon Prime Direct Global",
            isFlashSale = true,
            flashSaleDiscount = 13,
            colorsJson = "Silver,Midnight Black,Midnight Blue",
            sizesJson = "Standard Fit",
            externalAffiliateUrl = "https://www.amazon.com/dp/B09XS7JWHH?tag=zexo-affiliate-20",
            shippingEstimate = "2-Day Priority Air Global Fulfillment",
            isFeatured = true
        ),
        ProductEntity(
            id = "AMZ-002",
            title = "Kindle Paperwhite (16 GB) – 6.8\" Glare-Free Display",
            description = "Now with a 6.8\" display and thinner borders, adjustable warm light, up to 10 weeks of battery life, and 20% faster page turns. Waterproof for reading by the pool or bath.",
            price = 149.99,
            originalPrice = 169.99,
            rating = 4.7f,
            reviewCount = 980,
            source = MarketplaceSource.AMAZON,
            category = ProductCategory.GADGETS,
            imageUrl = "https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=800&auto=format&fit=crop&q=80",
            additionalImages = "https://images.unsplash.com/photo-1512820790803-83ca734da794?w=800&auto=format&fit=crop&q=80",
            stock = 82,
            sellerName = "Amazon Devices Retail",
            isFlashSale = false,
            colorsJson = "Black,Agave Green,Denim Blue",
            sizesJson = "16 GB,32 GB Signature",
            externalAffiliateUrl = "https://www.amazon.com/dp/B08KTZ8249?tag=zexo-affiliate-20",
            shippingEstimate = "3-5 Days International Priority",
            isFeatured = true
        ),

        // AliExpress Direct Dropshipping Products
        ProductEntity(
            id = "ALI-001",
            title = "CyberPunk RGB Mechanical Gaming Keyboard Hot-Swappable",
            description = "Gasket mounted mechanical keyboard with custom pre-lubed linear switches, vibrant multi-mode per-key ARGB backlighting, CNC aluminum body, and Bluetooth 5.3 + 2.4GHz dongle.",
            price = 69.50,
            originalPrice = 119.00,
            rating = 4.9f,
            reviewCount = 2850,
            source = MarketplaceSource.ALIEXPRESS,
            category = ProductCategory.GADGETS,
            imageUrl = "https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=800&auto=format&fit=crop&q=80",
            additionalImages = "https://images.unsplash.com/photo-1595225476474-87563907a212?w=800&auto=format&fit=crop&q=80",
            stock = 150,
            sellerName = "Shenzhen E-Tech Global Store (AliExpress)",
            isFlashSale = true,
            flashSaleDiscount = 42,
            colorsJson = "Cyber Violet,Stealth Dark,Retro Beige",
            sizesJson = "Linear Red Switches,Tactile Brown,Clicky Blue",
            externalAffiliateUrl = "https://www.aliexpress.com/item/10050064219.html?aff_platform=zexo",
            shippingEstimate = "AliExpress Standard Tracked (7-10 days)",
            isFeatured = true
        ),
        ProductEntity(
            id = "ALI-002",
            title = "Aesthetic Minimalist Chronograph Watch with Sapphire Glass",
            description = "Ultra-slim 316L stainless steel case, Japanese Miyota quartz movement, genuine full-grain Italian leather strap, 50M water resistance, scratch-proof sapphire crystal glass.",
            price = 48.90,
            originalPrice = 89.90,
            rating = 4.6f,
            reviewCount = 612,
            source = MarketplaceSource.ALIEXPRESS,
            category = ProductCategory.FASHION,
            imageUrl = "https://images.unsplash.com/photo-1524805444758-089113d48a6d?w=800&auto=format&fit=crop&q=80",
            additionalImages = "https://images.unsplash.com/photo-1522335789203-aabd1fc54bc9?w=800&auto=format&fit=crop&q=80",
            stock = 64,
            sellerName = "Kronos Luxury Direct",
            isFlashSale = false,
            colorsJson = "Obsidian Black,Rose Gold,Arctic Silver",
            sizesJson = "40mm Case,42mm Case",
            externalAffiliateUrl = "https://www.aliexpress.com/item/10050073210.html?aff_platform=zexo",
            shippingEstimate = "AliExpress Direct Shipping (8-12 days)",
            isFeatured = false
        ),
        ProductEntity(
            id = "ALI-003",
            title = "Smart Ultrasonic Aromatherapy Diffuser with Ambient LED",
            description = "App controlled essential oil diffuser with timer, whisper-quiet ultrasonic atomization, 500ml water tank, 7 soothing ambient nightlight colors, auto shut-off safety protection.",
            price = 28.50,
            originalPrice = 52.00,
            rating = 4.7f,
            reviewCount = 890,
            source = MarketplaceSource.ALIEXPRESS,
            category = ProductCategory.HOME,
            imageUrl = "https://images.unsplash.com/photo-1608571423902-eed4a5ad8108?w=800&auto=format&fit=crop&q=80",
            additionalImages = "https://images.unsplash.com/photo-1547887537-6158d64c35b3?w=800&auto=format&fit=crop&q=80",
            stock = 120,
            sellerName = "HomeVibe Global Dropship",
            isFlashSale = true,
            flashSaleDiscount = 45,
            colorsJson = "Natural Wood Grain,Dark Walnut,Pure Marble",
            sizesJson = "500ml Tank,800ml Tank",
            externalAffiliateUrl = "https://www.aliexpress.com/item/10050041238.html?aff_platform=zexo",
            shippingEstimate = "AliExpress Saver Shipping (9-14 days)",
            isFeatured = false
        ),

        // Alibaba B2B Wholesale / Bulk Products
        ProductEntity(
            id = "BABA-001",
            title = "Industrial Portable Magnetic Power Bank 10000mAh (MOQ 5 pcs)",
            description = "Wholesale bulk package with MagSafe Qi2 certified fast wireless charging. Aircraft-grade aluminum alloy body with digital LED display percentage. Ready for private label branding.",
            price = 18.20,
            originalPrice = 35.00,
            rating = 4.9f,
            reviewCount = 340,
            source = MarketplaceSource.ALIBABA,
            category = ProductCategory.WHOLESALE,
            imageUrl = "https://images.unsplash.com/photo-1609592426508-cc296e8105d1?w=800&auto=format&fit=crop&q=80",
            additionalImages = "https://images.unsplash.com/photo-1620799140408-edc6dcb6d633?w=800&auto=format&fit=crop&q=80",
            stock = 500,
            sellerName = "Zhejiang PowerTech Manufacturer (Alibaba Verified)",
            isFlashSale = false,
            colorsJson = "Titanium Gray,Metallic White,Deep Navy",
            sizesJson = "Pack of 5,Pack of 20,Pack of 100",
            externalAffiliateUrl = "https://www.alibaba.com/product-detail/1600984128.html",
            shippingEstimate = "Alibaba Air Cargo (10-15 days with Customs Clearance)",
            moq = 5,
            isFeatured = true
        ),
        ProductEntity(
            id = "BABA-002",
            title = "Smart Bio-Ceramic Thermal Vacuum Water Bottle (MOQ 10 pcs)",
            description = "Double wall 18/8 food-grade stainless steel with internal bio-ceramic coating that prevents metallic taste. Touch temperature sensor on cap with smart drink reminders.",
            price = 9.80,
            originalPrice = 22.00,
            rating = 4.7f,
            reviewCount = 180,
            source = MarketplaceSource.ALIBABA,
            category = ProductCategory.WHOLESALE,
            imageUrl = "https://images.unsplash.com/photo-1602143407151-7111542de6e8?w=800&auto=format&fit=crop&q=80",
            additionalImages = "https://images.unsplash.com/photo-1517256064527-09c73fc73e38?w=800&auto=format&fit=crop&q=80",
            stock = 800,
            sellerName = "Guangzhou EcoLife Industrial Co.",
            isFlashSale = false,
            colorsJson = "Matte Black,Sand Dune,Sage Green",
            sizesJson = "600ml Bottle,1000ml Flask",
            externalAffiliateUrl = "https://www.alibaba.com/product-detail/1600874112.html",
            shippingEstimate = "Alibaba Express Freight (12-16 days)",
            moq = 10,
            isFeatured = false
        ),

        // Local Verified Marketplace Sellers
        ProductEntity(
            id = "LOC-001",
            title = "Zexo Signature Handcrafted Italian Leather Crossbody Bag",
            description = "Locally curated luxury crossbody bag made from top-grade vegetable-tanned Tuscan leather. Solid brass hardware, YKK Excella zippers, and reinforced canvas interior with laptop sleeve.",
            price = 185.00,
            originalPrice = 240.00,
            rating = 5.0f,
            reviewCount = 420,
            source = MarketplaceSource.LOCAL,
            category = ProductCategory.FASHION,
            imageUrl = "https://images.unsplash.com/photo-1548036328-c9fa89d128fa?w=800&auto=format&fit=crop&q=80",
            additionalImages = "https://images.unsplash.com/photo-1590874103328-eac38a683ce7?w=800&auto=format&fit=crop&q=80",
            stock = 25,
            sellerName = "Veritas Atelier Studio (Local Verified)",
            isFlashSale = true,
            flashSaleDiscount = 23,
            colorsJson = "Cognac Brown,Rich Espresso,Ebony Black",
            sizesJson = "Medium 13-inch,Large 15-inch",
            externalAffiliateUrl = "",
            shippingEstimate = "Local Same-Day / Next-Day Delivery",
            isFeatured = true
        ),
        ProductEntity(
            id = "LOC-002",
            title = "Pure Organics 24K Botanical Glow Facial Serum & Elixir",
            description = "Cold-pressed rosehip seed oil infused with 24K colloidal gold flakes, bakuchiol (natural retinol alternative), vitamin C & E for radiant, deeply hydrated skin. Vegan and cruelty-free.",
            price = 39.00,
            originalPrice = 58.00,
            rating = 4.9f,
            reviewCount = 530,
            source = MarketplaceSource.LOCAL,
            category = ProductCategory.BEAUTY,
            imageUrl = "https://images.unsplash.com/photo-1620916566398-39f1143ab7be?w=800&auto=format&fit=crop&q=80",
            additionalImages = "https://images.unsplash.com/photo-1608248597359-0097127e997d?w=800&auto=format&fit=crop&q=80",
            stock = 75,
            sellerName = "Bloom Naturals Labs",
            isFlashSale = false,
            colorsJson = "Standard Formula,Sensitive Skin Formula",
            sizesJson = "30ml Dropper,50ml Value Size",
            externalAffiliateUrl = "",
            shippingEstimate = "1-2 Days Local Courier",
            isFeatured = false
        )
    )

    val sampleOrders = listOf(
        OrderEntity(
            orderId = "ZXO-2026-8941",
            dateFormatted = "Sept 24, 2026",
            totalAmount = 417.50,
            status = "Shipped",
            currentStepIndex = 2, // Shipped
            shippingAddress = "742 Evergreen Terrace, Apt 4B, New York, NY 10001",
            recipientName = "Alex Mercer",
            paymentMethod = "Visa ending in •••• 4821",
            deliveryMethod = "AliExpress Tracked Air Freight",
            supplierTrackingNumber = "AE-US-928174921",
            source = MarketplaceSource.ALIEXPRESS,
            itemCount = 2,
            itemsSummary = "CyberPunk RGB Mechanical Gaming Keyboard + Aesthetic Watch",
            estimatedDeliveryDate = "Oct 01, 2026"
        ),
        OrderEntity(
            orderId = "ZXO-2026-7732",
            dateFormatted = "Sept 20, 2026",
            totalAmount = 348.00,
            status = "Delivered",
            currentStepIndex = 4, // Delivered
            shippingAddress = "742 Evergreen Terrace, Apt 4B, New York, NY 10001",
            recipientName = "Alex Mercer",
            paymentMethod = "Apple Pay / Digital Wallet",
            deliveryMethod = "Amazon Prime 2-Day Priority",
            supplierTrackingNumber = "TBA-8921740912",
            source = MarketplaceSource.AMAZON,
            itemCount = 1,
            itemsSummary = "Sony WH-1000XM5 Wireless Headphones",
            estimatedDeliveryDate = "Delivered on Sept 22, 2026"
        )
    )

    val defaultApiConfigs = listOf(
        ExternalApiConfigEntity(
            platformKey = "amazon",
            platformName = "Amazon Product Advertising API (PA-API v5.0)",
            apiKey = "AKIA-ZEXO-PROD-98124X",
            apiSecret = "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY",
            trackingOrAssociateId = "zexo-global-20",
            isConnected = true,
            autoSyncStock = true,
            profitMarginPercent = 12,
            lastSyncStatus = "Sync Successful (320 items active)",
            lastSyncTime = "15 mins ago",
            totalSyncedProducts = 320
        ),
        ExternalApiConfigEntity(
            platformKey = "aliexpress",
            platformName = "AliExpress Open Platform (Dropshipping Bridge)",
            apiKey = "ali_app_50291048",
            apiSecret = "sec_8219afbc8912d8a01",
            trackingOrAssociateId = "zexo_ae_affiliate",
            isConnected = true,
            autoSyncStock = true,
            profitMarginPercent = 25,
            lastSyncStatus = "Sync Successful (840 items active)",
            lastSyncTime = "40 mins ago",
            totalSyncedProducts = 840
        ),
        ExternalApiConfigEntity(
            platformKey = "alibaba",
            platformName = "Alibaba B2B Wholesale / RFQ Supplier Gateway",
            apiKey = "baba_b2b_trade_9918",
            apiSecret = "ali_trade_key_901238",
            trackingOrAssociateId = "zexo_wholesale_hub",
            isConnected = true,
            autoSyncStock = false,
            profitMarginPercent = 18,
            lastSyncStatus = "Connected (Wholesale Catalog Ready)",
            lastSyncTime = "2 hours ago",
            totalSyncedProducts = 215
        )
    )

    val sampleVendors = listOf(
        VendorEntity(
            vendorId = "VND-101",
            storeName = "Veritas Atelier Studio",
            ownerName = "Marco Bellini",
            email = "marco@veritas-atelier.com",
            category = "Leather Goods & Fashion",
            status = "Active",
            totalRevenue = 28450.00,
            totalOrders = 154,
            rating = 4.95f,
            joinedDate = "Jan 2025"
        ),
        VendorEntity(
            vendorId = "VND-102",
            storeName = "Bloom Naturals Labs",
            ownerName = "Sophia Chen",
            email = "sophia@bloomnaturals.co",
            category = "Organic Beauty & Skincare",
            status = "Active",
            totalRevenue = 19200.00,
            totalOrders = 492,
            rating = 4.88f,
            joinedDate = "March 2025"
        ),
        VendorEntity(
            vendorId = "VND-103",
            storeName = "Apex Audio Craft",
            ownerName = "Liam Vance",
            email = "liam@apexaudio.dev",
            category = "High-End Audio Equipment",
            status = "Pending Approval",
            totalRevenue = 0.0,
            totalOrders = 0,
            rating = 0.0f,
            joinedDate = "Sept 2026"
        )
    )
}
