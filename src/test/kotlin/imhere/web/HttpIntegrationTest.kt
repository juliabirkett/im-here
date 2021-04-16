package imhere.web

import imhere.application.Hub
import imhere.domain.Timetable
import imhere.domain.UserId
import imhere.domain.acl.Storage
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
        private val existingUser = UserId()

        @Test
        fun `when checking in successfully, returns 201`() {
            testingStorage.save(existingUser)
            val request = Request(method = Method.POST, uri = "/check-in")
                .body(jsonActionBody(existingUser))

            assertEquals(201, handler(request).status.code)
        }

        @Test
        fun `when checking out after check-in, returns 201`() {
            testingStorage.save(existingUser)
            val checkInRequest = Request(method = Method.POST, uri = "/check-in")
                .body(jsonActionBody(existingUser))
            handler(checkInRequest)

            val request = Request(method = Method.POST, uri = "/check-out")
                .body(jsonActionBody(existingUser))

            assertEquals(201, handler(request).status.code)
        }

        @Test
        fun `when checking out without checking in, returns 422`() {
            testingStorage.save(existingUser)
            val request = Request(method = Method.POST, uri = "/check-out")
                .body(jsonActionBody(existingUser))

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
    private var userRepository: Set<UserId> = emptySet()
    private var timetableRepository: Map<UUID, Timetable> = emptyMap()

    override fun find(userId: UserId): UserId? = userRepository.find { it == userId }
    override fun save(entity: UserId) { userRepository = userRepository + entity }
    override fun save(entity: Timetable) { timetableRepository = timetableRepository.plus(UUID.randomUUID() to entity) }
}