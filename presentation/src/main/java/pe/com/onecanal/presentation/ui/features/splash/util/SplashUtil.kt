package pe.com.onecanal.presentation.ui.features.splash.util

import android.app.Activity
import pe.com.onecanal.data.config.DataStoreConfig
import pe.com.onecanal.presentation.ui.config.Settings
import pe.com.onecanal.presentation.ui.extensions.startNewActivity
import pe.com.onecanal.presentation.ui.features.newPassword.view.NewPasswordActivity

/**
 * Created by Sergio Carrillo Diestra on 17/02/2022.
 * scarrillo.peruapps@gmail.com
 * Peru Apps
 * Huacho, Peru.
 *
 **/
class SplashUtil {
    companion object {
        fun init(activity: Activity, getUserSession: () -> Unit) {
            val urlSplit = activity.intent.data.toString().split(Settings.DEEP_LINK_URL)
            return if (urlSplit.size > 1 && urlSplit[1].isNotEmpty()) {
                activity.startNewActivity<NewPasswordActivity>(
                    DataStoreConfig.ARGS_EMAIL_TOKEN to urlSplit[1],
                    clearStack = true
                )
            } else
                getUserSession.invoke()
        }
    }
}