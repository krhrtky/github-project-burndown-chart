package api.domains.applications.project

import api.domains.models.project.Id
import api.domains.models.project.Organization
import api.domains.models.project.Project
import api.domains.models.project.ProjectNumber
import api.domains.models.project.ProjectRepository
import api.domains.models.project.ProjectService
import api.usecases.project.create.ProjectAlreadyExistsException
import api.usecases.project.create.ProjectCreateInputData
import api.usecases.project.create.ProjectCreateOutPutData
import api.usecases.project.create.ProjectCreateUseCase
import arrow.core.Either
import java.util.UUID
import org.springframework.stereotype.Service

@Service
class ProjectCreateInteractor(
    private val projectRepository: ProjectRepository,
): ProjectCreateUseCase {
    private val projectService: ProjectService = ProjectService(projectRepository)

    override fun handle(inputData: ProjectCreateInputData): Either<
            ProjectAlreadyExistsException,
            ProjectCreateOutPutData
            > {
        val uuid = UUID.randomUUID().toString()
        val newProject = Project(
            id = Id(uuid),
            organization = Organization(inputData.organization),
            projectNumber = ProjectNumber(inputData.projectNumber),
            tasks = emptyList(),
        )

        if (projectService.isExists(newProject)) {
            return Either.Left(ProjectAlreadyExistsException())
        }

        projectRepository.store(newProject)

        return Either.Right(ProjectCreateOutPutData(uuid))
    }
}
