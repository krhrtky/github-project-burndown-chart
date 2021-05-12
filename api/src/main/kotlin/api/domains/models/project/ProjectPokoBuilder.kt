package api.domains.models.project

import api.domains.models.task.TaskId as TaskId
import java.lang.IllegalStateException

class ProjectPokoBuilder: ProjectNotification {
    private var projectId: ProjectId? = null
    private var projectNumber: ProjectNumber? = null
    private var organization: Organization? = null
    private var tasks: List<TaskId>? = null

    fun build(): ProjectData {
        if (this.projectId != null && this.projectNumber != null && this.organization != null && tasks != null) {
            return ProjectData(
                id = this.projectId!!.value,
                projectNumber = this.projectNumber!!.value,
                organization = this.organization!!.value,
                tasks = this.tasks!!.map { it.value }
            )
        }

        throw IllegalStateException()
    }

    override fun setId(projectId: ProjectId) {
        this.projectId = projectId
    }

    override fun setProjectNumber(projectNumber: ProjectNumber) {
        this.projectNumber = projectNumber
    }

    override fun setOrganization(organization: Organization) {
        this.organization = organization
    }

    override fun setTasks(tasks: List<TaskId>) {
        this.tasks = tasks
    }
}

data class ProjectData(
    val id: String,
    val projectNumber: Int,
    val organization: String,
    val tasks: List<String>,
)
