package pe.com.onecanal.presentation.ui.features.mainDrawer.intent

sealed class MainDrawerIntent {
    object GetUserSession : MainDrawerIntent()
    object ClearUserSession : MainDrawerIntent()
}
