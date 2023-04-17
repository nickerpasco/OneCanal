package pe.com.onecanal.framework.hilt.application

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp
import pe.com.onecanal.BuildConfig
import timber.log.Timber

/**
 * Created by Sergio Carrillo Diestra on 10/01/2022.
 * scarrillo.peruapps@gmail.com
 * Peru Apps
 * Huacho, Peru.
 *
 **/
@HiltAndroidApp
class HiltApp : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}