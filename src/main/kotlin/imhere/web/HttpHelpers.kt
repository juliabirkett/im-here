package imhere.web

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import errorhandling.ErrorCode
import imhere.domain.AttemptToCheckOut
import imhere.domain.acl.UserNotFound
import org.http4k.core.Request
import org.http4k.core.Status

fun Request.jsonToText(at: String) = jacksonObjectMapper().readTree(bodyString()).at(at).textValue()

fun ErrorCode.toHttpStatus(): Status = when (this) {
    is UserNotFound -> Status.NOT_FOUND
    is AttemptToCheckOut -> Status.UNPROCESSABLE_ENTITY
    else -> Status.INTERNAL_SERVER_ERROR
}
