package imhere.web

import imhere.application.Hub
import imhere.domain.Timetable
import imhere.domain.UserId
import imhere.domain.acl.Storage
import imhere.infra.InMemoryStorage
import imhere.web.Resources.Companion.checkIn
import imhere.web.Resources.Companion.checkOut
import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Request
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

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
            val request = Request(method = Method.POST, uri = checkIn)
                .body(jsonActionBody(emptyTimetable.userId))

            assertEquals(201, handler(request).status.code)
        }

        @Test
        fun `when checking out after check-in, returns 201`() {
            testingStorage.save(emptyTimetable.checkIn())
            val checkInRequest = Request(method = Method.POST, uri = checkIn)
                .body(jsonActionBody(emptyTimetable.userId))
            handler(checkInRequest)

            val request = Request(method = Method.POST, uri = checkOut)
                .body(jsonActionBody(emptyTimetable.userId))

            assertEquals(201, handler(request).status.code)
        }

        @Test
        fun `when checking out without checking in, returns 422`() {
            testingStorage.save(emptyTimetable)
            val request = Request(method = Method.POST, uri = checkOut)
                .body(jsonActionBody(emptyTimetable.userId))

            assertEquals(422, handler(request).status.code)
        }
    }

    @Nested
    inner class WhenUserDoesNotExist {
        @Test
        fun `when checking in, returns 404`() {
            val request = Request(method = Method.POST, uri = checkIn)
                .body(jsonActionBody(UserId()))

            assertEquals(404, handler(request).status.code)
        }

        @Test
        fun `when checking out, returns 404`() {
            val request = Request(method = Method.POST, uri = checkOut)
                .body(jsonActionBody(UserId()))

            assertEquals(404, handler(request).status.code)
        }
    }

    private fun jsonActionBody(userId: UserId): String =  """
        { "user": "${userId.raw}" }
    """
}
