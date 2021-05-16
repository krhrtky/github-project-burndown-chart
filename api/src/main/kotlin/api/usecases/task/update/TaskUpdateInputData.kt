package api.usecases.task.update

import java.time.LocalDateTime

data class TaskUpdateInputData(
    val taskId: String,
    val estimateStoryPoint: Int? = null,
    val resultStoryPoint: Int? = null,
    val finishedAt: LocalDateTime? = null,
)
