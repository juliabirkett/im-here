package imhere.infra

import errorhandling.Result
import errorhandling.asFailure
import errorhandling.asSuccess
import imhere.domain.Timetable
import imhere.domain.UserId
import imhere.domain.acl.Storage
import imhere.domain.acl.UserNotFound
import java.util.*

class InMemoryStorage: Storage {
    private var timetableRepository: Map<UUID, Timetable> = emptyMap()

    override fun findByUser(userId: UserId): Result<Timetable, UserNotFound> =
        timetableRepository.values.find { it.userId == userId }?.asSuccess()
            ?: UserNotFound.asFailure()

    override fun save(entity: Timetable) { timetableRepository = timetableRepository.plus(UUID.randomUUID() to entity) }
}
