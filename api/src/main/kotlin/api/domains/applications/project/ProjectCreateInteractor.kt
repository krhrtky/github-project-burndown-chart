package api.domains.applications.project

import api.domains.models.project.Organization
import api.domains.models.project.Project
import api.domains.models.project.ProjectId
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
import reactor.core.publisher.Mono

@Service
class ProjectCreateInteractor(
    private val projectRepository: ProjectRepository,
): ProjectCreateUseCase {
    private val projectService: ProjectService = ProjectService(projectRepository)

    override fun handle(inputData: ProjectCreateInputData): Mono<Either<
            ProjectAlreadyExistsException,
            ProjectCreateOutPutData
            >> {
        val uuid = UUID.randomUUID().toString()
        val newProject = Project(
            projectId = ProjectId(uuid),
            organization = Organization(inputData.organization),
            projectNumber = ProjectNumber(inputData.projectNumber),
            tasks = emptyList(),
        )

        if (projectService.isDuplicated(newProject)) {
            return Mono.just(Either.Left(ProjectAlreadyExistsException()))
        }

        projectRepository.store(newProject)

        return Mono.just(Either.Right(ProjectCreateOutPutData(uuid)))
    }
}
