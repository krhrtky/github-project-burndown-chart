package api.domains.models.task

interface TaskRepository {
    fun store(task: Task)
    fun findByIds(taskIds: List<TaskId>): List<Task>
}
