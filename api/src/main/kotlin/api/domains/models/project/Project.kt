package api.domains.models.project

import api.domains.models.task.TaskId as TaskId
import java.util.UUID

data class Project(
    val projectId: ProjectId,
    private val organization: Organization,
    private val projectNumber: ProjectNumber,
    val tasks: List<TaskId>,
) {
    companion object {
        fun create(organization: Organization, projectNumber: ProjectNumber): Project {
            val uuid = UUID.randomUUID().toString()

            return Project(
                projectId = ProjectId(uuid),
                organization = organization,
                projectNumber = projectNumber,
                tasks = emptyList(),
            )
        }
    }

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
