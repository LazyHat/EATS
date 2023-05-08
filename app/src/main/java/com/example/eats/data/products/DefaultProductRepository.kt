package com.example.eats.data.products

import com.example.eats.data.filter
import com.example.eats.data.products.db.ProductDao
import com.example.eats.data.products.db.day.Day
import com.example.eats.data.products.db.products.LocalProduct
import com.example.eats.data.toExternal
import com.example.eats.data.toLocal
import com.example.eats.pages.eat.EatTime
import com.example.eats.staticdata.DataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class DefaultProductsRepository(
    private val productDao: ProductDao,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.Default
) : ProductRepository {
    override suspend fun upsertProduct(time: EatTime, product: ProductState) =
        withContext(coroutineDispatcher) {
            productDao.upsertProduct(
                product.toLocal(time)
            )
        }

    override fun getAllProductsStream(): Flow<List<Pair<EatTime, ProductState>>> =
        productDao.getAllProductsStream().toExternal()

    override suspend fun deleteProduct(time: EatTime, product: ProductState) {
        withContext(coroutineDispatcher) {
            productDao.deleteProduct(product.info.id, time.name)
        }
    }

    override fun getUnusedInfoStream(time: EatTime): Flow<List<ProductInfo>> {
        val used = productDao.getAllProductsStream().toExternal()
            .map { it.filter(time).map { p -> p.info } }
        return combine(getAllProductInfoStream(), used) { all, _used ->
            all.filter { it !in _used }
        }
    }

    override suspend fun getAllProductsInfo(): List<ProductInfo> =
        withContext(coroutineDispatcher) {
            getAllProductInfoStream().first()
        }

    override suspend fun insertProductInfo(new: ProductInfo) {
        productDao.insertInfo(new.toLocal())
    }

    override fun getAllDaysStream(): Flow<List<Day>> {
        return MutableStateFlow(DataSource.days)
    }

    private fun getAllProductInfoStream(): Flow<List<ProductInfo>> =
        productDao.getAllInfosStream().map { data ->
            data.map { it.toExternal() }.toMutableList().apply {
                DataSource.productInfos.forEach {
                    add(it)
                }
            }.sortedBy { it.label }
        }

    private suspend fun LocalProduct.toState(): ProductState =
        getAllProductInfoStream().first().let {
            if (it.find { product -> product.id == id } != null)
                ProductState(weight, it.first { product -> product.id == id })
            else
                ProductState(
                    weight,
                    ProductInfo(
                        id,
                        "Не найден продукт",
                        NutritionFacts(
                            0f,
                            0f,
                            0f,
                            0f
                        ),
                        null
                    )
                )
        }

    private fun Flow<List<LocalProduct>>.toExternal(): Flow<List<Pair<EatTime, ProductState>>> =
        this.map { list ->
            list.map {
                EatTime.valueOf(it.time) to it.toState()
            }
        }
}
