package org.ridlo.resto.data

import com.google.firebase.Timestamp
import org.ridlo.resto.db.Food

class Service {
    var pesanan: List<Makan>? = null
    var nama_pemesan: String? = null
    var completed: Boolean? = null
    var created: Timestamp? = null
    var userId: String? = null

    constructor()

    constructor(
        pesanan: List<Makan>?,
        nama_pemesan: String,
        completed: Boolean,
        created: Timestamp?,
        userId: String?
    ) {
        this.pesanan = pesanan
        this.nama_pemesan = nama_pemesan
        this.completed = completed
        this.created = created
        this.userId = userId
    }




}