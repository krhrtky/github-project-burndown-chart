package api.domains.models.project

import api.domains.models.task.TaskId as TaskId

interface ProjectNotification {
    fun setId(projectId: ProjectId)
    fun setProjectNumber(projectNumber: ProjectNumber)
    fun setOrganization(organization: Organization)
    fun setTasks(tasks: List<TaskId>)
}
