package api.controllers.project

import api.usecases.project.create.ProjectCreateInputData
import api.usecases.project.create.ProjectCreateOutPutData
import api.usecases.project.create.ProjectCreateUseCase
import arrow.core.Either
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono

@RestController
class ProjectController(
    private val projectCreateUseCase: ProjectCreateUseCase,
) {
    @PostMapping("/projects")
    fun create(
        @RequestBody requestBody: CreateProjectRequestBody,
    ): Mono<ProjectCreateOutPutData> {
        return projectCreateUseCase
                .handle(
                    ProjectCreateInputData(
                        requestBody.organization,
                        requestBody.projectNumber
                    )
                ).handle { result, sink ->
                when(result) {
                    is Either.Right -> sink.next(result.value)
                    is Either.Left -> sink.error(ResponseStatusException(HttpStatus.CONFLICT))
                }
            }
    }
}
