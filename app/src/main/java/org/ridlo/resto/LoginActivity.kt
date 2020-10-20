package org.ridlo.resto

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient


    companion object {
        private const val TAG = "EmailPassword"
        private const val TAG_GOOGLE = "GoogleActivity"
        private const val RC_SIGN_IN = 9001

    }

    override fun getView(): Int = R.layout.activity_login

    override fun onActivityCreated() {
//        Glide.with(this)
//            .load(R.drawable.logo_treavel)
//            .into(imageView5)

        txt_btnDaftar.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }
        btn_masuk.setOnClickListener {
            val emailUser = et_email_login.text.toString()
            if (emailUser.equals("admin@gmail.com")){
                signInAdmin(et_email_login.text.toString(), et_pass_login.text.toString())
            }else{
                signIn(et_email_login.text.toString(), et_pass_login.text.toString())
            }
        }
        txt_forget_pass.setOnClickListener {
            startActivity(Intent(this@LoginActivity, ForgetActivity::class.java))
        }

        hideKeyboard()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)


        txt_login_google.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

        auth = FirebaseAuth.getInstance()
    }

    private fun signInAdmin(emailAdmin: String, passAdmin: String) {
        Log.d(TAG, "signIn:$emailAdmin")
        if (!validateForm()) {
            return
        }

        auth.signInWithEmailAndPassword(emailAdmin, passAdmin)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmail:success")
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))

                } else {
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null){
            val email = auth.currentUser!!.email

            if (email.equals("admin@gmail.com")){
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            }else{
                startActivity(Intent(this@LoginActivity, MainUserActivity::class.java))
            }
        }
        updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {

    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account!!)
            } catch (e: ApiException) {
                Log.w(TAG, "Google sign in failed", e)
                updateUI(null)
            }
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + account.id!!)

        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    finish()
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }

    private fun signIn(email: String, password: String){
        Log.d(TAG, "signIn:$email")
        if (!validateForm()) {
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmail:success")
                    startActivity(Intent(this@LoginActivity, MainUserActivity::class.java))

                } else {
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }

    }

    private fun validateForm(): Boolean {
        var valid = true

        val email = et_email_login.text.toString()
        if (TextUtils.isEmpty(email)) {
            et_email_login.error = "Required."
            valid = false
        } else {
            et_email_login.error = null
        }

        val password = et_pass_login.text.toString()
        if (TextUtils.isEmpty(password)) {
            et_pass_login.error = "Required."
            valid = false
        } else {
            et_pass_login.error = null
        }

        return valid
    }

}