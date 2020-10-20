package org.ridlo.resto

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forget.*

class ForgetActivity : BaseActivity() {
    override fun getView(): Int = R.layout.activity_forget

    override fun onActivityCreated() {

        auth = FirebaseAuth.getInstance()

        txt_kembali_forget.setOnClickListener{ loginIntent() }

        btn_resetPassword.setOnClickListener {
            val et_reset = editText_reset.text.toString()
            if (TextUtils.isEmpty(et_reset)){
                Toast.makeText(this, "Field tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }else{
                auth.sendPasswordResetEmail(et_reset).addOnCompleteListener {
                    if (it.isSuccessful){
                        Toast.makeText(
                            this@ForgetActivity,
                            "Check email to reset your password!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }else{
                        Toast.makeText(
                            this@ForgetActivity,
                            "Fail to send reset password email!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun loginIntent() {
        startActivity(Intent(this@ForgetActivity, LoginActivity::class.java))
        finish()
    }

}