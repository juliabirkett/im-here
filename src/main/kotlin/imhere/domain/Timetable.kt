package imhere.domain

import CheckIn

data class Timetable(val userId: UserId) {
    fun checkIn(): CheckIn = object : CheckIn {}
}