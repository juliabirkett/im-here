package imhere.domain

import CheckIn
import CheckOut
import java.time.Instant

data class Timetable(val userId: UserId) {
    private lateinit var checkIn: CheckIn
    private lateinit var checkOut: CheckOut

    fun checkIn(): CheckIn {
        checkIn = CheckIn(at = Instant.now())

        return checkIn
    }
    fun checkOut(): CheckOut {
        checkOut = CheckOut(at = Instant.now())

        return checkOut
    }
}