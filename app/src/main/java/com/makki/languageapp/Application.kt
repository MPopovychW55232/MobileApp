package com.makki.languageapp

import androidx.multidex.MultiDexApplication

/**
 * @author Maksym.Popovych
 */
class Application : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        declareKoin()
    }
}
