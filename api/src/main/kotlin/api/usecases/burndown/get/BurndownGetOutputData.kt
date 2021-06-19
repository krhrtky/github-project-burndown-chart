package api.usecases.burndown.get

import java.time.LocalDate

data class BurndownGetOutputData(
    val dateRange: List<LocalDate>,
    val ideal: List<Double>,
    val finished: List<Int>,
    val estimate: List<Int>,
    val result: List<Int>,
)
