package api.domains.models.project

import api.domains.models.task.Task

data class ProjectTasks(private val tasks: List<Task>) {
    fun hasDuplicatedTask() = this.tasks.any {
        this.tasks.filter { task ->
            it.isSameProjectCard(task)
        }.size > 1
    }
}
