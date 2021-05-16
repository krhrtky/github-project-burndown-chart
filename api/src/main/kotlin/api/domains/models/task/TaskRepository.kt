package api.domains.models.task

interface TaskRepository {
    fun store(task: Task)
    fun findById(taskId: TaskId): Task?
    fun findByIds(taskIds: List<TaskId>): List<Task>
}
