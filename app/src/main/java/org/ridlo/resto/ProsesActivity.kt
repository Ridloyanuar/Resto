package org.ridlo.resto

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.api.ResourceDescriptor
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.activity_proses.*
import org.ridlo.resto.data.History
import org.ridlo.resto.db.Food
import org.ridlo.resto.db.FoodCartDatabase
import org.ridlo.resto.presenters.FoodAdapter
import org.ridlo.resto.presenters.KonfirmasiAdapter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ProsesActivity : BaseActivity() {

    override fun getView(): Int = R.layout.activity_proses

    override fun onActivityCreated() {

        toolbar_atur.setTitle("Pesanan")
        setSupportActionBar(toolbar_atur)

        toolbar_atur.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
        toolbar_atur.setNavigationOnClickListener {
            finish()
        }

        auth = FirebaseAuth.getInstance()
        val userId =  auth.currentUser!!.email

        firebaseFirestore = FirebaseFirestore.getInstance()
        storageReference = FirebaseStorage.getInstance().reference


        userDatabase = FoodCartDatabase.getInstance(this)
        val getIntentStringChartLocation = getIntent().getStringExtra("EXTRA_FOOD")
        val listType = object : TypeToken<ArrayList<Food>?>() {}.type
        val listDataChartLocation = Gson().fromJson<List<Food>>(getIntentStringChartLocation, listType)

        val lastIndex = listDataChartLocation.lastIndex + 1
        val harga = listDataChartLocation.subList(0, lastIndex).sumBy { it.hargaFood.toInt() }
        btn_konfirm.setText("Checkout ( Rp. "+harga+" )")

        val mLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        rv_konfirmasi.apply {
            layoutManager = mLayoutManager
            adapter = KonfirmasiAdapter(listDataChartLocation)
        }


        btn_konfirm.setOnClickListener {
            val updateMap = HashMap<String, Any>()
            updateMap.put("pesanan", listDataChartLocation)
            updateMap.put("completed", false)
            updateMap.put("created", Timestamp(Date()))
            updateMap.put("userId", auth.currentUser!!.uid)
            updateMap.put("nama_pemesan", userId!!)


            firebaseFirestore.collection("Service").document().set(updateMap)
                .addOnCompleteListener {
                    history()
                    resetDb()
                    Toast.makeText(this@ProsesActivity, "Thanks For Order.", Toast.LENGTH_SHORT).show()
                    val mainIntent = Intent(this, MainUserActivity::class.java)
                    startActivity(mainIntent)
                }
                .addOnFailureListener {
                    Toast.makeText(this@ProsesActivity, "(FIRESTORE Error) : "+it.localizedMessage!!, Toast.LENGTH_SHORT).show()
                }
        }


    }

    private fun history() {
        val getIntentStringChartLocation = getIntent().getStringExtra("EXTRA_FOOD")
        val listType = object : TypeToken<ArrayList<Food>?>() {}.type
        val listDataChartLocation = Gson().fromJson<List<Food>>(getIntentStringChartLocation, listType)
        for (i in 0 until listDataChartLocation.size){
            val namaMakan = listDataChartLocation.get(i).namaFood
            val gambarMakan = listDataChartLocation.get(i).gambarFood
            val foodId = listDataChartLocation.get(i).foodId
            val hargaMakan= listDataChartLocation.get(i).hargaFood

            val history = History(foodId, gambarMakan, hargaMakan, namaMakan, false, Timestamp(Date()), auth.uid)

            firebaseFirestore.collection("History")
                .add(history)
                .addOnCompleteListener {
                    val intent = Intent(this, MainUserActivity::class.java)
                    startActivity(intent)
//                        finish()
                    Log.d("OnSucces", "History added successfully")
                }
                .addOnFailureListener {
                    Log.d("OnFailure", it.localizedMessage!!)
                }
        }
    }

    private fun resetDb() {
        Completable.fromAction {
            userDatabase!!.cartFoodDatabaseDAO.clear()
        }.observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}

                override fun onComplete() {
//                    Toast.makeText(this@ProsesActivity, "Thanks For Order.", Toast.LENGTH_SHORT).show()
                }

                override fun onError(e: Throwable) {

                }
            })
    }


}