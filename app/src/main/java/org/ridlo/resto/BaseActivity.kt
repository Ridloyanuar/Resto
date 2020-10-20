package org.ridlo.resto

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import org.ridlo.resto.db.FoodCartDatabase

abstract class BaseActivity: AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    lateinit var firebaseFirestore: FirebaseFirestore
    lateinit var storageReference: StorageReference
    lateinit var userDatabase: FoodCartDatabase




    abstract fun getView(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getView())
        onActivityCreated()
    }

    abstract fun onActivityCreated()

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun signOut(){
        auth = FirebaseAuth.getInstance()
        auth.signOut()
        val login = Intent(this, LoginActivity::class.java)
        startActivity(login)
    }




}