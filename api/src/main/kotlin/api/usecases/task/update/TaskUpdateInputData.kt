package api.usecases.task.update

import api.domains.types.StoryPoint
import java.time.LocalDateTime

data class TaskUpdateInputData(
    val taskId: String,
    val estimateStoryPoint: StoryPoint? = null,
    val resultStoryPoint: StoryPoint? = null,
    val finishedAt: LocalDateTime? = null,
)
