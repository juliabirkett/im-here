package imhere.web

import imhere.application.Hub
import imhere.infra.Environment
import imhere.infra.Environment.Env.Local
import imhere.infra.PGStorage
import imhere.infra.dsl
import org.http4k.server.Http4kServer
import org.http4k.server.Netty
import org.http4k.server.asServer
import org.postgresql.ds.PGConnectionPoolDataSource

object ImHereServer {
    // TODO: receive from env var
    private val env = Environment(Local)

    val storage = PGStorage(
        env.databaseConfig
            .toConnection(PGConnectionPoolDataSource())
            .dsl()
    )

    val handler = ImHereHttpHandler(
        Hub(storage)
    )

    operator fun invoke(): Http4kServer = handler.asServer(Netty())
}