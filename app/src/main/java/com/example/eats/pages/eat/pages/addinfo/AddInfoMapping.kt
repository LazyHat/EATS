package com.example.eats.pages.eat.pages.addinfo

import android.os.Bundle
import com.example.eats.pages.eat.getFieldState
import com.example.eats.pages.eat.putFieldState

const val LABEL_KEY = "label_b"
const val CALORIES_KEY = "cal_b"
const val PROTEINS_KEY = "prot_b"
const val FATS_KEY = "fats_b"
const val CARBOHYDRATES_KEY = "card"

fun AddInfoState.toBundle(): Bundle = Bundle().apply {
    putFieldState(LABEL_KEY, this@toBundle.label)
    putFieldState(CALORIES_KEY, this@toBundle.calories)
    putFieldState(PROTEINS_KEY, this@toBundle.proteins)
    putFieldState(FATS_KEY, this@toBundle.fats)
    putFieldState(CARBOHYDRATES_KEY, this@toBundle.carbohydrates)
}

fun Bundle.toAddInfoState(): AddInfoState = this.let {
    AddInfoState(
        it.getFieldState(LABEL_KEY),
        it.getFieldState(CALORIES_KEY),
        it.getFieldState(PROTEINS_KEY),
        it.getFieldState(FATS_KEY),
        it.getFieldState(CARBOHYDRATES_KEY)
    )
}