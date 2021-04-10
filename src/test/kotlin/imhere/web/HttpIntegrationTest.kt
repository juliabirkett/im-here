package imhere.web

import org.http4k.core.HttpHandler
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class HttpIntegrationTest {
    private val handler = object : HttpHandler {
        override fun invoke(p1: Request): Response {
            TODO("Not yet implemented")
        }
    }
    @Test
    fun `when checking in successfully, returns 201`() {
        val request = Request(method = Method.POST, uri = "/check-in")

        assertEquals(201, handler(request).status.code)
    }
}