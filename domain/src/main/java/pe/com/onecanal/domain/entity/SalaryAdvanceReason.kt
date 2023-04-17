package pe.com.onecanal.domain.entity

data class SalaryAdvanceReason(
    val id: Int,
    val name: String
) {
    override fun toString(): String {
        return name
    }


}