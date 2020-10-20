package org.ridlo.resto.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.frogobox.recycler.boilerplate.adapter.callback.FrogoAdapterCallback
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_history.*
import kotlinx.android.synthetic.main.fragment_home.*
import org.ridlo.resto.BaseFragment
import org.ridlo.resto.R
import org.ridlo.resto.data.History
import org.ridlo.resto.data.Service

class HistoryFragment : BaseFragment() {
    override fun getViewId(): Int = R.layout.fragment_history

    override fun onFragmentCreated() {
        auth = FirebaseAuth.getInstance()


        val queryMakanan = FirebaseFirestore.getInstance()
            .collection("History")
            .whereEqualTo("userId", auth.uid)
            .orderBy("completed", Query.Direction.ASCENDING)
            .orderBy("created", Query.Direction.DESCENDING)

        queryMakanan.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            if (firebaseFirestoreException != null) {
                Log.e("EXCEPTION", "Listen failed!", firebaseFirestoreException)
//                return@EventListener
            }

            val historyList = mutableListOf<History>()

            if (querySnapshot != null) {
                for (doc in querySnapshot) {
                    val history = doc.toObject(History::class.java)
                    historyList.add(history)
                }
            }
            //adapter
            val adapterCallback = object : FrogoAdapterCallback<History> {
                override fun setupInitComponent(view: View, data: History) {
                    view.findViewById<TextView>(R.id.tv_makan_history).text = data.namaFood
                    view.findViewById<TextView>(R.id.tv_harga_history).text = data.hargaFood

                }

                override fun onItemClicked(data: History) {

                }

                override fun onItemLongClicked(data: History) {

                }
            }

            rv_history!!.injector<History>()
                .addData(historyList)
                .addCustomView(R.layout.history_list_item)
                .addEmptyView(null)
                .addCallback(adapterCallback)
                .createLayoutLinearVertical(false)
                .createAdapter()
                .build(rv_history)

        }
    }

}