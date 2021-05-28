package imhere.infra

import CheckIn
import CheckOut
import com.fasterxml.jackson.databind.JsonNode
import errorhandling.Result
import errorhandling.asFailure
import errorhandling.asSuccess
import imhere.domain.Timetable
import imhere.domain.UserId
import imhere.domain.acl.Storage
import imhere.domain.acl.UserNotFound
import org.jooq.*
import org.jooq.impl.DSL.field
import org.jooq.impl.DSL.table
import org.jooq.impl.SQLDataType
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatter.ISO_INSTANT
import java.util.*
import java.util.Date.from

class PGStorage(
    val context: DSLContext
): Storage {
    override fun findByUser(userId: UserId): Result<Timetable, UserNotFound> {
        val query = context.select()
            .from("timetable")
            .where(field("user_id").equal(userId.id))
            .fetchOne()
            ?.map(TimetableMapper())

        query?.let {
            return it.asSuccess()
        } ?: return UserNotFound.asFailure()
    }

    override fun save(entity: Timetable) {
        context.insertInto(
            table("timetable"),
            field("id"),
            field("user_id"),
            field("checked_in_at"),
            field("checked_out_at")
        ).values(
            UUID.randomUUID(),
            entity.userId.id,
            entity.checkIn?.at.toString(),
            entity.checkOut?.at.toString()
        ).execute()
    }
}

class TimetableMapper : RecordMapper<Record, Timetable> {
    override fun map(record: Record): Timetable = Timetable(
        userId = UserId(id = record.getValue("user_id", UUID::class.java)),
        checkIn = CheckIn(at = (record.getValue("checked_in_at") as String).toInstant()),
        checkOut = CheckOut(at = (record.getValue("checked_out_at") as String).toInstant())
    )
}

fun String.toInstant(formatter: DateTimeFormatter = ISO_INSTANT): Instant = formatter.parse(this, Instant::from)