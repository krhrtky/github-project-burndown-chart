package api.controllers.project

import api.usecases.project.create.ProjectCreateInputData
import api.usecases.project.create.ProjectCreateOutPutData
import api.usecases.project.create.ProjectCreateUseCase
import arrow.core.Either
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono

@CrossOrigin
@RestController
class ProjectController(
    private val projectCreateUseCase: ProjectCreateUseCase,
) {
    @PostMapping("/projects")
    @ApiResponses(value = [
        ApiResponse(
            responseCode = "200",
            description = "Success",
            content = [Content(schema = Schema(implementation = ProjectCreateOutPutData::class))]
        ),
    ])
    fun create(
        @RequestBody @Validated requestBody: Mono<CreateProjectRequestBody>,
    ): Mono<ProjectCreateOutPutData> = requestBody
        .flatMap {
            projectCreateUseCase
                .handle(
                    ProjectCreateInputData(
                        it.organization ?: "",
                        it.projectNumber ?: 0,
                    )
                )
        }
        .handle { result, sink ->
            when(result) {
                is Either.Right -> sink.next(result.value)
                is Either.Left -> sink.error(ResponseStatusException(HttpStatus.CONFLICT))
            }
        }
}
