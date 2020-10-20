package org.ridlo.resto.data

import com.google.firebase.Timestamp

class Paket {
    var nama_paket: String? = null
    var harga: String? = null
    var menu: String? = null
    var completed: Boolean = false
    var created: Timestamp? = null
    var userId: String? = null

    constructor()

    constructor(
        nama_paket: String?,
        harga: String?,
        menu: String?,
        completed: Boolean,
        created: Timestamp?,
        userId: String?
    ) {
        this.nama_paket = nama_paket
        this.harga = harga
        this.menu = menu
        this.completed = completed
        this.created = created
        this.userId = userId
    }


}