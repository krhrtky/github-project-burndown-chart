package api.domains.models.task

interface TaskRepository {
    fun store(task: Task)
    fun find(taskId: TaskId): Task?
    fun find(projectCardId: ProjectCardId): Task?
    fun find(taskIds: List<TaskId>): List<Task>
}
