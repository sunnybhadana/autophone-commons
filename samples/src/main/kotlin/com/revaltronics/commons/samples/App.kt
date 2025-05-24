package com.revaltronics.commons.samples

import com.github.ajalt.reprint.core.Reprint
import com.revaltronics.commons.RightApp
import com.revaltronics.commons.helpers.rustore.RuStoreModule

class App : RightApp() {
    override fun onCreate() {
        super.onCreate()
        Reprint.initialize(this)
        RuStoreModule.install(this, "309929407")
    }
}
