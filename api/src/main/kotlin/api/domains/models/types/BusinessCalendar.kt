package api.domains.models.types

import com.samuraism.bc4j.BusinessCalendar as Calender
import com.samuraism.bc4j.Japan

class BusinessCalendar {
    companion object {
        val default = Calender
            .newBuilder()
            .holiday(Calender.CLOSED_ON_SATURDAYS_AND_SUNDAYS)
            .holiday(Japan.PUBLIC_HOLIDAYS)
            .build()
    }
}
