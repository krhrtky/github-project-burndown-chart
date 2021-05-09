package api.domains.models.project

import api.domains.models.task.Id as TaskId

interface ProjectNotification {
    fun setId(id: Id)
    fun setProjectNumber(projectNumber: ProjectNumber)
    fun setOrganization(organization: Organization)
    fun setTasks(tasks: List<TaskId>)
}
