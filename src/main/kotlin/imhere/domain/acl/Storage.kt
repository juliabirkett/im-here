package imhere.domain.acl

import imhere.domain.UserId

interface Storage {
    fun find(userId: UserId): UserId?
    fun save(userId: UserId)
}