package api.usecases.task.create

import api.domains.types.StoryPoint

data class TaskCreateInputData(
    val projectId: String,
    val projectCardId: String,
    val estimateStoryPoint: StoryPoint,
)
