package errorhandling

import errorhandling.Result.Companion.failure
import errorhandling.Result.Companion.success

sealed class Result<out T, out E> {
    companion object {
        fun <T> success(value: T): Result<T, Nothing> = Ok(value)
        fun <T> of(value: T): Result<T, Nothing> = Ok(value)
        fun <E : ErrorCode> failure(err: E): Result<Nothing, E> = Error(err)
    }
}
class Ok<out T>(val value: T): Result<T, Nothing>()
class Error<out E>(val value: E): Result<Nothing, E>()

fun <U, T, E: ErrorCode> Result<T, E>.map(transform: (T) -> U): Result<U, E> = when (this) {
    is Ok -> Ok(transform(value))
    is Error -> this
}

fun <U, T, E: ErrorCode> Result<T, E>.mapError(transform: (E) -> U): Result<T, U> = when (this) {
    is Ok -> this
    is Error -> Error(transform(value))
}

fun <U, T, E: ErrorCode> Result<T, E>.andThen(transform: (T) -> Result<U, E>): Result<U, E> = when (this) {
    is Ok -> transform(value)
    is Error -> this
}

fun <T> T.asSuccess(): Result<T, Nothing> = success(this)
fun <E : ErrorCode> E.asFailure(): Result<Nothing, E> = failure(this)

inline fun <T, E: ErrorCode> Result<T, E>.orElse(f: (E) -> T): T = when (this) {
    is Error -> f(value)
    is Ok<T> -> value
}

fun <T> Result<T, ErrorCode>.success(): T? = when (this) {
    is Ok -> value
    is Error -> null
}