package api.controllers.task

import api.usecases.task.create.ProjectNotExistsException
import api.usecases.task.create.TaskAlreadyExistsException
import api.usecases.task.create.TaskCreateInputData
import api.usecases.task.create.TaskCreateOutputData
import api.usecases.task.create.TaskCreateUseCase
import arrow.core.Either
import javax.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono

@RestController
class TaskController(
    private val taskCreateUseCase: TaskCreateUseCase,
) {

    @PostMapping("/projects/{projectId}/tasks")
    fun create(
        @PathVariable projectId: String,
        @Valid @RequestBody requestBody: Mono<CreateTaskRequestBody>,
    ): Mono<TaskCreateOutputData> {

        return requestBody
            .flatMap { t ->
                taskCreateUseCase
                    .handle(TaskCreateInputData(projectId, t.projectCardId ?: 0, t.estimateStoryPoint ?: 0))
            }
            .handle<TaskCreateOutputData> { result, sink ->
                when(result) {
                    is Either.Right -> sink.next(result.value)
                    is Either.Left ->
                        when(result.value) {
                            is TaskAlreadyExistsException -> sink.error(ResponseStatusException(HttpStatus.CONFLICT))
                            is ProjectNotExistsException -> sink.error(ResponseStatusException(HttpStatus.NOT_FOUND))
                            else -> sink.error(ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR))
                        }
                }
            }
    }
}
