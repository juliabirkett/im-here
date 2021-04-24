package imhere.domain.acl

import errorhandling.ErrorCode
import errorhandling.Result
import imhere.domain.Timetable
import imhere.domain.UserId

interface Storage {
    fun findByUser(userId: UserId): Result<Timetable, UserNotFound>
    fun save(entity: Timetable)
}

object UserNotFound : ErrorCode
