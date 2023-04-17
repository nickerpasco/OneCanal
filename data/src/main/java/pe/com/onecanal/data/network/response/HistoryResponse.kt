package pe.com.onecanal.data.network.response

data class HistoryResponse(
    val data: List<Data>,
    val links: Links,
    val meta: Meta
)