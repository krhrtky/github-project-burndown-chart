package api.domains.models.project

interface ProjectRepository {
    fun store(project: Project)
    fun findByOrganizationAndNumber(project: Project): Project?
}
