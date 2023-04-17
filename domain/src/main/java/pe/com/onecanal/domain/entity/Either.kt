package pe.com.onecanal.domain.entity

/**
 * Represents a value of one of two possible types (a disjoint union).
 * Instances of [Either] are either an instance of [Error] or [Success].
 * FP Convention dictates that [Error] is used for "failure"
 * and [Success] is used for "success".
 *
 * @see Error
 * @see Success
 *
 * https://danielwestheide.com/blog/the-neophytes-guide-to-scala-part-7-the-either-type/
 */
sealed class Either<out L, out R> {
    /** * Represents the left side of [Either] class which by convention is a "Error". */
    data class Error<out L>(val error: L) : Either<L, Nothing>()

    /** * Represents the right side of [Either] class which by convention is a "Success". */
    data class Success<out R>(val success: R) : Either<Nothing, R>()

    val isSuccess get() = this is Success<R>
    val isError get() = this is Error<L>

    fun <L> left(a: L) = Error(a)
    fun <R> right(b: R) = Success(b)

    fun either(fnL: (L) -> Unit, fnR: (R) -> Unit): Any =
        when (this) {
            is Error -> fnL(error)
            is Success -> fnR(success)
        }
}