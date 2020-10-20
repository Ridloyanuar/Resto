package org.ridlo.resto

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.frogobox.recycler.boilerplate.adapter.callback.FrogoAdapterCallback
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.fragment_home.*
import org.ridlo.resto.data.Makan
import org.ridlo.resto.data.Service
import org.ridlo.resto.db.Food

class DetailActivity : BaseActivity() {
    override fun getView(): Int = R.layout.activity_detail

    override fun onActivityCreated() {

        toolbar_detail.setTitle("Detail Pesanan")
        toolbar_detail.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)

        toolbar_detail.setOnClickListener {
            finish()
        }

        val nama_pemesan = intent.getStringExtra("NAMA")
        val tanggal = intent.getStringExtra("TANGGAL")

        val getIntentStringChartLocation = getIntent().getStringExtra("PESANAN")
        val listType = object : TypeToken<ArrayList<Makan>?>() {}.type
        val listDataChartLocation = Gson().fromJson<List<Makan>>(getIntentStringChartLocation, listType)

        textView.text = "Pesanan: "+nama_pemesan
        textView2.text = tanggal

        //adapter
        val adapterCallback = object : FrogoAdapterCallback<Makan> {
            override fun setupInitComponent(view: View, data: Makan) {
                view.findViewById<TextView>(R.id.tv_pemesan).text = data.namaFood
                view.findViewById<TextView>(R.id.tv_tanggal_service).text = data.hargaFood
            }

            override fun onItemClicked(data: Makan) {

            }

            override fun onItemLongClicked(data: Makan) {

            }
        }

        rv_pesananService!!.injector<Makan>()
            .addData(listDataChartLocation)
            .addCustomView(R.layout.detail_list)
            .addEmptyView(null)
            .addCallback(adapterCallback)
            .createLayoutLinearVertical(false)
            .createAdapter()
            .build(rv_pesananService)

    }

}