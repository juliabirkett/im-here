package imhere.web

import imhere.application.Hub
import imhere.infra.DatabaseConfig
import imhere.infra.PGStorage
import imhere.infra.dsl
import org.http4k.server.Http4kServer
import org.http4k.server.Netty
import org.http4k.server.asServer
import org.postgresql.ds.PGConnectionPoolDataSource

object ImHereServer {
    val storage = PGStorage(
        DatabaseConfig(
            host = "imhere-postgresql.local.birketta.io",
            port = 5433,
            user = "birketta",
            password = "birketta123!",
            databaseName = "imhere"
        ).toConnection(
            PGConnectionPoolDataSource()
        ).dsl()
    )

    val handler = ImHereHttpHandler(
        Hub(storage)
    )

    operator fun invoke(): Http4kServer = handler.asServer(Netty())
}