package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.ExternalApiConfigEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExternalApiConfigDao {
    @Query("SELECT * FROM api_configs")
    fun getAllConfigs(): Flow<List<ExternalApiConfigEntity>>

    @Query("SELECT * FROM api_configs WHERE platformKey = :key LIMIT 1")
    fun getConfigByKey(key: String): Flow<ExternalApiConfigEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConfigs(configs: List<ExternalApiConfigEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConfig(config: ExternalApiConfigEntity)

    @Query("UPDATE api_configs SET lastSyncStatus = :status, lastSyncTime = :time, totalSyncedProducts = :count WHERE platformKey = :key")
    suspend fun updateSyncStatus(key: String, status: String, time: String, count: Int)

    @Query("UPDATE api_configs SET isConnected = :connected WHERE platformKey = :key")
    suspend fun updateConnectionStatus(key: String, connected: Boolean)
}
