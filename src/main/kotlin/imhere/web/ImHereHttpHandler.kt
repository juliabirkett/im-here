package imhere.web

import errorhandling.map
import errorhandling.orElse
import imhere.application.Hub
import org.http4k.core.HttpHandler
import org.http4k.core.Method.POST
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.routing.bind
import org.http4k.routing.routes

class ImHereHttpHandler(
    private val hub: Hub
): HttpHandler {
    val app = routes(
        "check-in" bind POST to { handleCheckIn() },
        "check-out" bind POST to { handleCheckout() }
    )

    override fun invoke(request: Request): Response = app(request)

    fun handleCheckout(): Response = hub.checkOut().map {
        Response(Status.CREATED)
    }.orElse {
        Response(Status.UNPROCESSABLE_ENTITY)
    }

    fun handleCheckIn(): Response = hub.checkIn().map {
        Response(Status.CREATED)
    }.orElse {
        Response(Status.NOT_FOUND)
    }
}