package api.domains.models.project

class ProjectService(private val projectRepository: ProjectRepository) {
    fun isExists(project: Project): Boolean {
        val maybeProject = projectRepository.findByOrganizationAndNumber(project)

        return maybeProject != null
    }
}
