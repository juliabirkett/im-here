package imhere.web

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import errorhandling.map
import errorhandling.orElse
import imhere.application.Hub
import imhere.domain.AttemptToCheckOut
import imhere.domain.UserId
import imhere.domain.acl.UserNotFound
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
        "check-in" bind POST to { handleCheckIn(jacksonObjectMapper().readTree(it.bodyString()).at("/user").textValue().toUserId()) },
        "check-out" bind POST to { handleCheckout(jacksonObjectMapper().readTree(it.bodyString()).at("/user").textValue().toUserId()) }
    )

    override fun invoke(request: Request): Response = app(request)

    fun handleCheckout(user: UserId): Response = hub.checkOut(user).map {
        Response(Status.CREATED)
    }.orElse {
        when (it) {
            is UserNotFound -> Response(Status.NOT_FOUND)
            is AttemptToCheckOut -> Response(Status.UNPROCESSABLE_ENTITY)
            else -> Response(Status.INTERNAL_SERVER_ERROR)
        }
    }

    fun handleCheckIn(user: UserId): Response = hub.checkIn(user).map {
        Response(Status.CREATED)
    }.orElse {
        Response(Status.NOT_FOUND)
    }
}

fun String.toUserId() = UserId(UUID.fromString(this))