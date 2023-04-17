package pe.com.onecanal.domain.entity

/**
 * Created by Sergio Carrillo Diestra on 11/01/2022.
 * scarrillo.peruapps@gmail.com
 * Peru Apps
 * Huacho, Peru.
 *
 **/
data class DocumentType(
    val id: Int,
    val name: String
)
{
    override fun toString()=name
}