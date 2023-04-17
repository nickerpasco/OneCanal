package pe.com.onecanal.domain.entity

sealed class Failure {

    /** When service return 401 or 403 this will force the client to log out of the app.*/
    open class UnauthorizedOrForbidden(val errorMessage:String?,val code:Int?) : Failure()

    /** Weird and strange error that we don´t know the cause.*/
    open class None(val code:Int?) :Failure()

    /** When suddenly the connection is lost.*/
    open class NetworkConnectionLostSuddenly(val code:Int?) : Failure()

    /** When there is no internet network detected.*/
    open class NoNetworkDetected(val code:Int?) : Failure()

    open class SSLError(val code:Int?): Failure()

    /** When service is taking to long on return the response.*/
    open class TimeOut(val code:Int?): Failure()

    /** Extend this class for feature specific failures.*/
    open class ServiceUncaughtFailure(val uncaughtFailureMessage: String,val code:Int?) : Failure()

    /** Extend this class for feature specific SERVICE ERROR BODY RESPONSE.*/
    open class ServerBodyError(val code: Int?,val message : String) : Failure()

    /** Extend this class for feature specific DATA -> DOMAIN MAPPERS exceptions.*/
    open class DataToDomainMapperFailure(val mapperException: String?) : Failure()

    /** Extend this class for local datasource error.*/
    object ReadFromLocalDatasourceError :Failure()

}