package api.domains.models.task

import java.lang.IllegalStateException
import java.time.LocalDateTime

class TaskPokoBuilder : TaskNotification {
    private var taskId: TaskId? = null
    private var projectCardId: ProjectCardId? = null
    private var estimateStoryPoint: EstimateStoryPoint? = null
    private var resultStoryPoint: ResultStoryPoint? = null
    private var finishedAt: FinishedAt? = null

    fun build(): TaskData {
        if (this.taskId != null && this.projectCardId != null && this.estimateStoryPoint != null) {
            return TaskData(
                taskId = this.taskId!!.value,
                projectCardId = this.projectCardId!!.value,
                estimateStoryPoint = this.estimateStoryPoint!!.value,
                resultStoryPoint = this.resultStoryPoint?.value,
                finishedAt = this.finishedAt?.value,
            )
        }

        throw IllegalStateException()
    }

    override fun setTaskId(taskId: TaskId) {
        this.taskId = taskId
    }

    override fun setProjectCardId(projectCardId: ProjectCardId) {
        this.projectCardId = projectCardId
    }

    override fun setEstimateStoryPoint(estimateStoryPoint: EstimateStoryPoint) {
        this.estimateStoryPoint = estimateStoryPoint
    }

    override fun setResultStoryPoint(resultStoryPoint: ResultStoryPoint?) {
        this.resultStoryPoint = resultStoryPoint
    }

    override fun setFinishedAt(finishedAt: FinishedAt?) {
        this.finishedAt = finishedAt
    }
}

data class TaskData(
    val taskId: String,
    val projectCardId: Int,
    val estimateStoryPoint: Int,
    val resultStoryPoint: Int?,
    val finishedAt: LocalDateTime?,
)
