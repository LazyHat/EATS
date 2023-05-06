package com.example.eats.data

import com.example.eats.data.products.NutritionFacts
import com.example.eats.data.products.ProductInfo
import com.example.eats.data.products.ProductState
import com.example.eats.data.products.db.infos.LocalInfo
import com.example.eats.data.products.db.products.LocalProduct
import com.example.eats.data.userdata.LocalUser
import com.example.eats.data.userdata.User
import com.example.eats.pages.eat.EatTime
import com.example.eats.pages.eat.pages.ResultDialogState

fun LocalUser.toExternal(): User =
    User(
        height = this.height,
        age = this.age,
        weight = this.weight,
        gender = this.gender,
        activity = activeness
    )

fun LocalUser.copy(user: User): LocalUser =
    this.copy(
        height = user.height,
        age = user.age,
        weight = user.weight,
        gender = user.gender,
        activeness = user.activity
    )

fun ProductState.toLocal(time: EatTime): LocalProduct =
    LocalProduct(time.name, info.id, weight)

fun List<Pair<EatTime, ProductState>>.filter(time: EatTime): List<ProductState> =
    this.filter { it.first == time }.map { it.second }

fun ResultDialogState.toProductState(): ProductState = ProductState(weight, info)

fun String.formatAsFloat(): String =
    this.filter { it.isDigit() || it == '.' }.let {
        it.count { c -> c == '.' }.let { count ->
            if (count > 1) it.filterIndexed { index, c -> !(c == '.' && index != it.indexOfLast { k -> k == '.' }) }
            else if (count == 1) it.dropLast(it.substringAfter('.').length.let { l -> if (l == 0) 0 else l - 1 })
            else it
        }
    }

fun NutritionFacts.getCurrentNutrition(weight: Float): NutritionFacts =
    NutritionFacts(
        this.calories * weight / 100,
        this.proteins * weight / 100,
        this.fats * weight / 100,
        this.carbohydrates * weight / 100
    )

fun LocalInfo.toExternal(): ProductInfo =
    ProductInfo(
        id,
        label,
        NutritionFacts(calories100, proteins100, fats100, carbohydrates100),
        pieceWeight
    )

fun ProductInfo.toLocal(): LocalInfo = LocalInfo(
    id,
    label,
    nutrition100.calories,
    nutrition100.proteins,
    nutrition100.fats,
    nutrition100.carbohydrates,
    pieceWeight
)

