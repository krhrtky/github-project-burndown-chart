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
        val taskId = this.taskId
        val projectCardId = this.projectCardId
        val estimateStoryPoint = this.estimateStoryPoint
        val resultStoryPoint = this.resultStoryPoint
        val finishedAt = this.finishedAt

        if (taskId != null && projectCardId != null && estimateStoryPoint != null) {
            return TaskData(
                taskId = taskId.value,
                projectCardId = projectCardId.value,
                estimateStoryPoint = estimateStoryPoint.value,
                resultStoryPoint = resultStoryPoint?.value,
                finishedAt = finishedAt?.value,
            )
        }

        throw IllegalStateException()
    }

    fun extract(task: Task): TaskPokoBuilder {
        task.notify(this)
        return this
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
    val projectCardId: String,
    val estimateStoryPoint: Int,
    val resultStoryPoint: Int?,
    val finishedAt: LocalDateTime?,
)
