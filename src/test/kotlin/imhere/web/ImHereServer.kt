package imhere.web

import imhere.application.Hub
import imhere.infra.PGStorage
import org.http4k.server.Http4kServer
import org.http4k.server.Netty
import org.http4k.server.asServer
import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.postgresql.ds.PGConnectionPoolDataSource
import java.sql.Connection

object ImHereServer {
    val handler = ImHereHttpHandler(
        Hub(
            PGStorage(connection().dsl())
        )
    )

    operator fun invoke(): Http4kServer = handler.asServer(Netty())

    fun connection(): Connection {
        val connection = PGConnectionPoolDataSource()
        connection.serverName = "imhere-postgresql.local.birketta.io"
        connection.portNumber = 5433
        connection.user = "birketta"
        connection.password = "birketta123!"
        connection.databaseName = "imhere"

        return connection.connection
    }

    fun Connection.dsl(): DSLContext = DSL.using(this, SQLDialect.POSTGRES)
}