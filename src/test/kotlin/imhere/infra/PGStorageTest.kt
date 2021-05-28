package imhere.infra

import errorhandling.success
import imhere.domain.Timetable
import imhere.domain.UserId
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.postgresql.ds.PGConnectionPoolDataSource

class PGStorageTest {
    private val env = Environment(Environment.Env.Test)

    private val storage = PGStorage(
        env.databaseConfig
            .toConnection(PGConnectionPoolDataSource())
            .dsl()
    )

    @AfterEach
    fun teardown() {
        storage.teardown()
    }

    @Test
    fun `saves a timetable and finds it by user`() {
        val user = UserId()
        val timetable = Timetable(user).checkIn().checkOut().success()!!

        storage.save(timetable)

        val foundTimetable = storage.findByUser(userId = user).success()!!
        assertEquals(timetable, foundTimetable)
    }

    private fun PGStorage.teardown() {
        this.context.truncate("timetable").execute()
    }
}