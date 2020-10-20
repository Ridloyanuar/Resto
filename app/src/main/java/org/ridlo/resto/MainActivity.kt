package org.ridlo.resto

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    lateinit var navController: NavController


    override fun getView(): Int = R.layout.activity_main

    override fun onActivityCreated() {
        navController = Navigation.findNavController(this,R.id.nav_host_fragment)
        bottom_navigation.setupWithNavController(navController)
    }

}