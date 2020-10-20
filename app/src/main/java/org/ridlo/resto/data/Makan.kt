package org.ridlo.resto.data

import com.google.firebase.Timestamp

class Makan {
    var foodId: Long? = null
    var gambarFood: String? = null
    var hargaFood: String? = null
    var namaFood: String? = null

    constructor()

    constructor(
        foodId: Long?,
        gambarFood: String?,
        hargaFood: String?,
        namaFood: String?
    ) {
        this.foodId = foodId
        this.gambarFood = gambarFood
        this.hargaFood = hargaFood
        this.namaFood = namaFood
    }


}