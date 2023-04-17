package pe.com.onecanal.domain.entity

/**
 * Created by Sergio Carrillo Diestra on 27/01/2022.
 * scarrillo.peruapps@gmail.com
 * Peru Apps
 * Huacho, Peru.
 *
 **/
data class HistoryPagination(
     val items: List<HistoryItem>,
     val meta: MetaData
)