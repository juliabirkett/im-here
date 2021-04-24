package imhere.domain

import CheckIn
import CheckOut
import errorhandling.ErrorCode
import errorhandling.Result
import errorhandling.asFailure
import errorhandling.asSuccess
import java.time.Instant

data class Timetable(
    val userId: UserId,
    val checkIn: CheckIn? = null,
    val checkOut: CheckOut? = null,
) {
    fun checkIn(): Timetable = copy(checkIn = CheckIn(at = Instant.now()))
    fun checkOut(): Result<Timetable, ErrorCode> = checkIn?.let {
        copy(checkOut = CheckOut(at = Instant.now())).asSuccess()
    } ?: AttemptToCheckOut.asFailure()
}

object AttemptToCheckOut : ErrorCode