package pe.com.onecanal.presentation.ui.base

import pe.com.onecanal.domain.entity.Failure

/**
 * Created by Sergio Carrillo Diestra on 12/01/2022.
 * scarrillo.peruapps@gmail.com
 * Peru Apps
 * Huacho, Peru.
 *
 **/
interface BaseIntentCallback {
    fun onLoading()
    fun onError(error: Failure)
}