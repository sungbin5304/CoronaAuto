package me.sungbin.coronaauto

import android.app.Application
import android.os.StrictMode
import dagger.hilt.android.HiltAndroidApp
import me.sungbin.coronaauto.util.ExceptionUtil


/**
 * Created by SungBin on 2020-09-11.
 */

@HiltAndroidApp
class CoronaAuto : Application() {

    override fun onCreate() {
        super.onCreate()

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        Thread.setDefaultUncaughtExceptionHandler { _, throwable ->
            ExceptionUtil.except(Exception(throwable), applicationContext)
        }
    }

}