package pe.com.onecanal.framework.hilt.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import pe.com.onecanal.domain.repository.AppRepository
import pe.com.onecanal.domain.usecases.*

/**
 * Created by Sergio Carrillo Diestra on 10/01/2022.
 * scarrillo.peruapps@gmail.com
 * Peru Apps
 * Huacho, Peru.
 *
 **/
@Module
@InstallIn(ViewModelComponent::class)
class UserCasesModule {
    @Provides
    fun providesGetDocumentTypesUseCase(appRepository: AppRepository) =
        GetDocumentTypesUseCase(appRepository)

    @Provides
    fun providesValidateAccountUseCase(appRepository: AppRepository) =
        AccountValidationUseCase(appRepository)

    @Provides
    fun providesCreateNewPasswordUseCase(appRepository: AppRepository) =
        CreateNewPasswordUseCase(appRepository)

    @Provides
    fun validCodeUseCase(appRepository: AppRepository) =
        ValidCodeUseCase(appRepository)

    @Provides
    fun providesDoLoginUseCase(appRepository: AppRepository) =
        DoLoginUseCase(appRepository)

    @Provides
    fun providesForgotPasswordUseCase(appRepository: AppRepository) =
        ForgotPasswordUseCase(appRepository)

    @Provides
    fun providesGetFeesUseCase(appRepository: AppRepository) =
        GetFeesUseCase(appRepository)

    @Provides
    fun providesGetSalaryAdvanceReasonsUseCase(appRepository: AppRepository) =
        GetSalaryAdvanceReasonsUseCase(appRepository)

    @Provides
    fun providesSaveUserSessionUseCase(appRepository: AppRepository) =
        SaveUserSessionUseCase(appRepository)

    @Provides
    fun providesGetUserSessionUseCase(appRepository: AppRepository) =
        GetUserSessionUseCase(appRepository)

    @Provides
    fun providesGeSalaryUseCase(appRepository: AppRepository) =
        GetSalaryUseCase(appRepository)

    @Provides
    fun providesGetBankAccountsUseCase(appRepository: AppRepository) =
        GetBankAccountsFromSessionUseCase(appRepository)

    @Provides
    fun providesSalaryAdvanceDetailsUseCase(appRepository: AppRepository) =
        GetSalaryAdvanceDetailsUseCase(appRepository)

    @Provides
    fun providesSaveSalaryAdvanceUseCase(appRepository: AppRepository) =
        SaveSalaryAdvanceUseCase(appRepository)

    @Provides
    fun providesGetShowSalarySwitchStateUseCase(appRepository: AppRepository) =
        GetSalarySwitchStateUseCase(appRepository)

    @Provides
    fun providesSaveShowSalarySwitchStateUseCase(appRepository: AppRepository) =
        SaveSalarySwitchStateUseCase(appRepository)

    @Provides
    fun providesGetHistoryUseCase(appRepository: AppRepository) =
        GetHistoryUseCase(appRepository)

    @Provides
    fun providesChangePasswordUseCase(appRepository: AppRepository) =
        ChangePasswordUseCase(appRepository)

    @Provides
    fun providesGetUserDataFromRemoteUseCase(appRepository: AppRepository) =
        GetUserDataFromRemoteUseCase(appRepository)

    @Provides
    fun providesClearUserSessionUseCase(appRepository: AppRepository) =
        ClearUserSessionUseCase(appRepository)

    @Provides
    fun providesTermsAndConditionsUseCase(appRepository: AppRepository) =
        GetTermsAndConditionsUseCase(appRepository)

    @Provides
    fun providesSalaryAdvanceFormatUseCase(appRepository: AppRepository) =
        GetSalaryAdvanceFormatUseCase(appRepository)

    @Provides
    fun saveBankAccountUseCase(appRepository: AppRepository) =
        SaveBankAccountUseCase(appRepository)

    @Provides
    fun getBanksUseCase(appRepository: AppRepository) =
        GetBanksUseCase(appRepository)

    @Provides
    fun saveProfileUseCase(appRepository: AppRepository) =
        SaveProfileUseCase(appRepository)

    @Provides
    fun getMaritalStatusUseCase(appRepository: AppRepository) =
        GetMaritalStatusUseCase(appRepository)
}