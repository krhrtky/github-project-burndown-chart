package api.usecases.task.finish

import api.domains.types.StoryPoint
import java.time.LocalDateTime

data class TaskFinishInputData(
    val taskId: String,
    val resultStoryPoint: StoryPoint,
    val finishedAt: LocalDateTime,
)
