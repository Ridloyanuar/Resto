package org.ridlo.resto

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : BaseActivity() {

    companion object {
        private const val TAG = "EmailPassword"

    }

    override fun getView(): Int = R.layout.activity_register

    override fun onActivityCreated() {
        auth = FirebaseAuth.getInstance()

//        Glide.with(this)
//            .load(R.drawable.logo_treavel)
//            .into(imageView6)

        txt_btnMasuk.setOnClickListener {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
        }
        btn_daftaar.setOnClickListener {
            createAccount(et_email_daftar.text.toString(), et_pass_daftar.text.toString())
        }
    }

    private fun createAccount(email: String, password: String) {
        Log.d(TAG, "createAccount:$email")
        if (!validateForm()) {
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "createUserWithEmail:success")
                    if (email.equals("admin@gmail.com")){
                        val mainIntent = Intent(this, MainActivity::class.java)
                        startActivity(mainIntent)
                    }else{
                        val mainIntent = Intent(this, MainUserActivity::class.java)
                        startActivity(mainIntent)
                    }

                } else {
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun validateForm(): Boolean {
        var valid = true


        val email = et_email_daftar.text.toString()
        if (TextUtils.isEmpty(email)) {
            et_email_daftar.error = "Required."
            valid = false
        } else {
            et_email_daftar.error = null
        }

        val pass = et_pass_daftar.text.toString()
        if (TextUtils.isEmpty(pass)) {
            et_pass_daftar.error = "Required."
            valid = false
        } else {
            et_pass_daftar.error = null
        }

        val konf = et_konf_pass.text.toString()
        if (TextUtils.isEmpty(konf)) {
            et_konf_pass.error = "Required."
            valid = false
        } else {
            et_konf_pass.error = null
        }

        if (pass.equals(konf)){
            valid
        } else{
            valid = false
        }

        return valid
    }

}