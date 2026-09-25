package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.local.entity.CartItemEntity
import com.example.ui.components.SourceBadge
import com.example.ui.theme.AliExpressRed
import com.example.ui.theme.CyanPrimary
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.LocalVendorGreen
import com.example.ui.viewmodel.Screen
import com.example.ui.viewmodel.ZexoViewModel

@Composable
fun CartScreen(
    viewModel: ZexoViewModel,
    onNavigateToCheckout: () -> Unit,
    onContinueShopping: () -> Unit
) {
    val cartItems by viewModel.cartItems.collectAsStateWithLifecycle()
    val subtotal by viewModel.cartSubtotal.collectAsStateWithLifecycle()
    val promoCode by viewModel.promoCode.collectAsStateWithLifecycle()
    val discountPercent by viewModel.appliedDiscountPercent.collectAsStateWithLifecycle()

    var promoInput by remember { mutableStateOf(promoCode) }

    val discountAmount = (subtotal * discountPercent) / 100.0
    val shippingThreshold = 100.0
    val isFreeShipping = subtotal >= shippingThreshold
    val shippingCost = if (isFreeShipping || subtotal == 0.0) 0.0 else 9.99
    val finalTotal = (subtotal - discountAmount + shippingCost).coerceAtLeast(0.0)

    if (cartItems.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingBag,
                        contentDescription = null,
                        modifier = Modifier.size(46.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Your Cart is Empty",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Explore global trending items from Amazon, AliExpress, and local sellers.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onContinueShopping,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary),
                    modifier = Modifier.height(48.dp)
                ) {
                    Text("Explore Marketplace", fontWeight = FontWeight.Bold)
                }
            }
        }
        return
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(top = 12.dp, bottom = 110.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Free Shipping Indicator Bar
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LocalShipping,
                                contentDescription = null,
                                tint = if (isFreeShipping) EmeraldSuccess else CyanPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isFreeShipping) "You unlocked FREE Global Shipping!" else "Add ${viewModel.formatPrice(shippingThreshold - subtotal)} more for FREE Global Shipping",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        val progress = (subtotal / shippingThreshold).toFloat().coerceIn(0f, 1f)
                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = if (isFreeShipping) EmeraldSuccess else CyanPrimary,
                            trackColor = MaterialTheme.colorScheme.outlineVariant
                        )
                    }
                }
            }

            // Cart Items
            items(cartItems, key = { it.id }) { item ->
                CartItemRow(item = item, viewModel = viewModel)
            }

            // Coupon Code Application Box
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "Have a Promo / Discount Code?",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            OutlinedTextField(
                                value = promoInput,
                                onValueChange = { promoInput = it },
                                placeholder = { Text("e.g. ZEXO10 or GLOBAL50", fontSize = 12.sp) },
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp)
                                    .testTag("promo_code_input")
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = { viewModel.applyPromoCode(promoInput) },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary),
                                modifier = Modifier.height(48.dp)
                            ) {
                                Text("Apply", fontWeight = FontWeight.Bold)
                            }
                        }

                        if (discountPercent > 0) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "✓ Promo applied: $discountPercent% off",
                                color = EmeraldSuccess,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Price Breakdown
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Order Summary",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        CostRow(label = "Subtotal (${cartItems.sumOf { it.quantity }} items)", value = viewModel.formatPrice(subtotal))

                        if (discountPercent > 0) {
                            CostRow(label = "Discount ($discountPercent%)", value = "-${viewModel.formatPrice(discountAmount)}", isHighlight = true)
                        }

                        CostRow(
                            label = "Estimated Shipping",
                            value = if (shippingCost == 0.0) "FREE" else viewModel.formatPrice(shippingCost)
                        )

                        CostRow(label = "Duties & Customs", value = "Included (DDP)")

                        Spacer(modifier = Modifier.height(8.dp))
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Estimated Total",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = viewModel.formatPrice(finalTotal),
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Black,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            )
                        }
                    }
                }
            }
        }

        // Bottom Proceed to Checkout Button
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding(),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp,
            shadowElevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Total:", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(
                        text = viewModel.formatPrice(finalTotal),
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Button(
                    onClick = onNavigateToCheckout,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary),
                    modifier = Modifier
                        .height(48.dp)
                        .testTag("proceed_to_checkout_button")
                ) {
                    Icon(imageVector = Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Proceed to Checkout", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun CartItemRow(item: CartItemEntity, viewModel: ZexoViewModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Thumbnail
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(item.imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = item.productTitle,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Details
            Column(modifier = Modifier.weight(1f)) {
                SourceBadge(source = item.source)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.productTitle,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp,
                    maxLines = 1,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${item.selectedColor} • ${item.selectedSize}",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = viewModel.formatPrice(item.price),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // Stepper and Delete
            Column(horizontalAlignment = Alignment.End) {
                IconButton(
                    onClick = { viewModel.removeFromCart(item.id) },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = "Remove",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .padding(horizontal = 2.dp, vertical = 1.dp)
                ) {
                    IconButton(
                        onClick = { viewModel.updateCartQuantity(item.id, item.quantity - 1) },
                        modifier = Modifier.size(26.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Remove, contentDescription = "Decrease", modifier = Modifier.size(12.dp))
                    }
                    Text(
                        text = "${item.quantity}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 6.dp)
                    )
                    IconButton(
                        onClick = { viewModel.updateCartQuantity(item.id, item.quantity + 1) },
                        modifier = Modifier.size(26.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Increase", modifier = Modifier.size(12.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun CostRow(label: String, value: String, isHighlight: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = if (isHighlight) FontWeight.Bold else FontWeight.Medium,
            color = if (isHighlight) EmeraldSuccess else MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun CheckoutScreen(
    viewModel: ZexoViewModel,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    var currentStep by remember { mutableIntStateOf(1) } // 1: Shipping, 2: Delivery, 3: Payment
    val form by viewModel.checkoutForm.collectAsStateWithLifecycle()
    val isSubmitting by viewModel.isSubmittingOrder.collectAsStateWithLifecycle()
    val subtotal by viewModel.cartSubtotal.collectAsStateWithLifecycle()
    val discountPercent by viewModel.appliedDiscountPercent.collectAsStateWithLifecycle()

    val discountAmount = (subtotal * discountPercent) / 100.0
    val totalWithDelivery = (subtotal - discountAmount + form.deliveryCost).coerceAtLeast(0.0)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .navigationBarsPadding()
    ) {
        // Step Indicator Progress
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            StepPill(number = 1, title = "Address", isActive = currentStep == 1, isDone = currentStep > 1)
            StepDivider()
            StepPill(number = 2, title = "Delivery", isActive = currentStep == 2, isDone = currentStep > 2)
            StepDivider()
            StepPill(number = 3, title = "Payment", isActive = currentStep == 3, isDone = false)
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Step 1: Shipping Address
        if (currentStep == 1) {
            Text(
                text = "Step 1: Shipping Address",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(14.dp))

            OutlinedTextField(
                value = form.fullName,
                onValueChange = { viewModel.updateCheckoutForm(form.copy(fullName = it)) },
                label = { Text("Full Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().testTag("checkout_name_input")
            )
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = form.streetAddress,
                onValueChange = { viewModel.updateCheckoutForm(form.copy(streetAddress = it)) },
                label = { Text("Street Address & Apartment") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().testTag("checkout_address_input")
            )
            Spacer(modifier = Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = form.city,
                    onValueChange = { viewModel.updateCheckoutForm(form.copy(city = it)) },
                    label = { Text("City") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = form.zipCode,
                    onValueChange = { viewModel.updateCheckoutForm(form.copy(zipCode = it)) },
                    label = { Text("ZIP / Postal") },
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = form.country,
                onValueChange = { viewModel.updateCheckoutForm(form.copy(country = it)) },
                label = { Text("Country") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = form.phone,
                onValueChange = { viewModel.updateCheckoutForm(form.copy(phone = it)) },
                label = { Text("Phone Number for Courier") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { currentStep = 2 },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary),
                modifier = Modifier.fillMaxWidth().height(50.dp).testTag("continue_to_delivery")
            ) {
                Text("Continue to Delivery Method", fontWeight = FontWeight.Bold)
            }
        }

        // Step 2: Delivery Method
        if (currentStep == 2) {
            Text(
                text = "Step 2: Sourcing & Delivery Method",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(14.dp))

            val methods = listOf(
                Triple("AliExpress Tracked Air Freight (7-10 Days)", 0.0, "Free Tracked Global Shipping"),
                Triple("DHL Express Air Cargo (3-5 Days)", 19.99, "Priority Courier with customs pre-clearance"),
                Triple("Local Verified Express Courier (1-2 Days)", 9.50, "Direct from domestic fulfillment center")
            )

            methods.forEach { (name, cost, desc) ->
                val isSelected = form.deliveryMethod == name
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable {
                            viewModel.updateCheckoutForm(
                                form.copy(deliveryMethod = name, deliveryCost = cost)
                            )
                        },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        width = if (isSelected) 1.5.dp else 1.dp,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = name, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text(text = desc, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Text(
                            text = if (cost == 0.0) "FREE" else viewModel.formatPrice(cost),
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedButton(
                    onClick = { currentStep = 1 },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f).height(50.dp)
                ) {
                    Text("Back")
                }
                Button(
                    onClick = { currentStep = 3 },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary),
                    modifier = Modifier.weight(2f).height(50.dp).testTag("continue_to_payment")
                ) {
                    Text("Continue to Payment", fontWeight = FontWeight.Bold)
                }
            }
        }

        // Step 3: Payment Gateway
        if (currentStep == 3) {
            Text(
                text = "Step 3: Payment Gateway",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(14.dp))

            val paymentOptions = listOf(
                "Credit / Debit Card",
                "Digital Wallet / UPI (Apple Pay, Google Pay)",
                "Cash on Delivery (Local Sourced)"
            )

            paymentOptions.forEach { method ->
                val isSelected = form.paymentMethod == method
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { viewModel.updateCheckoutForm(form.copy(paymentMethod = method)) },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        width = if (isSelected) 1.5.dp else 1.dp,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (method.contains("Card")) Icons.Default.CreditCard else Icons.Default.Payments,
                            contentDescription = null,
                            tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(text = method, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    }
                }
            }

            if (form.paymentMethod.contains("Card")) {
                Spacer(modifier = Modifier.height(14.dp))
                // Mock Card Visual Box
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        OutlinedTextField(
                            value = form.cardNumber,
                            onValueChange = { viewModel.updateCheckoutForm(form.copy(cardNumber = it)) },
                            label = { Text("Card Number") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = form.cardExpiry,
                                onValueChange = { viewModel.updateCheckoutForm(form.copy(cardExpiry = it)) },
                                label = { Text("Expiry (MM/YY)") },
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = form.cardCvv,
                                onValueChange = { viewModel.updateCheckoutForm(form.copy(cardCvv = it)) },
                                label = { Text("CVV") },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Summary Box
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Final Amount to Pay:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(
                            text = viewModel.formatPrice(totalWithDelivery),
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Text(
                        text = "Includes all international taxes and dropshipping fulfillment relay.",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedButton(
                    onClick = { currentStep = 2 },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f).height(50.dp)
                ) {
                    Text("Back")
                }
                Button(
                    onClick = { viewModel.placeOrder() },
                    enabled = !isSubmitting,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = CyanPrimary),
                    modifier = Modifier.weight(2f).height(50.dp).testTag("place_order_button")
                ) {
                    if (isSubmitting) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Relaying Order...")
                    } else {
                        Icon(imageVector = Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Place Order", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun StepPill(number: Int, title: String, isActive: Boolean, isDone: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(
                    when {
                        isDone -> EmeraldSuccess
                        isActive -> CyanPrimary
                        else -> MaterialTheme.colorScheme.surfaceVariant
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isDone) {
                Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
            } else {
                Text(
                    text = "$number",
                    color = if (isActive) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = title,
            fontSize = 10.sp,
            fontWeight = if (isActive || isDone) FontWeight.Bold else FontWeight.Normal,
            color = if (isActive) CyanPrimary else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun StepDivider() {
    Box(
        modifier = Modifier
            .width(40.dp)
            .height(2.dp)
            .background(MaterialTheme.colorScheme.outlineVariant)
    )
}
