package com.dota.tracker.opendota.model

data class Cosmetic(
    val creation_date: String,
    val image_inventory: String,
    val image_path: String,
    val item_description: String,
    val item_id: Int,
    val item_name: String,
    val item_rarity: String,
    val item_type_name: String,
    val name: String,
    val prefab: String,
    val used_by_heroes: String
)