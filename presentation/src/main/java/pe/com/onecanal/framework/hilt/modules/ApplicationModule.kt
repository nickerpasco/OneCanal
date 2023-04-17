package pe.com.onecanal.framework.hilt.modules

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import pe.com.onecanal.BuildConfig
import pe.com.onecanal.data.mapper.ResponseMapper
import pe.com.onecanal.data.mapper.ResponseMapperImpl
import pe.com.onecanal.data.network.endpoints.*
import pe.com.onecanal.data.repository.AppRepositoryImpl
import pe.com.onecanal.data.util.ConnectionUtils
import pe.com.onecanal.data.util.ConnectionUtilsImpl
import pe.com.onecanal.data.util.NetworkUtils
import pe.com.onecanal.data.util.Settings
import pe.com.onecanal.domain.repository.AppRepository
import pe.com.onecanal.framework.hilt.RequestInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {
    private val Context.dataStore by preferencesDataStore(name = Settings.DATA_STORE)

    @Singleton
    @Provides
    fun providesInterceptor(dataSource: LocalDataSource): RequestInterceptor =
        RequestInterceptor(dataSource)

    @Singleton
    @Provides
    fun providesRetrofit(requestInterceptor: RequestInterceptor): Retrofit {
        val httpClient = OkHttpClient.Builder()


        val logging = HttpLoggingInterceptor()
        logging.level = if(BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        httpClient.addInterceptor(logging)
//            .connectTimeout(90, TimeUnit.SECONDS)
//            .readTimeout(90, TimeUnit.SECONDS)
        //        val myRequestInterceptor = RequestInterceptor()
        httpClient.addInterceptor(requestInterceptor)
        val gson = GsonBuilder()
            .registerTypeAdapter(Double::class.java, DoubleGsonAdapter())
            .create()

        return Retrofit.Builder()
            .client(httpClient.build())
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    fun providesAccountValidationRepository(
        remoteDataSource: RemoteDataSource,
        localDataSource: LocalDataSource,
        responseMapper: ResponseMapper
    ): AppRepository =
        AppRepositoryImpl(remoteDataSource, localDataSource, responseMapper)

    @Singleton
    @Provides
    fun providesRemoteAPI(retrofit: Retrofit): RemoteAPI =
        retrofit.create(RemoteAPI::class.java)


    @Singleton
    @Provides
    fun providesConnectionUtils(@ApplicationContext appContext: Context): ConnectionUtils =
        ConnectionUtilsImpl(appContext)

    @Singleton
    @Provides
    fun providesNetworkUtils(connectionUtils: ConnectionUtils): NetworkUtils =
        NetworkUtils(connectionUtils)

    @Singleton
    @Provides
    fun providesRemoteDataSource(
        remoteAPI: RemoteAPI,
        networkUtils: NetworkUtils
    ): RemoteDataSource = RemoteDataSourceImpl(remoteAPI, networkUtils)

    @Singleton
    @Provides
    fun providesRLocalDataSource(
        @ApplicationContext appContext: Context,
        networkUtils: NetworkUtils
    ): LocalDataSource = LocalDataSourceImpl(appContext.dataStore, networkUtils)


    @Singleton
    @Provides
    fun providesResponseMapper(): ResponseMapper = ResponseMapperImpl()


}