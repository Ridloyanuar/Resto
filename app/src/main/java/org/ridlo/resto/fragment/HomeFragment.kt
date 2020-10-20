package org.ridlo.resto.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.frogobox.recycler.boilerplate.adapter.callback.FrogoAdapterCallback
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_makanan.*
import org.ridlo.resto.*
import org.ridlo.resto.data.Makan
import org.ridlo.resto.data.Makanan
import org.ridlo.resto.data.Service

class HomeFragment : BaseFragment() {

    override fun getViewId(): Int = R.layout.fragment_home

    override fun onFragmentCreated() {
        //Makanan
        val queryMakanan = FirebaseFirestore.getInstance()
            .collection("Service")
            .orderBy("completed", Query.Direction.ASCENDING)
            .orderBy("created", Query.Direction.DESCENDING)

        queryMakanan.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            if (firebaseFirestoreException != null) {
                Log.e("EXCEPTION", "Listen failed!", firebaseFirestoreException)
//                return@EventListener
            }

            val historyList = mutableListOf<Service>()

            if (querySnapshot != null) {
                for (doc in querySnapshot) {
                    val history = doc.toObject(Service::class.java)
                    historyList.add(history)
                }
            }
            //adapter
            val adapterCallback = object : FrogoAdapterCallback<Service> {
                override fun setupInitComponent(view: View, data: Service) {
                    view.findViewById<TextView>(R.id.tv_pemesan).text = data.nama_pemesan
                    view.findViewById<TextView>(R.id.tv_tanggal_service).text = data.created!!.toDate().toString()
                    view.findViewById<Button>(R.id.button4).setOnClickListener {

                        val stringListCartLocation = Gson().toJson(data.pesanan)


                        val detailIntent = Intent(activity!!, DetailActivity::class.java)
                        detailIntent.putExtra("PESANAN", stringListCartLocation)
                        detailIntent.putExtra("NAMA", data.nama_pemesan)
                        detailIntent.putExtra("TANGGAL", data.created!!.toDate().toString())
                        startActivity(detailIntent)
                    }
                }

                override fun onItemClicked(data: Service) {

                }

                override fun onItemLongClicked(data: Service) {

                }
            }

            rv_service!!.injector<Service>()
                .addData(historyList)
                .addCustomView(R.layout.service_list_item)
                .addEmptyView(null)
                .addCallback(adapterCallback)
                .createLayoutLinearVertical(false)
                .createAdapter()
                .build(rv_service)

        }
    }

}