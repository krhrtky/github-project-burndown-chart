package api.usecases.task.finish

import java.time.LocalDateTime

data class TaskFinishInputData(
    val taskId: String,
    val resultStoryPoint: Int,
    val finishedAt: LocalDateTime,
)
