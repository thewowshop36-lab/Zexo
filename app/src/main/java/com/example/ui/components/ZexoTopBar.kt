package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.AdminPanelSettings
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Currency
import com.example.data.model.UserRole
import com.example.ui.theme.CyanDark
import com.example.ui.theme.CyanLight
import com.example.ui.theme.CyanPrimary
import com.example.ui.theme.VioletAccent
import com.example.ui.viewmodel.Screen
import com.example.ui.viewmodel.ZexoViewModel

@Composable
fun ZexoTopBar(
    viewModel: ZexoViewModel,
    currentScreen: Screen,
    currentUserRole: UserRole,
    currentCurrency: Currency,
    cartItemCount: Int,
    onNavigateBack: () -> Unit,
    onOpenCart: () -> Unit
) {
    var showRoleMenu by remember { mutableStateOf(false) }
    var showCurrencyMenu by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 3.dp,
        shadowElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .height(64.dp)
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val isRootScreen = currentScreen is Screen.Home ||
                    currentScreen is Screen.VendorDashboard ||
                    currentScreen is Screen.AdminPanel

            if (!isRootScreen) {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.testTag("top_bar_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            } else {
                // Brand Monogram Badge
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(CyanPrimary, VioletAccent)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Z",
                        color = Color.White,
                        fontWeight = FontWeight.Black,
                        fontSize = 20.sp
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "ZEXO",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.2.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Currency Switcher Chip
            Box {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .clickable { showCurrencyMenu = true }
                        .padding(horizontal = 8.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = currentCurrency.code,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Select Currency",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                DropdownMenu(
                    expanded = showCurrencyMenu,
                    onDismissRequest = { showCurrencyMenu = false }
                ) {
                    Currency.values().forEach { curr ->
                        DropdownMenuItem(
                            text = { Text("${curr.code} (${curr.symbol})") },
                            onClick = {
                                viewModel.setCurrency(curr)
                                showCurrencyMenu = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Role Switcher Pill
            Box {
                val roleBg = when (currentUserRole) {
                    UserRole.CUSTOMER -> MaterialTheme.colorScheme.primaryContainer
                    UserRole.VENDOR -> MaterialTheme.colorScheme.secondaryContainer
                    UserRole.ADMIN -> MaterialTheme.colorScheme.errorContainer
                }
                val roleFg = when (currentUserRole) {
                    UserRole.CUSTOMER -> MaterialTheme.colorScheme.onPrimaryContainer
                    UserRole.VENDOR -> MaterialTheme.colorScheme.onSecondaryContainer
                    UserRole.ADMIN -> MaterialTheme.colorScheme.onErrorContainer
                }

                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(roleBg)
                        .clickable { showRoleMenu = true }
                        .padding(horizontal = 10.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = when (currentUserRole) {
                            UserRole.CUSTOMER -> Icons.Outlined.Person
                            UserRole.VENDOR -> Icons.Outlined.Storefront
                            UserRole.ADMIN -> Icons.Outlined.AdminPanelSettings
                        },
                        contentDescription = "Current Role",
                        modifier = Modifier.size(14.dp),
                        tint = roleFg
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = currentUserRole.badge,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = roleFg
                    )
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = roleFg
                    )
                }

                DropdownMenu(
                    expanded = showRoleMenu,
                    onDismissRequest = { showRoleMenu = false }
                ) {
                    UserRole.values().forEach { role ->
                        DropdownMenuItem(
                            leadingIcon = {
                                Icon(
                                    imageVector = when (role) {
                                        UserRole.CUSTOMER -> Icons.Outlined.Person
                                        UserRole.VENDOR -> Icons.Outlined.Storefront
                                        UserRole.ADMIN -> Icons.Outlined.AdminPanelSettings
                                    },
                                    contentDescription = null
                                )
                            },
                            text = { Text(role.title) },
                            onClick = {
                                viewModel.setUserRole(role)
                                showRoleMenu = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(6.dp))

            // Cart Icon Button with reactive Badge
            IconButton(
                onClick = onOpenCart,
                modifier = Modifier.testTag("top_bar_cart_button")
            ) {
                BadgedBox(
                    badge = {
                        if (cartItemCount > 0) {
                            Badge(
                                containerColor = CyanPrimary,
                                contentColor = Color.White
                            ) {
                                Text(
                                    text = if (cartItemCount > 99) "99+" else "$cartItemCount",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Shopping Cart",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}
