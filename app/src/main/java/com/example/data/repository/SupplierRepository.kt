package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.data.model.Product
import com.example.data.model.SupplierOrderRequest
import com.example.data.remote.ApiClient
import com.example.data.remote.CJLoginRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SupplierRepository(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("supplier_prefs", Context.MODE_PRIVATE)

    suspend fun ensureLoggedIn(email: String, apiPassword: String): Result<String> =
        withContext(Dispatchers.IO) {
            val cachedToken = prefs.getString("cj_access_token", null)
            if (cachedToken != null) {
                return@withContext Result.success(cachedToken)
            }

            try {
                val response = ApiClient.cjAuthApi.login(CJLoginRequest(email, apiPassword))
                if (response.isSuccessful && response.body()?.data != null) {
                    val token = response.body()!!.data!!.accessToken
                    prefs.edit().putString("cj_access_token", token).apply()
                    Result.success(token)
                } else {
                    Result.failure(Exception("Login failed: ${response.body()?.message}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    suspend fun syncProducts(
        accessToken: String,
        pageNum: Int = 1,
        searchQuery: String? = null
    ): Result<List<Product>> = withContext(Dispatchers.IO) {
        try {
            val response = ApiClient.cjDropshippingApi.getProducts(
                accessToken = accessToken,
                pageNum = pageNum,
                pageSize = 50,
                searchQuery = searchQuery
            )

            if (response.isSuccessful && response.body()?.data != null) {
                val products = response.body()!!.data!!.list
                Result.success(products)
            } else {
                Result.failure(Exception("Sync failed: ${response.body()?.message}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun placeSupplierOrder(
        accessToken: String,
        orderRequest: SupplierOrderRequest
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            val response = ApiClient.cjDropshippingApi.createOrder(accessToken, orderRequest)
            if (response.isSuccessful && response.body()?.data != null) {
                val supplierOrderId = response.body()!!.data!!.supplierOrderId
                Result.success(supplierOrderId)
            } else {
                Result.failure(Exception("Order failed: ${response.body()?.message}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
