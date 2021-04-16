package errorhandling

import errorhandling.Result.Companion.success

sealed class Result<out T, out E> {
    companion object {
        fun <T> success(value: T): Result<T, Nothing> = Ok(value)
        fun <T> of(value: T): Result<T, Nothing> = Ok(value)
        fun <E> failure(err: E): Result<Nothing, E> = Err(err)
    }
}
class Ok<out T>(val value: T): Result<T, Nothing>()
class Err<out E>(val value: E): Result<Nothing, E>()

fun <U, T, E> Result<T, E>.map(transform: (T) -> U): Result<U, E> = when (this) {
    is Ok -> Ok(transform(value))
    is Err -> this
}

fun <U, T, E> Result<T, E>.mapError(transform: (E) -> U): Result<T, U> = when (this) {
    is Ok -> this
    is Err -> Err(transform(value))
}

fun <U, T, E> Result<T, E>.andThen(transform: (T) -> Result<U, E>): Result<U, E> = when (this) {
    is Ok -> transform(value)
    is Err -> this
}

fun <T> T.asSuccess(): Result<T, Nothing> = success(this)

inline fun <T, E> Result<T, E>.orElse(f: (E) -> T): T =
    when (this) {
        is Err -> f(value)
        is Ok<T> -> value
    }