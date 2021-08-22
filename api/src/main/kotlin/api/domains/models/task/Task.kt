package api.domains.models.task

import java.time.LocalDate

data class Task constructor(
    private val taskId: TaskId,
    private val projectCardId: ProjectCardId,
    private val estimateStoryPoint: EstimateStoryPoint,
    private val addedAt: AddedAt,
    private val resultStoryPoint: ResultStoryPoint? = null,
    private val finishedAt: FinishedAt? = null,
) {
    init {
        if((resultStoryPoint != null && finishedAt == null) ||
            (resultStoryPoint == null && finishedAt != null)) {
            throw IllegalArgumentException(
                "${this.javaClass.simpleName} must be both resultStoryPoint and finishedAt are null or nonnull."
            )
        }
    }

    companion object {
        fun create(
            taskId: TaskId,
            projectCardId: ProjectCardId,
            estimateStoryPoint: EstimateStoryPoint,
            addedAt: AddedAt,
        ): Task = Task(
            taskId = taskId,
            projectCardId = projectCardId,
            estimateStoryPoint = estimateStoryPoint,
            addedAt = addedAt,
        )
    }

    fun done(
        resultStoryPoint: ResultStoryPoint,
        finishedAt: FinishedAt,
    ): Task = Task(
        taskId = this.taskId,
        projectCardId = this.projectCardId,
        estimateStoryPoint = this.estimateStoryPoint,
        addedAt = this.addedAt,
        resultStoryPoint = resultStoryPoint,
        finishedAt = finishedAt,
    )

    fun isCountableDate(date: LocalDate) = addedAt.value.toLocalDate().isEqual(date) ||
            addedAt.value.toLocalDate().isBefore(date)

    fun isDigestibleDate(date: LocalDate) = addedAt.value.toLocalDate().isBefore(date)

    fun isAddedAt(date: LocalDate) = addedAt.value.toLocalDate().isEqual(date) ||
            addedAt.value.toLocalDate().isBefore(date)

    fun isExistsWithinPeriod(from: LocalDate, to: LocalDate) = (
            addedAt.value.toLocalDate().isEqual(from) || addedAt.value.toLocalDate().isBefore(from)
            ) && (
            addedAt.value.toLocalDate().isEqual(to) || addedAt.value.toLocalDate().isAfter(to)
            )

    fun isFinished(): Boolean = resultStoryPoint != null && finishedAt != null
    fun isFinishedAt(date: LocalDate): Boolean {
        val finishedAt = finishedAt?.value?.toLocalDate() ?: return false
        return finishedAt.isEqual(date) || finishedAt.isBefore(date)
    }

    fun isSameProjectCard(other: Task): Boolean = this.projectCardId == other.projectCardId

    fun notify(notification: TaskNotification) {
        notification.setTaskId(this.taskId)
        notification.setProjectCardId(this.projectCardId)
        notification.setEstimateStoryPoint(this.estimateStoryPoint)
        notification.setAddedAt(this.addedAt)
        notification.setResultStoryPoint(this.resultStoryPoint)
        notification.setFinishedAt(this.finishedAt)
    }
}
