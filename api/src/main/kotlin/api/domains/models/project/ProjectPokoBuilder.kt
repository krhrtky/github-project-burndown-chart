package api.domains.models.project

import api.domains.models.task.Id as TaskId
import java.lang.IllegalStateException

class ProjectPokoBuilder: ProjectNotification {
    private var id: Id? = null
    private var projectNumber: ProjectNumber? = null
    private var organization: Organization? = null
    private var tasks: List<TaskId>? = null

    fun build(): ProjectData {
        if (this.id != null && this.projectNumber != null && this.organization != null && tasks != null) {
            return ProjectData(
                id = this.id!!.value,
                projectNumber = this.projectNumber!!.value,
                organization = this.organization!!.value,
                tasks = this.tasks!!.map { it.value }
            )
        }

        throw IllegalStateException()
    }

    override fun setId(id: Id) {
        this.id = id
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
