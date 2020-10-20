package org.ridlo.resto.data

import com.google.firebase.Timestamp

class History {
    var foodId: Long? = null
    var gambarFood: String? = null
    var hargaFood: String? = null
    var namaFood: String? = null
    var completed: Boolean = false
    var created: Timestamp? = null
    var userId: String? = null

    constructor()

    constructor(
        foodId: Long?,
        gambarFood: String?,
        hargaFood: String?,
        namaFood: String?,
        completed: Boolean,
        created: Timestamp?,
        userId: String?
    ) {
        this.foodId = foodId
        this.gambarFood = gambarFood
        this.hargaFood = hargaFood
        this.namaFood = namaFood
        this.completed = completed
        this.created = created
        this.userId = userId
    }


}