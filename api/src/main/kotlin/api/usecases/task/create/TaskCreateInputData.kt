package api.usecases.task.create

data class TaskCreateInputData(
    val projectId: String,
    val projectCardId: String,
    val estimateStoryPoint: Int,
)
