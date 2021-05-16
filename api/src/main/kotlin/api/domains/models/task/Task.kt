package api.domains.models.task

data class Task constructor(
    private val taskId: TaskId,
    private val projectCardId: ProjectCardId,
    private val estimateStoryPoint: EstimateStoryPoint,
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
        ): Task = Task(
            taskId = taskId,
            projectCardId = projectCardId,
            estimateStoryPoint = estimateStoryPoint,
        )
    }

    fun done(
        resultStoryPoint: ResultStoryPoint,
        finishedAt: FinishedAt,
    ): Task = Task(
        taskId = this.taskId,
        projectCardId = this.projectCardId,
        estimateStoryPoint = this.estimateStoryPoint,
        resultStoryPoint = resultStoryPoint,
        finishedAt = finishedAt,
    )

    fun isFinished(): Boolean = resultStoryPoint == null && finishedAt == null

    fun isSameProjectCard(other: Task): Boolean = this.projectCardId == other.projectCardId

    fun notify(notification: TaskNotification) {
        notification.setTaskId(this.taskId)
        notification.setProjectCardId(this.projectCardId)
        notification.setEstimateStoryPoint(this.estimateStoryPoint)
        notification.setResultStoryPoint(this.resultStoryPoint)
        notification.setFinishedAt(this.finishedAt)
    }
}
