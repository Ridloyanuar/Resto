package org.ridlo.resto.fragment

import android.app.Activity
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
import kotlinx.android.synthetic.main.fragment_menu.*
import org.ridlo.resto.BaseFragment
import org.ridlo.resto.CartActivity
import org.ridlo.resto.R
import org.ridlo.resto.data.Makanan
import org.ridlo.resto.data.Paket
import org.ridlo.resto.db.Food
import org.ridlo.resto.db.FoodCartDatabase

class MenuFragment : BaseFragment() {

    lateinit var userDatabase: FoodCartDatabase

    override fun getViewId(): Int = R.layout.fragment_menu

    override fun onFragmentCreated() {

        userDatabase = FoodCartDatabase.getInstance(requireActivity())
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
                        val cartIntent = Intent(activity, CartActivity::class.java)
                        startActivity(cartIntent)
                    }

                }

                override fun onItemClicked(data: Makanan) {

                }

                override fun onItemLongClicked(data: Makanan) {

                }
            }

            rv_food_user!!.injector<Makanan>()
                .addData(historyList)
                .addCustomView(R.layout.food_list_item)
                .addEmptyView(null)
                .addCallback(adapterCallback)
                .createLayoutLinearVertical(false)
                .createAdapter()
                .build(rv_food_user)


        }

        //Paket
        val queryPaket = FirebaseFirestore.getInstance()
            .collection("Paket")
            .orderBy("completed", Query.Direction.ASCENDING)
            .orderBy("created", Query.Direction.DESCENDING)

        queryPaket.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            if (firebaseFirestoreException != null) {
                Log.e("EXCEPTION", "Listen failed!", firebaseFirestoreException)
//                return@EventListener
            }

            val historyList = mutableListOf<Paket>()

            if (querySnapshot != null) {
                for (doc in querySnapshot) {
                    val history = doc.toObject(Paket::class.java)
                    historyList.add(history)
                }
            }
            //adapter
            val adapterCallback = object : FrogoAdapterCallback<Paket> {
                override fun setupInitComponent(view: View, data: Paket) {
                    val menu_paket = data.menu
                    val nama_paket = data.nama_paket
                    val harga_paket = data.harga

                    view.findViewById<TextView>(R.id.tv_nama_paket_user).text = nama_paket
                    view.findViewById<TextView>(R.id.tv_harga_paket_user).text = harga_paket
                    view.findViewById<TextView>(R.id.tv_menu_paket_user).text = menu_paket
                    view.findViewById<Button>(R.id.btn_beli).setOnClickListener {
                        tambahFood(menu_paket!!, nama_paket!!, harga_paket!!)
                        val cartIntent = Intent(activity, CartActivity::class.java)
                        startActivity(cartIntent)
                    }

                }

                override fun onItemClicked(data: Paket) {

                }

                override fun onItemLongClicked(data: Paket) {

                }
            }

            rv_paket_user!!.injector<Paket>()
                .addData(historyList)
                .addCustomView(R.layout.paket_list_item)
                .addEmptyView(null)
                .addCallback(adapterCallback)
                .createLayoutLinearVertical(false)
                .createAdapter()
                .build(rv_paket_user)


        }
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

    private fun toast(s: String) {
        Toast.makeText(activity,s, Toast.LENGTH_SHORT).show()
    }

}