package imhere.infra

import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.postgresql.ds.common.BaseDataSource
import java.sql.Connection

data class DatabaseConfig(
    val host: String,
    val port: Int,
    val user: String,
    val password: String,
    val databaseName: String
) {
    fun toConnection(source: BaseDataSource): Connection {
        source.serverName = host
        source.portNumber = port
        source.user = user
        source.password = password
        source.databaseName = databaseName

        return source.connection
    }
}

fun Connection.dsl(): DSLContext = DSL.using(this, SQLDialect.POSTGRES)
