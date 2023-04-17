package pe.com.onecanal.data.network.endpoints

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import pe.com.onecanal.data.config.DataStoreConfig.ARGS_SESSION_USER
import pe.com.onecanal.data.config.DataStoreConfig.ARGS_SHOW_SALARY_STATE
import pe.com.onecanal.data.util.NetworkUtils
import pe.com.onecanal.domain.entity.*

/**
 * Created by Sergio Carrillo Diestra on 19/01/2022.
 * scarrillo.peruapps@gmail.com
 * Peru Apps
 * Huacho, Peru.
 *
 **/
class LocalDataSourceImpl(
    private val dataStore: DataStore<Preferences>,
    private val networkUtils: NetworkUtils
) : LocalDataSource {
    override suspend fun getSalary(): Either<Failure, Salary> {
        return networkUtils.callFromLocalDataSource {
            Salary(
                Gson().fromJson(
                    getString(ARGS_SESSION_USER),
                    UserProfileData::class.java
                ).salary,
                Gson().fromJson(
                    getString(ARGS_SESSION_USER),
                    UserProfileData::class.java
                ).available_salary
            )
        }
    }

    override suspend fun saveUserSession(userProfileData: UserProfileData): Either<Failure, Unit> {
        return networkUtils.callFromLocalDataSource {
            dataStore.edit {
                it[stringPreferencesKey(ARGS_SESSION_USER)] =
                    Gson().toJson(userProfileData)
            }
        }
    }

    override suspend fun getUserSession(): Either<Failure, UserProfileData?> {
        return networkUtils.callFromLocalDataSource {
            Gson().fromJson(
                getString(ARGS_SESSION_USER),
                UserProfileData::class.java
            )
        }
    }

    override suspend fun getBankAccountsFromSession(): Either<Failure, List<BankAccount>> {
        return networkUtils.callFromLocalDataSource {
            Gson().fromJson(
                getString(ARGS_SESSION_USER),
                UserProfileData::class.java
            ).accounts
        }
    }

    override fun getSalarySwitchState(): Boolean = runBlocking {
        dataStore.data.first()[booleanPreferencesKey(ARGS_SHOW_SALARY_STATE)] ?: false
    }

    override suspend fun saveSalarySwitchState(state: Boolean): Either<Failure, Unit> {
        return networkUtils.callFromLocalDataSource {
            dataStore.edit {
                it[booleanPreferencesKey(ARGS_SHOW_SALARY_STATE)] = state
            }
        }
    }

    override suspend fun clearUserSession(): Either<Failure, Any> {
        return networkUtils.callFromLocalDataSource {
            dataStore.edit {
                it.remove(stringPreferencesKey(ARGS_SESSION_USER))
            }
        }
    }

    override suspend fun getToken(): String {
        return try {
            val userSession = Gson().fromJson(
                getString(ARGS_SESSION_USER),
                UserProfileData::class.java
            )
            return userSession.userToken
        } catch (e: Exception) {
            ""
        }
    }

    private suspend fun getString(key: String): String? {
        return dataStore.data.first()[stringPreferencesKey(key)]
    }


}