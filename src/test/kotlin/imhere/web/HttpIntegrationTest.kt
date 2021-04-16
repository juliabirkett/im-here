package imhere.web

import errorhandling.Result
import imhere.application.Hub
import io.mockk.every
import io.mockk.mockk
import org.http4k.core.Method
import org.http4k.core.Request
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class HttpIntegrationTest {
    private val hub = mockk<Hub>()
    private val handler = imhere.web.ImHereHttpHandler(hub)

    @Test
    fun `when checking in successfully, returns 201`() {
        val request = Request(method = Method.POST, uri = "/check-in")

        assertEquals(201, handler(request).status.code)
    }

    @Test
        fun `when checking out after check-in, returns 201`() {
        every { hub.checkOut() } returns Result.of(mockk())
        val checkInRequest = Request(method = Method.POST, uri = "/check-in")
        handler(checkInRequest)
        val request = Request(method = Method.POST, uri = "/check-out")

        assertEquals(201, handler(request).status.code)
    }

    @Test
    fun `when checking out without checking in, returns 422`() {
        every { hub.checkOut() } returns Result.failure(Exception("Something bad happened there!"))
        val request = Request(method = Method.POST, uri = "/check-out")

        assertEquals(422, handler(request).status.code)
    }
}