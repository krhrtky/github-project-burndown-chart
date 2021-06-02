package api.usecases.task.update

import java.time.LocalDateTime

class TaskUpdateOutputData(
    val projectId: String,
    val projectCardId: String,
    val estimateStoryPoint: Int,
    val resultStoryPoint: Int?,
    val finishedAt: LocalDateTime?,
)
