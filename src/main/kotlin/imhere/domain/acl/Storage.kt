package imhere.domain.acl

import imhere.domain.Timetable
import imhere.domain.UserId

interface Storage {
    fun find(userId: UserId): UserId?
    fun save(entity: UserId)
    fun save(entity: Timetable)
}