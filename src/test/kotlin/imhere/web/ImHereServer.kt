package imhere.web

import imhere.application.Hub
import org.http4k.server.Http4kServer
import org.http4k.server.Netty
import org.http4k.server.asServer

object ImHereServer {
    val handler = ImHereHttpHandler(
        Hub(
            InMemoryStorage()
        )
    )

    operator fun invoke(): Http4kServer = handler.asServer(Netty())
}