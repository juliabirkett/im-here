package imhere.web

import errorhandling.Result
import errorhandling.asFailure
import errorhandling.asSuccess
import imhere.application.Hub
import imhere.domain.Timetable
import imhere.domain.UserId
import imhere.domain.acl.Storage
import imhere.domain.acl.UserNotFound
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Request
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.*

class HttpIntegrationTest {
    private val testingStorage: Storage = InMemoryStorage()
    private val hub = Hub(storage = testingStorage)
    private val handler: HttpHandler = ImHereHttpHandler(hub)

    @Nested
    inner class WhenUserExists {
        private val emptyTimetable = Timetable(userId = UserId())

        @Test
        fun `when checking in successfully, returns 201`() {
            testingStorage.save(emptyTimetable)
            val request = Request(method = Method.POST, uri = "/check-in")
                .body(jsonActionBody(emptyTimetable.userId))

            assertEquals(201, handler(request).status.code)
        }

        @Test
        fun `when checking out after check-in, returns 201`() {
            testingStorage.save(emptyTimetable.checkIn())
            val checkInRequest = Request(method = Method.POST, uri = "/check-in")
                .body(jsonActionBody(emptyTimetable.userId))
            handler(checkInRequest)

            val request = Request(method = Method.POST, uri = "/check-out")
                .body(jsonActionBody(emptyTimetable.userId))

            assertEquals(201, handler(request).status.code)
        }

        @Test
        fun `when checking out without checking in, returns 422`() {
            testingStorage.save(emptyTimetable)
            val request = Request(method = Method.POST, uri = "/check-out")
                .body(jsonActionBody(emptyTimetable.userId))

            assertEquals(422, handler(request).status.code)
        }
    }

    @Nested
    inner class WhenUserDoesNotExist {
        @Test
        fun `when checking in, returns 404`() {
            val request = Request(method = Method.POST, uri = "/check-in")
                .body(jsonActionBody(UserId()))

            assertEquals(404, handler(request).status.code)
        }

        @Test
        fun `when checking out, returns 404`() {
            val request = Request(method = Method.POST, uri = "/check-out")
                .body(jsonActionBody(UserId()))

            assertEquals(404, handler(request).status.code)
        }
    }

    private fun jsonActionBody(userId: UserId): String =  """
        { "user": "${userId.raw}" }
    """
}

class InMemoryStorage: Storage {
    private var timetableRepository: Map<UUID, Timetable> = emptyMap()

    override fun findByUser(userId: UserId): Result<Timetable, UserNotFound> =
        timetableRepository.values.find { it.userId == userId }?.asSuccess()
            ?: UserNotFound.asFailure()

    override fun save(entity: Timetable) { timetableRepository = timetableRepository.plus(UUID.randomUUID() to entity) }
}
