package pe.com.onecanal.domain.dtos

import com.google.gson.annotations.SerializedName

data class DataUserRes (

    @SerializedName("id") var id : Int,
    @SerializedName("document_type") var documentType : String,
    @SerializedName("document_number") var documentNumber : String,
    @SerializedName("names") var names : String,
    @SerializedName("surnames") var surnames : String,
    @SerializedName("email") var email : String,
    @SerializedName("phone") var phone : Int,
    @SerializedName("role") var role : String,
    @SerializedName("business_id") var businessId : Int,
    @SerializedName("active") var active : Int,
    @SerializedName("attemps") var attemps : Int,
    @SerializedName("salary") var salary : String,
    @SerializedName("valid") var valid : Int,
    @SerializedName("validated_at") var validatedAt : String,
    @SerializedName("salary_view") var salaryView : Int,
    @SerializedName("salary_updated") var salaryUpdated : Int,
    @SerializedName("marital_status_id") var maritalStatusId : Int,
    @SerializedName("address") var address : String,
    @SerializedName("business_job") var businessJob : String,
    @SerializedName("created_at") var createdAt : String,
    @SerializedName("updated_at") var updatedAt : String,
    @SerializedName("sex") var sex : String,
    @SerializedName("birth_date") var birthDate : String,
    @SerializedName("city") var city : String,
    @SerializedName("province") var province : String,
    @SerializedName("cycle_number") var cycleNumber : String,
    @SerializedName("date_cycle_1") var dateCycle1 : String,
    @SerializedName("date_cycle_2") var dateCycle2 : String,
    @SerializedName("withdrawals_number") var withdrawalsNumber : Int
)

