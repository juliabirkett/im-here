package imhere.application

import CheckIn
import CheckOut
import errorhandling.ErrorCode
import errorhandling.Result
import errorhandling.asFailure
import errorhandling.orElse
import imhere.domain.Timetable
import imhere.domain.UserId
import imhere.domain.acl.Storage

class Hub(
    private val storage: Storage
) {
    fun checkIn(user: UserId): Result<CheckIn, ErrorCode> = storage.find(user)?.let {
        Result.of(Timetable(userId = it).checkIn())
    } ?: UserNotFound().asFailure()

    fun checkOut(): Result<CheckOut, ErrorCode> = throw NotImplementedError()
}

class UserNotFound : ErrorCode