package imhere.functional

import imhere.web.ImHereServer
import imhere.web.Resources.Companion.healthCheck
import org.http4k.core.*
import org.http4k.filter.ClientFilters
import org.http4k.server.Netty
import org.http4k.server.asServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class HttpApplicationTest {
    private val handler = ImHereServer.handler
    private val server = handler.asServer(Netty())
    private val app: HttpHandler = ClientFilters.SetBaseUriFrom(Uri.of("http://localhost:${server.port()}")).then(handler)

    @BeforeEach
    fun setup() {
        println("Starting app...")
        server.start()
    }

    @AfterEach
    fun teardown() {
        server.stop()
        println("App finished!")
    }

    @Test
    fun `checks if the application can start with all real dependencies`() {
        assertTrue(app(Request(Method.GET, healthCheck)).status.successful)
    }
}