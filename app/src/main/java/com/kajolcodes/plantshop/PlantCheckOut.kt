package com.kajolcodes.plantshop

data class PlantCheckOut(
    var name: String = "",
    var price: Long = 0,  // Ensure it's a Long, not String or null
    var quantity: Long = 0,
    var ImageUrl: Any
)