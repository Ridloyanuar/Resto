package org.ridlo.resto.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey


@Entity(tableName = "food")
class Food{

    @PrimaryKey(autoGenerate = true)
    var foodId: Long = 0

    @ColumnInfo(name = "gambar")
    var gambarFood: String = ""

    @ColumnInfo(name = "nama")
    var namaFood: String = ""

    @ColumnInfo(name = "harga")
    var hargaFood: String = ""


    constructor(foodId: Long, gambarFood: String, namaFood: String, hargaFood: String) {
        this.foodId = foodId
        this.gambarFood = gambarFood
        this.namaFood = namaFood
        this.hargaFood = hargaFood
    }

    @Ignore
    constructor(gambarFood: String, namaFood: String, hargaFood: String) {
        this.gambarFood = gambarFood
        this.namaFood = namaFood
        this.hargaFood = hargaFood
    }


}





