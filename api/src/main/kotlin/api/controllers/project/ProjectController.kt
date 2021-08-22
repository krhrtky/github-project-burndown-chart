package api.controllers.project

import api.usecases.burndown.get.BurndownGetInputData
import api.usecases.burndown.get.BurndownGetOutputData
import api.usecases.burndown.get.BurndownGetUseCase
import api.usecases.project.create.ProjectCreateInputData
import api.usecases.project.create.ProjectCreateOutPutData
import api.usecases.project.create.ProjectCreateUseCase
import api.usecases.task.create.ProjectNotExistsException
import arrow.core.Either
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import java.time.LocalDateTime
import java.time.ZoneId
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono

@CrossOrigin
@RestController
class ProjectController(
    private val projectCreateUseCase: ProjectCreateUseCase,
    private val burndownGetUseCase: BurndownGetUseCase,
) {
    @PostMapping("/projects")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "Success",
                content = [Content(schema = Schema(implementation = ProjectCreateOutPutData::class))]
            ),
        ]
    )
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
            when (result) {
                is Either.Right -> sink.next(result.value)
                is Either.Left -> sink.error(ResponseStatusException(HttpStatus.CONFLICT))
            }
        }

    @GetMapping("/projects/{projectId}/burndown")
    fun burndown(
        @PathVariable projectId: String,
        @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) from: LocalDateTime?,
        @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) to: LocalDateTime?
    ): Mono<BurndownGetOutputData> = burndownGetUseCase
        .handle(
            BurndownGetInputData(
                projectId,
                from?.atZone(ZoneId.of("Asia/Tokyo"))?.toLocalDateTime(),
                to?.atZone(ZoneId.of("Asia/Tokyo"))?.toLocalDateTime(),
            )
        ).handle { result, sink ->
            when (result) {
                is Either.Right -> sink.next(result.value)
                is Either.Left ->
                    when (result.value) {
                        is ProjectNotExistsException -> sink.error(ResponseStatusException(HttpStatus.NOT_FOUND))
                    }
            }
        }
}
