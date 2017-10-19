package com.mathearo.smsretrieverdemo.intent

import android.content.Context
import android.content.Intent
import com.mathearo.smsretrieverdemo.view.VerificationCodeActivity

/**
 * Created by buthmathearo on 10/19/17.
 */
class VerificationCodeIntent(ctx: Context) : Intent(ctx, VerificationCodeActivity::class.java)