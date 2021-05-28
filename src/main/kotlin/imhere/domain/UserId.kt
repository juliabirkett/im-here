package imhere.domain

import java.util.UUID

data class UserId(val id: UUID = UUID.randomUUID()) {
    val raw: String = id.toString()
}
