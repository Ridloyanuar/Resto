package org.ridlo.resto.data

import com.google.firebase.Timestamp

class Makanan {
    var nama_makanan: String? = null
    var harga: String? = null
    var image: String? = null
    var completed: Boolean = false
    var created: Timestamp? = null
    var userId: String? = null

    constructor()

    constructor(
        nama_makanan: String?,
        harga: String?,
        image: String?,
        completed: Boolean,
        created: Timestamp?,
        userId: String?
    ) {
        this.nama_makanan = nama_makanan
        this.harga = harga
        this.image = image
        this.completed = completed
        this.created = created
        this.userId = userId
    }


}