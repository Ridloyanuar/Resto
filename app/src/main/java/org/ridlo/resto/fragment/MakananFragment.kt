package org.ridlo.resto.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.frogobox.recycler.boilerplate.adapter.callback.FrogoAdapterCallback
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_makanan.*
import org.ridlo.resto.*
import org.ridlo.resto.data.Makanan
import org.ridlo.resto.data.Paket


class MakananFragment : BaseFragment() {

    override fun getViewId(): Int = R.layout.fragment_makanan

    override fun onFragmentCreated() {

        auth = FirebaseAuth.getInstance()

        //Makanan
        val queryMakanan = FirebaseFirestore.getInstance()
            .collection("Makanan")
            .whereEqualTo("userId", auth.uid)
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
                    view.findViewById<TextView>(R.id.tv_namaMakanan).text = data.nama_makanan
                    view.findViewById<TextView>(R.id.tv_hargaMakanan).text = data.harga
                    view.findViewById<ImageButton>(R.id.ib_delete).setOnClickListener {

                        FirebaseFirestore.getInstance().collection("Makanan")
                            .document(data.nama_makanan!!)
                            .delete()
                            .addOnSuccessListener {
                                val intent = Intent(activity!!, MainActivity::class.java)
                                startActivity(intent)
                                Log.d("TAG", "DocumentSnapshot successfully deleted!")
                            }
                            .addOnFailureListener { e ->
                                Log.w(
                                    "TAG",
                                    "Error deleting document",
                                    e
                                )
                            }

                    }

                    view.findViewById<ImageButton>(R.id.ib_edit).setOnClickListener {
                        val editIntent = Intent(activity!!, EditMakananActivity::class.java)
                        editIntent.putExtra("NAMA_MAKAN_EXTRA", data.nama_makanan)
                        editIntent.putExtra("HARGA_EXTRA", data.harga)
                        startActivity(editIntent)
                    }
                }

                override fun onItemClicked(data: Makanan) {

                }

                override fun onItemLongClicked(data: Makanan) {

                }
            }

            rv_makanan!!.injector<Makanan>()
                .addData(historyList)
                .addCustomView(R.layout.makanan_list_item)
                .addEmptyView(null)
                .addCallback(adapterCallback)
                .createLayoutLinearVertical(false)
                .createAdapter()
                .build(rv_makanan)

        }

        val queryPaket = FirebaseFirestore.getInstance()
            .collection("Paket")
            .whereEqualTo("userId", auth.uid)
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
                    view.findViewById<TextView>(R.id.tv_namaMakanan).text = data.nama_paket
                    view.findViewById<TextView>(R.id.tv_hargaMakanan).text = data.harga
                    view.findViewById<ImageButton>(R.id.ib_delete).setOnClickListener {

                        FirebaseFirestore.getInstance().collection("Paket")
                            .document(data.nama_paket!!)
                            .delete()
                            .addOnSuccessListener {
                                val intent = Intent(activity!!, MainActivity::class.java)
                                startActivity(intent)
                                Log.d("TAG", "DocumentSnapshot successfully deleted!")
                            }
                            .addOnFailureListener { e ->
                                Log.w(
                                    "TAG",
                                    "Error deleting document",
                                    e
                                )
                            }

                    }

                    view.findViewById<ImageButton>(R.id.ib_edit).setOnClickListener {
                        val editIntent = Intent(activity!!, EditPaketActivity::class.java)
                        editIntent.putExtra("NAMA_PAKET", data.nama_paket)
                        editIntent.putExtra("MENU", data.menu)
                        editIntent.putExtra("HARGA_PAKET", data.harga)
                        startActivity(editIntent)
                    }
                }

                override fun onItemClicked(data: Paket) {

                }

                override fun onItemLongClicked(data: Paket) {

                }
            }

            rv_paket!!.injector<Paket>()
                .addData(historyList)
                .addCustomView(R.layout.makanan_list_item)
                .addEmptyView(null)
                .addCallback(adapterCallback)
                .createLayoutLinearVertical(false)
                .createAdapter()
                .build(rv_paket)

        }

        tv_tambahMakan.setOnClickListener {
            val addMakananIntent = Intent(activity, AddMakananActivity::class.java)
            startActivity(addMakananIntent)
        }

        tv_tambahPaket.setOnClickListener {
            val addPaket = Intent(activity, AddPaketActivity::class.java)
            startActivity(addPaket)
        }

    }


}