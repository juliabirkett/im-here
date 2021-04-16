package imhere.application

import CheckIn
import CheckOut
import errorhandling.ErrorCode
import errorhandling.Result

interface Hub {
    fun checkOut(): Result<CheckOut, ErrorCode>
    fun checkIn(): Result<CheckIn, ErrorCode>
}