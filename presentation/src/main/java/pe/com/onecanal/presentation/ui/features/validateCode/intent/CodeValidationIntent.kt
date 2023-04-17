package pe.com.onecanal.presentation.ui.features.validateCode.intent

sealed class CodeValidationIntent {
    class ValidateCode(
        val code: String,
        val email: String,
        val userId: Int
    ) : CodeValidationIntent()
}