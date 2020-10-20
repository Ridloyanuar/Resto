package org.ridlo.resto

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_add_makanan.*
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.activity_edit_makanan.*
import org.ridlo.resto.data.Makanan
import java.util.*
import kotlin.collections.HashMap

class EditMakananActivity : BaseActivity() {

    override fun getView(): Int = R.layout.activity_edit_makanan

    override fun onActivityCreated() {

        toolbar_edit_makan.setTitle("Edit Makanan")
        toolbar_edit_makan.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)

        toolbar_edit_makan.setOnClickListener {
            finish()
        }
        auth = FirebaseAuth.getInstance()
        val userId =  auth.currentUser!!.uid

        firebaseFirestore = FirebaseFirestore.getInstance()
        storageReference = FirebaseStorage.getInstance().reference

        val namaMakanan = intent.getStringExtra("NAMA_MAKAN_EXTRA")
        val hargaMakanan = intent.getStringExtra("HARGA_EXTRA")

        et_makanan_edit.setText(namaMakanan)
        et_harga_edit.setText(hargaMakanan)

        btn_post_edit.setOnClickListener {

            val nama_makanan = et_makanan_edit.text.toString()
            val harga = et_harga_edit.text.toString()

            val makanan = Makanan(nama_makanan, harga, "as", false, Timestamp(Date()), userId)

            val updateMap = HashMap<String, Any>()
            updateMap.put("nama_makanan", nama_makanan)
            updateMap.put("harga", harga)
            updateMap.put("image", "as")
            updateMap.put("completed", false)
            updateMap.put("created", Timestamp(Date()))
            updateMap.put("userId", userId)


            firebaseFirestore.collection("Makanan").document(nama_makanan).update(updateMap)
                .addOnCompleteListener {
                    Toast.makeText(this@EditMakananActivity, "Food Updated.", Toast.LENGTH_SHORT).show()
                    val mainIntent = Intent(this, MainActivity::class.java)
                    startActivity(mainIntent)
                }
                .addOnFailureListener {
                    Toast.makeText(this@EditMakananActivity, "(FIRESTORE Error) : "+it.localizedMessage!!, Toast.LENGTH_SHORT).show()
                }

        }
    }

}