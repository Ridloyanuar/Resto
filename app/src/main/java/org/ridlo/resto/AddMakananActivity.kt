package org.ridlo.resto

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_add_makanan.*
import kotlinx.android.synthetic.main.activity_edit_makanan.*
import org.ridlo.resto.data.Makanan
import java.util.*

class AddMakananActivity : BaseActivity() {

    override fun getView(): Int = R.layout.activity_add_makanan

    override fun onActivityCreated() {
        toolbar.setTitle("Tambah Makanan")
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)

        toolbar.setOnClickListener {
            finish()
        }

        auth = FirebaseAuth.getInstance()
        val userId =  auth.currentUser!!.uid

        firebaseFirestore = FirebaseFirestore.getInstance()
        storageReference = FirebaseStorage.getInstance().reference

        btn_post.setOnClickListener {
            val nama_makanan = et_makanan.text.toString()
            val harga = et_harga.text.toString()

            if (!TextUtils.isEmpty(nama_makanan) && !TextUtils.isEmpty(harga)){
//
                val makanan = Makanan(nama_makanan, harga, "as", false, Timestamp(Date()), userId)

                firebaseFirestore.collection("Makanan").document(nama_makanan).set(makanan)
                    .addOnCompleteListener {
                        Toast.makeText(this@AddMakananActivity, "Food Added.", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this@AddMakananActivity, "(FIRESTORE Error) : "+it.localizedMessage!!, Toast.LENGTH_SHORT).show()
                    }
            }else{
                Toast.makeText(this@AddMakananActivity, "Isi Semua.", Toast.LENGTH_SHORT).show()

            }
        }


    }


}