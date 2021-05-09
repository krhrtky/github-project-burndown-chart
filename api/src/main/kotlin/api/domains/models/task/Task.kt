package api.domains.models.task

class Task private constructor(
    private val id: Id,
    private val estimateStoryPoint: EstimateStoryPoint,
    private val resultStoryPoint: ResultStoryPoint? = null,
    private val finishedAt: FinishedAt? = null,
) {

    companion object {
        fun create(
            id: Id,
            estimateStoryPoint: EstimateStoryPoint,
        ): Task = Task(
            id = id,
            estimateStoryPoint = estimateStoryPoint,
        )
    }

    fun done(
        resultStoryPoint: ResultStoryPoint,
        finishedAt: FinishedAt,
    ): Task = Task(
        id,
        estimateStoryPoint = this.estimateStoryPoint,
        resultStoryPoint = resultStoryPoint,
        finishedAt = finishedAt,
    )

    fun isFinished(): Boolean = resultStoryPoint == null && finishedAt == null

    override fun equals(other: Any?): Boolean {
        if (other == null) {
            return false
        }
        if (other is Task) {
            return this.id.value == other.id.value
        }
        return false
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    override fun toString(): String {
        return super.toString()
    }
}
