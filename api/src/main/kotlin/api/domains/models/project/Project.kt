package api.domains.models.project

import api.domains.models.task.Id as TaskId

data class Project(
    private val id: Id,
    private val organization: Organization,
    private val projectNumber: ProjectNumber,
    private val tasks: List<TaskId>
) {
    fun notify(projectNotification: ProjectNotification) {
        projectNotification.setId(id)
        projectNotification.setProjectNumber(projectNumber)
        projectNotification.setOrganization(organization)
        projectNotification.setTasks(tasks)
    }
}
