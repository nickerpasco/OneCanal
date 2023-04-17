package pe.com.onecanal.data.network.response

data class MeResponse(
    val accounts: List<AccountResponse>,
    val business: BusinessResponse,
    val document_number: String,
    val document_type: String,
    val email: String,
    val id: Int,
    val names: String,
    val salary: Double,
    val available_salary:Double,
    val salary_view: Int,
    val surnames: String,
    val marital_status: MaritalStatusResponse?,
    val address: String?,
    val business_job: String?
)