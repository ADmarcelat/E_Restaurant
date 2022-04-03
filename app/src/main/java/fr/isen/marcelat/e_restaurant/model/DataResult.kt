package fr.isen.marcelat.e_restaurant.model

import java.io.Serializable

data class DataResult(
    val data: ArrayList<Category>,
): Serializable
