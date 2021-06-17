package api.domains.models.task

import api.domains.models.task.TaskId as TaskId

interface TaskNotification {
    fun setTaskId(taskId: TaskId)
    fun setProjectCardId(projectCardId: ProjectCardId)
    fun setEstimateStoryPoint(estimateStoryPoint: EstimateStoryPoint)
    fun setAddedAt(addedAt: AddedAt)
    fun setResultStoryPoint(resultStoryPoint: ResultStoryPoint?)
    fun setFinishedAt(finishedAt: FinishedAt?)
}
