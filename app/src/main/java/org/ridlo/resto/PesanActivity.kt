package org.ridlo.resto

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.frogobox.recycler.boilerplate.adapter.callback.FrogoAdapterCallback
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_pesan.*
import org.ridlo.resto.data.Makanan
import org.ridlo.resto.db.Food
import org.ridlo.resto.db.FoodCartDatabase

class PesanActivity : BaseActivity() {

    override fun getView(): Int = R.layout.activity_pesan

    override fun onActivityCreated() {

        userDatabase = FoodCartDatabase.getInstance(this)


        auth = FirebaseAuth.getInstance()

        //Makanan
        val queryMakanan = FirebaseFirestore.getInstance()
            .collection("Makanan")
            .orderBy("completed", Query.Direction.ASCENDING)
            .orderBy("created", Query.Direction.DESCENDING)

        queryMakanan.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            if (firebaseFirestoreException != null) {
                Log.e("EXCEPTION", "Listen failed!", firebaseFirestoreException)
//                return@EventListener
            }

            val historyList = mutableListOf<Makanan>()

            if (querySnapshot != null) {
                for (doc in querySnapshot) {
                    val history = doc.toObject(Makanan::class.java)
                    historyList.add(history)
                }
            }
            //adapter
            val adapterCallback = object : FrogoAdapterCallback<Makanan> {
                override fun setupInitComponent(view: View, data: Makanan) {
                    val gambar_food = data.image
                    val nama_food = data.nama_makanan
                    val harga_food = data.harga

                    view.findViewById<TextView>(R.id.tv_food).text = nama_food
                    view.findViewById<TextView>(R.id.tv_harga).text = harga_food
                    view.findViewById<Button>(R.id.btn_beli).setOnClickListener {
                        tambahFood(gambar_food!!, nama_food!!, harga_food!!)
                        val cartIntent = Intent(this@PesanActivity, CartActivity::class.java)
                        startActivity(cartIntent)
                    }

                }

                override fun onItemClicked(data: Makanan) {

                }

                override fun onItemLongClicked(data: Makanan) {

                }
            }

            rv_food!!.injector<Makanan>()
                .addData(historyList)
                .addCustomView(R.layout.food_list_item)
                .addEmptyView(null)
                .addCallback(adapterCallback)
                .createLayoutLinearVertical(false)
                .createAdapter()
                .build(rv_food)


        }
    }

    private fun toast(s: String) {
        Toast.makeText(this,s, Toast.LENGTH_SHORT).show()
    }

    private fun tambahFood(gambarFood: String, namaFood: String, hargaFood: String) {
        Completable.fromAction { val data = Food(gambarFood,namaFood, hargaFood)
            userDatabase.cartFoodDatabaseDAO.insert(data)
        }.observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}

                override fun onComplete() {
                    toast("Data store successfully")
//                    notifyDataSetChanged()
                }

                override fun onError(e: Throwable) {
                    toast(e.message.toString())
                }
            })
    }


}