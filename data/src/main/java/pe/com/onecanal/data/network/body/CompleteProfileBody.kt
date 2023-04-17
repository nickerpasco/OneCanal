package pe.com.onecanal.data.network.body

data class CompleteProfileBody(
    val marital_status_id: Int,
    val address: String,
    val business_job: String,
    val salary: String
)