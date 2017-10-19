package com.mathearo.smsretrieverdemo.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import com.mathearo.smsretrieverdemo.R
import com.mathearo.smsretrieverdemo.intent.BookListIntent
import com.mathearo.smsretrieverdemo.util.Utils
import kotlinx.android.synthetic.main.activity_verification_code.*
import kotlinx.android.synthetic.main.view_loading.*

/**
 * Created by buthmathearo on 10/19/17.
 */
class VerificationCodeActivity: AppCompatActivity() {

    private var mCode = ""
    private val mSmsBroadcastReceiver = getSmsBroadcastReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification_code)
        startSmsRetriever()

        next_btn.setOnClickListener {
            openBookList()
        }

        retry_btn.setOnClickListener {
            startSmsRetriever()
        }
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(mSmsBroadcastReceiver, IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION))
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(mSmsBroadcastReceiver)
    }

    private fun getSmsBroadcastReceiver(): BroadcastReceiver {
        return object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                Log.d("@@@", "onReceive()")
                if (SmsRetriever.SMS_RETRIEVED_ACTION == intent?.action) {
                    val extras = intent?.extras
                    val status = extras?.get(SmsRetriever.EXTRA_STATUS) as Status

                    when(status.statusCode) {
                        CommonStatusCodes.SUCCESS -> {
                            // Get SMS message contents
                            val message = extras.get(SmsRetriever.EXTRA_SMS_MESSAGE) as String
                            mCode = Utils.extractDigit(message)
                            code_input.setText(mCode)
                            // Extract one-time code from the message and complete verification
                            // by sending the code back to your server.
                            Log.d("@@@", mCode)
                        }
                        CommonStatusCodes.TIMEOUT -> {
                            Log.d("@@@", "Timeout (Only 5 Minutes)")
                            Toast.makeText(context, "Timeout, please click Retry.", Toast.LENGTH_LONG)
                        }
                    }
                }
            }
        }
    }

    private fun startSmsRetriever() {
        // Get an instance of SmsRetrieverClient, used to start listening for a matching
        // SMS message.
        val client = SmsRetriever.getClient(this)

        // Starts SmsRetriever, which waits for ONE matching SMS message until timeout
        // (5 minutes). The matching SMS message will be sent via a Broadcast Intent with
        // action SmsRetriever#SMS_RETRIEVED_ACTION.
        val task = client.startSmsRetriever()

        task.addOnSuccessListener {
            // Successfully started retriever, expect broadcast intent
            Log.d("@@@", "init sms retriever success");
        }

        task.addOnFailureListener{
            // Failed to start retriever, inspect Exception for more details
            Log.d("@@@", "init sms retriever success");
        }
    }

    private fun openBookList() {
        loading_container.visibility = View.VISIBLE

        Handler().postDelayed(Runnable {
            if (!mCode.isEmpty() && mCode == code_input.text.toString()){
                startActivity(BookListIntent(baseContext))
            } else {
                Toast.makeText(baseContext, "Verification code failed", Toast.LENGTH_SHORT).show()
            }

            loading_container.visibility = View.GONE
        }, 500)
    }

}