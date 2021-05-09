package api.controllers.project

import api.usecases.project.create.ProjectCreateInputData
import api.usecases.project.create.ProjectCreateOutPutData
import api.usecases.project.create.ProjectCreateUseCase
import arrow.core.Either
import org.springframework.http.HttpStatus
import org.springframework.http.server.ServletServerHttpResponse
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
    ): ProjectCreateOutPutData {
        try {
            return when(
                val result = projectCreateUseCase
                    .handle(
                        ProjectCreateInputData(
                            requestBody.organization,
                            requestBody.projectNumber
                        )
                    )
            ) {
                is Either.Right -> result.value
                is Either.Left -> throw ResponseStatusException(HttpStatus.CONFLICT)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            throw ResponseStatusException(HttpStatus.CONFLICT)
        }
    }
}
