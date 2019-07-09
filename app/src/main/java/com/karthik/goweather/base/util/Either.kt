package com.karthik.goweather.base.util


/**
 * created by Karthik A on 2019-05-26
 *
 * reference https://medium.com/@lupajz/you-either-love-it-or-you-havent-used-it-yet-a55f9b866dbe
 */

/**
 * Represents a value of one of two possible types (a disjoint union).
 * Instances of [Either] are either an instance of [Error] or [Success].
 * FP Convention dictates that [Error] is used for "failure"
 * and [Success] is used for "success".
 *
 * @see Error
 * @see Success
 */
sealed class Either<out L, out R> {
    data class Error<out L>(val a: L) : Either<L, Nothing>()
    data class Success<out R>(val b: R) : Either<Nothing, R>()

    val isSuccess get() = this is Success<R>
    val isError get() = this is Error<L>

    fun <L> error(a: L) = Error(a)
    fun <R> success(b: R) = Success(b)

    fun either(fnL: (L) -> Any, fnR: (R) -> Any): Any =
        when (this) {
            is Error -> fnL(a)
            is Success -> fnR(b)
        }

}

// Credits to Alex Hart -> https://proandroiddev.com/kotlins-nothing-type-946de7d464fb
// Composes 2 functions
fun <A, B, C> ((A) -> B).c(f: (B) -> C): (A) -> C = {
    f(this(it))
}

fun <T, L, R> Either<L, R>.flatMap(fn: (R) -> Either<L, T>): Either<L, T> =
    when (this) {
        is Either.Error -> Either.Error(a)
        is Either.Success -> fn(b)
    }

fun <T, L, R> Either<L, R>.map(fn: (R) -> (T)): Either<L, T> = this.flatMap(fn.c(::success))
