package api.domains.models.burndown

import api.domains.models.project.ProjectId
import api.domains.models.task.Task
import api.domains.models.task.TaskPokoBuilder
import com.samuraism.bc4j.BusinessCalendar
import com.samuraism.bc4j.Japan
import java.time.LocalDate

data class Burndown(
    private val projectId: ProjectId,
    private val taskList: List<Task>,
) {
    private val calendar = BusinessCalendar
        .newBuilder()
        .holiday(BusinessCalendar.CLOSED_ON_SATURDAYS_AND_SUNDAYS)
        .holiday(Japan.PUBLIC_HOLIDAYS)
        .build()

    fun chartFrom(from: LocalDate = LocalDate.now().minusMonths(1)): BurndownChart {
        val businessDays = (from..calendar.lastBusinessDay())
            .filter { calendar.isBusinessDay(it) }

        val ideal = businessDays
            .mapIndexed { index, day ->
                val totalPointAtThatTime = taskList
                    .filter { it.isAddedAt(day) }
                    .sumOf {
                        TaskPokoBuilder()
                            .extract(it)
                            .build().estimateStoryPoint
                    }
                if (index == 0)
                    totalPointAtThatTime.toDouble()
                else
                    totalPointAtThatTime - (totalPointAtThatTime.toDouble() / businessDays.size.toDouble()) * index
            }

        val finishedEstimatePointCharts = businessDays
            .map { day ->
                val totalPointAtThatTime = taskList
                    .filter { it.isAddedAt(day) }
                    .sumOf {
                        TaskPokoBuilder()
                            .extract(it)
                            .build().estimateStoryPoint
                    }
                totalPointAtThatTime - taskList
                    .filter { it.isFinishedAt(day) }
                    .sumOf {
                        TaskPokoBuilder()
                            .extract(it)
                            .build().estimateStoryPoint
                    }
            }

        val finishedEstimate = businessDays
            .map { day ->
                taskList
                    .filter { it.isFinishedAt(day) }
                    .sumOf {
                        TaskPokoBuilder()
                            .extract(it)
                            .build().estimateStoryPoint
                    }
            }

        val finishedResult = businessDays
            .map { day ->
                taskList
                    .filter { it.isFinishedAt(day) }
                    .mapNotNull {
                        TaskPokoBuilder()
                            .extract(it)
                            .build().resultStoryPoint
                    }
                    .sum()
            }

        return BurndownChart(
            dateRange = businessDays,
            idealDigestion = ideal,
            resultDigestion = finishedEstimatePointCharts,
            estimate = finishedEstimate,
            result = finishedResult,
        )
    }
}

data class BurndownChart(
    val dateRange: List<LocalDate>,
    val idealDigestion: List<Double>,
    val resultDigestion: List<Int>,
    val estimate: List<Int>,
    val result: List<Int>,
)
operator fun LocalDate.rangeTo(other: LocalDate): LocalDateRange {
    return LocalDateRange(this, other)
}

class LocalDateRange(override val start: LocalDate, override val endInclusive: LocalDate)
    : ClosedRange<LocalDate>, Iterable<LocalDate> {
    override fun iterator(): Iterator<LocalDate> = DateIterator(start, endInclusive)
}

class DateIterator(start: LocalDate, private val endInclusive: LocalDate)
    : Iterator<LocalDate> {

    private var current = start

    override fun hasNext(): Boolean = current.isBefore(endInclusive)

    override fun next(): LocalDate {
        val next = current

        current = current.plusDays(1)

        return next
    }
}
