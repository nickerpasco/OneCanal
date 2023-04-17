package pe.com.onecanal.domain.entity

/**
 * Created by Sergio Carrillo Diestra on 4/02/2022.
 * scarrillo.peruapps@gmail.com
 * Peru Apps
 * Huacho, Peru.
 *
 **/
enum class SalaryAdvanceStatus(val stringValue: String) {
    REGISTERED("Solicitado"),
    CONFIRMED("Aprobado"),
    DEPOSITED("Abonado"),
    COMPLETED("Finalizado"),
    CANCELED("Cancelado"),
}