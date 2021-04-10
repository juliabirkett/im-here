package imhere.web

import org.http4k.core.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class HttpIntegrationTest {
    private val handler = object : HttpHandler {
        override fun invoke(p1: Request): Response {
            return Response(status = Status.CREATED)
        }
    }
    @Test
    fun `when checking in successfully, returns 201`() {
        val request = Request(method = Method.POST, uri = "/check-in")

        assertEquals(201, handler(request).status.code)
    }
}