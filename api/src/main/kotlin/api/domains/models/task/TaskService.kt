package api.domains.models.task

class TaskService(private val taskRepository: TaskRepository) {
    fun isExists(projectCardId: ProjectCardId): Boolean {
        val existsTask = taskRepository.find(projectCardId)
        return existsTask != null
    }
}
