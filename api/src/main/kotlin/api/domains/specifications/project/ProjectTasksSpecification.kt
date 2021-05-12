package api.domains.specifications.project

import api.domains.models.project.ProjectTasks

class ProjectTasksSpecification {
    companion object {
        fun isSatisfiedBy(projectTasks: ProjectTasks): Boolean = !projectTasks.hasDuplicatedTask()
    }
}
