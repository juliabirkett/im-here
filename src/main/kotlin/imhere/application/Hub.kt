package imhere.application

import CheckOut
import errorhandling.Result

interface Hub {
    fun checkOut(): Result<CheckOut, Throwable>
}