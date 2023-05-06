package com.example.eats.data.products.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.eats.data.products.db.infos.INFO_TN
import com.example.eats.data.products.db.infos.LocalInfo
import com.example.eats.data.products.db.products.LocalProduct
import com.example.eats.data.products.db.products.PRODUCT_TN
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Upsert(entity = LocalProduct::class)
    suspend fun upsertProduct(item: LocalProduct)

    @Query("SELECT * FROM $PRODUCT_TN")
    fun getAllProductsStream(): Flow<List<LocalProduct>>

    @Query("DELETE FROM $PRODUCT_TN WHERE id = :id AND time = :time")
    suspend fun deleteProduct(id: String, time: String)

    @Query("SELECT * FROM $INFO_TN")
    fun getAllInfosStream(): Flow<List<LocalInfo>>

    @Upsert(entity = LocalInfo::class)
    suspend fun upsertInfo(item: LocalInfo)
}