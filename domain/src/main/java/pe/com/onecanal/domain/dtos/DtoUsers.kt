package pe.com.onecanal.domain.dtos



import com.google.gson.annotations.SerializedName


data class DtoUsers (

    @SerializedName("id") var id : String,
    @SerializedName("document_type") var documentType : String,
    @SerializedName("document_number") var documentNumber : String,
    @SerializedName("birth_date") var birthDate : String,
    @SerializedName("names") var names : String,
    @SerializedName("surnames") var surnames : String,
    @SerializedName("email") var email : String,
    @SerializedName("province") var province : String,
    @SerializedName("city") var city : String,
    @SerializedName("sex") var sex : String,
    @SerializedName("phone") var phone : Int,
    @SerializedName("marital_status_id") var maritalStatusId : Int,
    @SerializedName("address") var address : String

)