package api.usecases.task.update

import java.time.LocalDateTime

class TaskUpdateOutputData(
    val projectId: String,
    val projectCardId: Int,
    val estimateStoryPoint: Int,
    val resultStoryPoint: Int?,
    val finishedAt: LocalDateTime?,
)
