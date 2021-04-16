package imhere.application

import CheckIn
import CheckOut
import errorhandling.ErrorCode
import errorhandling.Result
import errorhandling.asFailure
import errorhandling.asSuccess
import imhere.domain.Timetable
import imhere.domain.UserId
import imhere.domain.acl.Storage

class Hub(
    private val storage: Storage
) {
    fun checkIn(user: UserId): Result<CheckIn, ErrorCode> = storage.find(user)?.let {
        Timetable(userId = it).checkIn().asSuccess()
    } ?: UserNotFound().asFailure()

    fun checkOut(user: UserId): Result<CheckOut, ErrorCode> = storage.find(user)?.let {
        Timetable(userId = it).checkOut().asSuccess()
    } ?: UserNotFound().asFailure()
}

class UserNotFound : ErrorCode