package api.domains.models.project

class ProjectService(private val projectRepository: ProjectRepository) {
    fun isDuplicated(project: Project): Boolean {
        val maybeProject = projectRepository.findByOrganizationAndNumber(project)

        return maybeProject != null
    }
    fun isExits(projectId: ProjectId): Boolean {
        val maybeProject = projectRepository.findById(projectId)
        return maybeProject != null
    }

}
