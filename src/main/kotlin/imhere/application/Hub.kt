package imhere.application

import CheckIn
import CheckOut
import errorhandling.*
import imhere.domain.UserId
import imhere.domain.acl.Storage

class Hub(
    private val storage: Storage
) {
    fun checkIn(user: UserId): Result<CheckIn, ErrorCode> = storage.findByUser(user).andThen {
        val timetable = it.checkIn()

        Result.of(timetable.checkIn!!)
    }

    fun checkOut(user: UserId): Result<CheckOut, ErrorCode> = storage.findByUser(user).andThen {
        it.checkOut().map { it.checkOut!! }
    }
}