package org.ridlo.resto.presenters

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import kotlinx.android.synthetic.main.food_list_item.view.*
import kotlinx.android.synthetic.main.proses_list_item.view.*
import org.ridlo.resto.R
import org.ridlo.resto.data.Makanan
import org.ridlo.resto.db.Food


class KonfirmasiAdapter(val food: List<Food>) : RecyclerView.Adapter<KonfirmasiAdapter.TimeLineViewHolder>() {

    var ambilData: List<Food?>
    lateinit var context: Context

    init {
        this.ambilData = food
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeLineViewHolder {
        var view =
            LayoutInflater.from(parent.context).inflate(R.layout.proses_list_item, parent, false)
        context = parent.context
        return TimeLineViewHolder(view)
    }

    override fun onBindViewHolder(holder: TimeLineViewHolder, position: Int) {
        holder.bind(ambilData, position)
    }

    override fun getItemCount() = ambilData.size

    class TimeLineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(ambilData: List<Food?>, position: Int) {
            itemView.tv_nama_proses.text = ambilData.get(position)?.namaFood
            itemView.tv_harga_proses.text = ambilData.get(position)?.hargaFood

        }

    }
}



