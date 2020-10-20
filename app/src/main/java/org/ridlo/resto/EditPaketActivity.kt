package org.ridlo.resto

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_edit_makanan.*
import kotlinx.android.synthetic.main.activity_edit_paket.*
import org.ridlo.resto.data.Makanan
import org.ridlo.resto.data.Paket
import java.util.*
import kotlin.collections.HashMap

class EditPaketActivity : BaseActivity() {
    override fun getView(): Int = R.layout.activity_edit_paket

    override fun onActivityCreated() {

        toolbar_paket.setTitle("Edit Paket")
        toolbar_paket.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)

        toolbar_paket.setOnClickListener {
            finish()
        }

        auth = FirebaseAuth.getInstance()
        val userId =  auth.currentUser!!.uid

        firebaseFirestore = FirebaseFirestore.getInstance()
        storageReference = FirebaseStorage.getInstance().reference

        val namaPaket = intent.getStringExtra("NAMA_PAKET")
        val menu = intent.getStringExtra("MENU")
        val hargaPaket = intent.getStringExtra("HARGA_PAKET")

        et_nama_paket.setText(namaPaket)
        et_deskripsi.setText(menu)
        et_harga_paket.setText(hargaPaket)

        btn_post_paket.setOnClickListener {
            val nama_paket = et_nama_paket.text.toString()
            val menu_paket = et_deskripsi.text.toString()
            val harga_paket = et_harga_paket.text.toString()

            val makanan = Paket(nama_paket, hargaPaket, menu_paket, false, Timestamp(Date()), userId)

            val updateMap = HashMap<String, Any>()
            updateMap.put("nama_paket", nama_paket)
            updateMap.put("harga", harga_paket)
            updateMap.put("menu", menu_paket)
            updateMap.put("completed", false)
            updateMap.put("created", Timestamp(Date()))
            updateMap.put("userId", userId)


            firebaseFirestore.collection("Paket").document(nama_paket).update(updateMap)
                .addOnCompleteListener {
                    Toast.makeText(this@EditPaketActivity, "Paket Updated.", Toast.LENGTH_SHORT).show()
                    val mainIntent = Intent(this, MainActivity::class.java)
                    startActivity(mainIntent)
                }
                .addOnFailureListener {
                    Toast.makeText(this@EditPaketActivity, "(FIRESTORE Error) : "+it.localizedMessage!!, Toast.LENGTH_SHORT).show()
                }

        }
    }

}