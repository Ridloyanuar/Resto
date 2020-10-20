package org.ridlo.resto

import android.content.Intent
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.frogobox.recycler.boilerplate.adapter.callback.FrogoAdapterCallback
import com.google.gson.Gson
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_cart.*
import org.ridlo.resto.db.Food
import org.ridlo.resto.db.FoodCartDatabase

class CartActivity : BaseActivity() {

    private lateinit var food : ArrayList<Food>

    override fun getView(): Int = R.layout.activity_cart

    override fun onActivityCreated() {

        toolbar_cart.setTitle("Keranjang")
        setSupportActionBar(toolbar_cart)

        toolbar_cart.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
        toolbar_cart.setNavigationOnClickListener {
            finish()
        }

        food = ArrayList()
        userDatabase = FoodCartDatabase.getInstance(this)
        readData()

        txt_tambah_cart.setOnClickListener {
            val mainIntent = Intent(this, MainUserActivity::class.java)
            startActivity(mainIntent)
        }
    }

    private fun toast(s: String) {
        Toast.makeText(this,s, Toast.LENGTH_SHORT).show()
    }

    private fun readData() {
        userDatabase!!.cartFoodDatabaseDAO.getAllLocation()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : DisposableSingleObserver<List<Food>>() {
                override fun onSuccess(result: List<Food>) {
//                    setupAdapter(result)
                    setupFrogoRecycler(result)
                }

                override fun onError(e: Throwable) {
                    toast("Empty data")
                }
            })
    }

    private fun setupFrogoRecycler(result: List<Food>) {
        setupCheckout(result)

        val adapterCallback = object : FrogoAdapterCallback<Food> {
            override fun setupInitComponent(view: View, data: Food) {
//
//                Glide.with(this@CartActivity)
//                    .load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="+data.gambarLokasi+"&key=AIzaSyBopZTpiQKeyI3lFE9oypdFz_vjnZga7-c")
//                    .transform(MultiTransformation(CenterCrop(), RoundedCornersTransformation(10,   0)))
//                    .error(R.drawable.noimage)
//                    .into(view.findViewById<ImageView>(R.id.iv_cart))

                view.findViewById<TextView>(R.id.tv_cart_name).text = data.namaFood
                view.findViewById<TextView>(R.id.txt_location).text = data.hargaFood
                view.findViewById<ImageButton>(R.id.ib_delete).setOnClickListener {
                    val id = data.foodId
                    deleted(id)
                }
            }

            override fun onItemClicked(data: Food) {

            }

            override fun onItemLongClicked(data: Food) {

            }
        }

        rv_cart.injector<Food>()
            .addData(result)
            .addCustomView(R.layout.item_list_cart)
            .addEmptyView(null)
            .addCallback(adapterCallback)
            .createLayoutLinearVertical(false)
            .createAdapter()
            .build(rv_cart)
    }

    private fun setupCheckout(result: List<Food>) {
        btn_checkout.setOnClickListener {
            val intent = Intent(this@CartActivity, ProsesActivity::class.java)
            val stringListCartLocation = Gson().toJson(result)
            intent.putExtra("EXTRA_FOOD", stringListCartLocation)
            startActivity(intent)
        }
    }

    private fun deleted(id: Long) {
        Completable.fromAction {
            userDatabase!!.cartFoodDatabaseDAO.deleteByItemId(id)
        }.observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.single())
            .subscribe(object : CompletableObserver {
                override fun onComplete() {
                    toast("deleted successfully")
                    readData()
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onError(e: Throwable) {
                    toast("deleted invalid")
                }

            })
    }

}