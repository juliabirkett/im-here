package imhere.web

import errorhandling.map
import errorhandling.orElse
import imhere.application.Hub
import imhere.domain.UserId
import org.http4k.core.HttpHandler
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.routing.bind
import org.http4k.routing.routes
import java.util.*

class ImHereHttpHandler(
    private val hub: Hub
): HttpHandler {
    val app = routes(
        "check-in" bind POST to { handleCheckIn(it.jsonToText("/user").toUserId()) },
        "check-out" bind POST to { handleCheckout(it.jsonToText("/user").toUserId()) }
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

fun String.toUserId() = UserId(UUID.fromString(this))