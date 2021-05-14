package imhere.web

import errorhandling.map
import errorhandling.orElse
import imhere.application.Hub
import imhere.domain.UserId
import imhere.web.Resources.Companion.checkIn
import imhere.web.Resources.Companion.checkOut
import imhere.web.Resources.Companion.healthCheck
import org.http4k.core.*
import org.http4k.core.Method.POST
import org.http4k.routing.bind
import org.http4k.routing.routes
import java.util.*

class ImHereHttpHandler(
    private val hub: Hub
): HttpHandler {
    val app = routes(
        healthCheck bind Method.GET to { Response(Status.OK) },
        checkIn bind POST to { handleCheckIn(it.jsonToText("/user").toUserId()) },
        checkOut bind POST to { handleCheckout(it.jsonToText("/user").toUserId()) }
    )

    override fun invoke(request: Request): Response = app(request)

    fun handleCheckout(user: UserId): Response = hub.checkOut(user).map {
        Response(Status.CREATED)
    }.orElse { Response(it.toHttpStatus()) }

    fun handleCheckIn(user: UserId): Response = hub.checkIn(user).map {
        Response(Status.CREATED)
    }.orElse {
        Response(Status.NOT_FOUND)
    }
}

class Resources {
    companion object {
        const val healthCheck = "health"
        const val checkIn = "check-in"
        const val checkOut = "check-out"
    }
}

fun String.toUserId() = UserId(UUID.fromString(this))