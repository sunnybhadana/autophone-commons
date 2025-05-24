package com.revaltronics.commons.compose.extensions

import android.app.Activity
import android.content.Context
import com.revaltronics.commons.R
import com.revaltronics.commons.extensions.baseConfig
import com.revaltronics.commons.extensions.redirectToRateUs
import com.revaltronics.commons.extensions.toast
import com.revaltronics.commons.helpers.BaseConfig

val Context.config: BaseConfig get() = BaseConfig.newInstance(applicationContext)

fun Activity.rateStarsRedirectAndThankYou(stars: Int) {
    if (stars == 5) {
        redirectToRateUs()
    }
    toast(R.string.thank_you)
    baseConfig.wasAppRated = true
}
