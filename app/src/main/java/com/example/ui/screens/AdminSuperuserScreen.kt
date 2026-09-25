package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.local.entity.ExternalApiConfigEntity
import com.example.data.local.entity.VendorEntity
import com.example.ui.theme.AliExpressRed
import com.example.ui.theme.AlibabaOrange
import com.example.ui.theme.AmazonOrange
import com.example.ui.theme.CyanPrimary
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.LocalVendorGreen
import com.example.ui.theme.RoseError
import com.example.ui.viewmodel.ZexoViewModel

@Composable
fun AdminSuperuserScreen(viewModel: ZexoViewModel) {
    val apiConfigs by viewModel.apiConfigs.collectAsStateWithLifecycle()
    val vendors by viewModel.vendors.collectAsStateWithLifecycle()
    val isSyncing by viewModel.isSyncingApi.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .navigationBarsPadding(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(top = 12.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Superuser Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Admin Superuser Panel",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Global marketplace governance, API sync engines, and vendor approvals.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.errorContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AdminPanelSettings,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // Platform Macro Metrics
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MetricBox(
                    title = "Total Platform GMV",
                    value = viewModel.formatPrice(342890.00),
                    icon = Icons.Default.TrendingUp,
                    color = CyanPrimary,
                    modifier = Modifier.weight(1f)
                )
                MetricBox(
                    title = "Commission (8%)",
                    value = viewModel.formatPrice(27431.20),
                    icon = Icons.Default.Paid,
                    color = EmeraldSuccess,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Global APIs Integration Section
        item {
            Text(
                text = "External Supplier APIs & Dropshipping Bridges",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        items(apiConfigs, key = { it.platformKey }) { config ->
            ApiConfigCard(
                config = config,
                isSyncing = isSyncing == config.platformKey,
                onSyncClick = { viewModel.syncMarketplaceApi(config.platformKey) },
                onToggleConnected = { connected ->
                    viewModel.updateApiConfig(config.copy(isConnected = connected))
                }
            )
        }

        // Vendor Approvals Section
        item {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Local Vendor Oversight & Approvals (${vendors.size})",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        items(vendors, key = { it.vendorId }) { vendor ->
            VendorRowCard(
                vendor = vendor,
                viewModel = viewModel,
                onStatusChange = { newStatus ->
                    viewModel.updateVendorStatus(vendor.vendorId, newStatus)
                }
            )
        }
    }
}

@Composable
fun ApiConfigCard(
    config: ExternalApiConfigEntity,
    isSyncing: Boolean,
    onSyncClick: () -> Unit,
    onToggleConnected: (Boolean) -> Unit
) {
    val platformColor = when (config.platformKey) {
        "amazon" -> AmazonOrange
        "aliexpress" -> AliExpressRed
        "alibaba" -> AlibabaOrange
        else -> CyanPrimary
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(if (config.isConnected) EmeraldSuccess else RoseError)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = config.platformName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Switch(
                    checked = config.isConnected,
                    onCheckedChange = onToggleConnected,
                    colors = SwitchDefaults.colors(checkedThumbColor = EmeraldSuccess)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Key: ${config.apiKey.take(12)}•••• | Affiliate: ${config.trackingOrAssociateId}",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Status: ${config.lastSyncStatus}",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = platformColor
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Synced Items: ${config.totalSyncedProducts}",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Button(
                    onClick = onSyncClick,
                    enabled = !isSyncing && config.isConnected,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = platformColor),
                    modifier = Modifier.height(34.dp).testTag("sync_button_${config.platformKey}")
                ) {
                    if (isSyncing) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(14.dp), strokeWidth = 2.dp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Syncing...", fontSize = 11.sp)
                    } else {
                        Icon(imageVector = Icons.Default.CloudSync, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Sync Live Stock", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun VendorRowCard(
    vendor: VendorEntity,
    viewModel: ZexoViewModel,
    onStatusChange: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = vendor.storeName, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Text(text = "${vendor.ownerName} • ${vendor.category}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(
                            when (vendor.status) {
                                "Active" -> EmeraldSuccess.copy(alpha = 0.15f)
                                "Pending Approval" -> Color(0xFFFF9900).copy(alpha = 0.15f)
                                else -> RoseError.copy(alpha = 0.15f)
                            }
                        )
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = vendor.status,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = when (vendor.status) {
                            "Active" -> EmeraldSuccess
                            "Pending Approval" -> Color(0xFFFF9900)
                            else -> RoseError
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Sales: ${viewModel.formatPrice(vendor.totalRevenue)} (${vendor.totalOrders} orders)",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    if (vendor.status != "Active") {
                        Button(
                            onClick = { onStatusChange("Active") },
                            shape = RoundedCornerShape(6.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess),
                            modifier = Modifier.height(30.dp)
                        ) {
                            Text("Approve", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    } else {
                        OutlinedButton(
                            onClick = { onStatusChange("Suspended") },
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.height(30.dp)
                        ) {
                            Text("Suspend", fontSize = 10.sp, color = RoseError)
                        }
                    }
                }
            }
        }
    }
}
