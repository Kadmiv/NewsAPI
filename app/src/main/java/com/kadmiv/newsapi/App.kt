package com.kadmiv.newsapi

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.support.multidex.MultiDex
import android.support.multidex.MultiDexApplication
import android.icu.util.ULocale.getCountry

var locale = "ua"

class App : MultiDexApplication() {

    override fun onCreate() {
        MultiDex.install(applicationContext)
//        locale = applicationContext.resources.configuration.locale.isO3Country
        super.onCreate()
    }
}