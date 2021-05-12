package api.domains.models.project

import api.domains.models.task.TaskId as TaskId

data class Project(
    private val projectId: ProjectId,
    private val organization: Organization,
    private val projectNumber: ProjectNumber,
    val tasks: List<TaskId>,
) {
    fun notify(projectNotification: ProjectNotification) {
        projectNotification.setId(projectId)
        projectNotification.setProjectNumber(projectNumber)
        projectNotification.setOrganization(organization)
        projectNotification.setTasks(tasks)
    }

    fun addTask(taskId: TaskId): Project {
        return Project(
            projectId = this.projectId,
            organization = this.organization,
            projectNumber = this.projectNumber,
            tasks = tasks.plus(taskId)
        )
    }

    fun hasTasks(): Boolean = !this.tasks.isEmpty()
}
