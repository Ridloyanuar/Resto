package org.ridlo.resto

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_add_makanan.*
import kotlinx.android.synthetic.main.activity_add_makanan.btn_post
import kotlinx.android.synthetic.main.activity_add_paket.*
import org.ridlo.resto.data.Makanan
import org.ridlo.resto.data.Paket
import java.util.*

class AddPaketActivity : BaseActivity() {

    private var postImageUri: Uri? = null
    private var isChanged = false


    override fun getView(): Int = R.layout.activity_add_paket

    override fun onActivityCreated() {

        toolbar_paket.setTitle("Tambah Paket")
        toolbar_paket.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)

        toolbar_paket.setOnClickListener {
            finish()
        }

        auth = FirebaseAuth.getInstance()
        val userId =  auth.currentUser!!.uid

        firebaseFirestore = FirebaseFirestore.getInstance()
        storageReference = FirebaseStorage.getInstance().reference


        btn_post_paket.setOnClickListener {
            val nama_paket = et_nama_paket.text.toString()
            val menu = et_deskripsi.text.toString()
            val harga_paket = et_harga_paket.text.toString()

            if (!TextUtils.isEmpty(nama_paket) && !TextUtils.isEmpty(menu) && !TextUtils.isEmpty(harga_paket)){
                storeFireStore(nama_paket, menu, harga_paket, false, Timestamp(Date()), userId)
            }
        }
    }

    private fun storeFireStore(
        namaPaket: String,
        menu: String,
        hargaPaket: String,
        completed: Boolean,
        timestamp: Timestamp,
        userId: String
    ) {
        val userMap: MutableMap<String, Any> = HashMap()
        userMap["nama_paket"] = namaPaket
        userMap["harga"] = hargaPaket
        userMap["menu"] = menu
        userMap["completed"] = completed
        userMap["created"] = timestamp
        userMap["userId"] = userId

        firebaseFirestore.collection("Paket").document(namaPaket).set(userMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this@AddPaketActivity, "Paket Ditambahkan.", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    val error = task.exception!!.message
                    Toast.makeText(this@AddPaketActivity, "(FIRESTORE Error) : $error", Toast.LENGTH_SHORT).show()
                }
            }

    }


}