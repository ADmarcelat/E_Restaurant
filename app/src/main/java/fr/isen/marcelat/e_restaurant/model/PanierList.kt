package fr.isen.marcelat.e_restaurant.model

import java.io.Serializable

data class PanierList(
    val info: ArrayList<ItemPanier>,
) : Serializable
