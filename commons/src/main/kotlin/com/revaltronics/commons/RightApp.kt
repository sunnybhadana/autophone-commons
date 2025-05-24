package com.revaltronics.commons

import android.app.Application
import androidx.lifecycle.ProcessLifecycleOwner
import com.revaltronics.commons.extensions.appLockManager
import com.revaltronics.commons.extensions.checkUseEnglish

open class RightApp : Application() {

    open val isAppLockFeatureAvailable = false

    override fun onCreate() {
        super.onCreate()
        checkUseEnglish()
        setupAppLockManager()
    }

    private fun setupAppLockManager() {
        if (isAppLockFeatureAvailable) {
            ProcessLifecycleOwner.get().lifecycle.addObserver(appLockManager)
        }
    }
}
