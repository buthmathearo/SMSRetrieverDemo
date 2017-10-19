package com.mathearo.smsretrieverdemo.view

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.mathearo.smsretrieverdemo.R
import com.mathearo.smsretrieverdemo.intent.VerificationCodeIntent
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.view_loading.*

/**
 * Created by buthmathearo on 10/19/17.
 */
class HomeActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        sign_in.setOnClickListener {
            phone_number_container.visibility = View.VISIBLE

            if (!phone_number.text.isEmpty()) {
                openVerificationCode()
            }

        }

    }

    private fun openVerificationCode() {
        // Simulate loading
        loading_container.visibility = View.VISIBLE

        Handler().postDelayed(Runnable {
            loading_container.visibility = View.GONE

            startActivity(VerificationCodeIntent(baseContext))
        }, 300)
    }
}