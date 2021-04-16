package imhere.application

import CheckOut
import errorhandling.ErrorCode
import errorhandling.Result

interface Hub {
    fun checkOut(): Result<CheckOut, ErrorCode>
}