package imhere.infra

data class Environment(private val name: Env) {
    val databaseConfig: DatabaseConfig by lazy {
        when (name) {
            Env.Local -> DatabaseConfig(
                host = "imhere-postgresql.local.birketta.io",
                port = 5433,
                user = "birketta",
                password = "birketta123!",
                databaseName = "imhere"
            )
        }
    }

    enum class Env {
        Local
    }
}