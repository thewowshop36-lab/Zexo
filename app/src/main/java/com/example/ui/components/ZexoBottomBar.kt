package com.example.ui.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Public
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyanDark
import com.example.ui.theme.CyanLight
import com.example.ui.theme.CyanPrimary
import com.example.ui.viewmodel.Screen

data class NavItem(
    val screen: Screen,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
)

@Composable
fun ZexoBottomBar(
    currentScreen: Screen,
    wishlistCount: Int,
    activeOrdersCount: Int,
    onNavigate: (Screen) -> Unit
) {
    val items = listOf(
        NavItem(Screen.Home, "Explore", Icons.Filled.Home, Icons.Outlined.Home, "nav_home"),
        NavItem(Screen.GlobalHub, "Global Hub", Icons.Filled.Public, Icons.Outlined.Public, "nav_global"),
        NavItem(Screen.Wishlist, "Wishlist", Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder, "nav_wishlist"),
        NavItem(Screen.Orders, "Orders", Icons.Filled.LocalShipping, Icons.Outlined.LocalShipping, "nav_orders"),
        NavItem(Screen.Profile, "Profile", Icons.Filled.Person, Icons.Outlined.Person, "nav_profile")
    )

    NavigationBar(
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.navigationBars)
            .testTag("main_navigation_bar"),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        items.forEach { item ->
            val isSelected = when (item.screen) {
                is Screen.Home -> currentScreen is Screen.Home
                is Screen.GlobalHub -> currentScreen is Screen.GlobalHub
                is Screen.Wishlist -> currentScreen is Screen.Wishlist
                is Screen.Orders -> currentScreen is Screen.Orders || currentScreen is Screen.OrderTrackingDetail
                is Screen.Profile -> currentScreen is Screen.Profile
                else -> false
            }

            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(item.screen) },
                icon = {
                    if (item.screen is Screen.Wishlist && wishlistCount > 0) {
                        BadgedBox(
                            badge = {
                                Badge(
                                    containerColor = CyanPrimary,
                                    contentColor = Color.White
                                ) {
                                    Text(text = "$wishlistCount", fontSize = 10.sp)
                                }
                            }
                        ) {
                            Icon(
                                imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                contentDescription = item.label,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    } else if (item.screen is Screen.Orders && activeOrdersCount > 0) {
                        BadgedBox(
                            badge = {
                                Badge(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary
                                ) {
                                    Text(text = "$activeOrdersCount", fontSize = 10.sp)
                                }
                            }
                        ) {
                            Icon(
                                imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                contentDescription = item.label,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    } else {
                        Icon(
                            imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                            contentDescription = item.label,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                label = {
                    Text(
                        text = item.label,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.testTag(item.testTag)
            )
        }
    }
}
