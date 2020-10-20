package org.ridlo.resto

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : BaseActivity() {
    override fun getView(): Int = R.layout.activity_dashboard

    override fun onActivityCreated() {
        button3.setOnClickListener {
//            val pegawaiIntent =
        }

        button2.setOnClickListener {
            val pesanIntent = Intent(this, PesanActivity::class.java)
            startActivity(pesanIntent)
        }
    }

}