package org.ridlo.resto.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_profile.*
import org.ridlo.resto.BaseFragment
import org.ridlo.resto.R


class ProfileFragment : BaseFragment() {
    override fun getViewId(): Int = R.layout.fragment_profile

    override fun onFragmentCreated() {

        auth = FirebaseAuth.getInstance()

        tv_user.text = auth.currentUser!!.email

        button.setOnClickListener {
            signOut()
        }

    }


}