package org.ridlo.resto.presenters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.food_list_item.view.*
import org.ridlo.resto.R
import org.ridlo.resto.data.Makanan
import org.ridlo.resto.db.Food
import org.ridlo.resto.db.FoodCartDatabase


class FoodAdapter(data: List<Makanan?>) : RecyclerView.Adapter<FoodAdapter.MyHolder>() {
    var ambilData : List<Makanan?>
     lateinit var context: Context
    init {
        this.ambilData = data
    }
    private lateinit var userDatabase: FoodCartDatabase



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.food_list_item, parent,false)
        context = parent.context
        return MyHolder(view)
    }

    override fun getItemCount(): Int {
        return ambilData.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.bind(ambilData, position)
        val add_location = holder.itemView.btn_beli
        val photoRef = ambilData.get(position)?.image


//        Glide.with(context)
//            .load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="+photoRef+"&key=AIzaSyBopZTpiQKeyI3lFE9oypdFz_vjnZga7-c")
//            .transform(MultiTransformation(CenterCrop(), RoundedCornersTransformation(25,0)))
//            .error(R.drawable.noimage)
//            .into(poto)


        userDatabase = FoodCartDatabase.getInstance(context)

        //ini fungsi tombolnya
        add_location.setOnClickListener{

            val gambar_food = ambilData.get(position)?.image
            val nama_food = ambilData.get(position)?.nama_makanan
            val harga_food = ambilData.get(position)?.harga

            tambahFood(gambar_food!!, nama_food!!, harga_food!!)

        }

    }

    private fun tambahFood(gambar: String, name: String, harga: String) {
        Completable.fromAction { val data = Food(gambar,name, harga)
            userDatabase.cartFoodDatabaseDAO.insert(data)
        }.observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {}

                override fun onComplete() {
                    toast("Data store successfully")
                    notifyDataSetChanged()
                }

                override fun onError(e: Throwable) {
                    toast(e.message.toString())
                }
            })
    }

    private fun toast(s: String) {
        Toast.makeText(context,s, Toast.LENGTH_SHORT).show()
    }

    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(ambilData: List<Makanan?>, position: Int) {
//            itemView.iv_tempat.setImageResource(position) = ambilData.get(position)?.photos
//            Glide.with()
//                .load(ambilData.get(position)?.photos)
//                .into(itemView.iv_tempat)
            itemView.tv_food.text = ambilData.get(position)?.nama_makanan
            itemView.tv_harga.text = ambilData.get(position)?.harga


//            itemView.setOnClickListener {
//                action.onItemClick(ambilData, adapterPosition)
//            }
        }

    }

//    interface OnLocationItemClickListner{
//        fun onItemClick(item: List<ResultsItem?>, position: Int)
//    }


}
