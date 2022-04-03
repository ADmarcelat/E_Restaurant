package fr.isen.marcelat.e_restaurant.model

import java.io.Serializable

data class Category (
             val name_fr: String,
             val name_en: String,
             val items: ArrayList<Item>,
        ):Serializable
