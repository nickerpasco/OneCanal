package pe.com.onecanal.presentation.ui.features.newPassword.intent

/**
 * Created by Sergio Carrillo Diestra on 10/01/2022.
 * scarrillo.peruapps@gmail.com
 * Peru Apps
 * Huacho, Peru.
 *
 **/
sealed class NewPasswordIntent {
    class CreateNewPassowrd(
        val code: String,
        val email: String,
        val userId: Int,
        val password: String,
        val password_confirmation: String
    ) : NewPasswordIntent()

}
