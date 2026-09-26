package com.example.data.remote

import com.example.data.model.ProductListResponse
import com.example.data.model.SupplierOrderRequest
import com.example.data.model.SupplierOrderResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface CJDropshippingApi {

    @GET("product/list")
    suspend fun getProducts(
        @Header("CJ-Access-Token") accessToken: String,
        @Query("pageNum") pageNum: Int = 1,
        @Query("pageSize") pageSize: Int = 50,
        @Query("categoryId") categoryId: String? = null,
        @Query("productName") searchQuery: String? = null
    ): Response<ProductListResponse>

    @GET("product/query")
    suspend fun getProductDetail(
        @Header("CJ-Access-Token") accessToken: String,
        @Query("pid") productId: String
    ): Response<ProductListResponse>

    @POST("shopping/order/createOrder")
    suspend fun createOrder(
        @Header("CJ-Access-Token") accessToken: String,
        @Body order: SupplierOrderRequest
    ): Response<SupplierOrderResponse>

    @GET("shopping/order/getOrderDetail")
    suspend fun getOrderStatus(
        @Header("CJ-Access-Token") accessToken: String,
        @Query("orderId") supplierOrderId: String
    ): Response<SupplierOrderResponse>
}

interface CJAuthApi {
    @POST("authentication/getAccessToken")
    suspend fun login(
        @Body credentials: CJLoginRequest
    ): Response<CJLoginResponse>
}

data class CJLoginRequest(
    val email: String,
    val password: String
)

data class CJLoginResponse(
    val code: Int,
    val message: String,
    val data: CJTokenData?
)

data class CJTokenData(
    val accessToken: String,
    val accessTokenExpiryDate: String,
    val refreshToken: String,
    val refreshTokenExpiryDate: String
)
