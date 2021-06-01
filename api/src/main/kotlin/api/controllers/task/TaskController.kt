package api.controllers.task

import api.usecases.task.create.ProjectNotExistsException
import api.usecases.task.create.TaskAlreadyExistsException
import api.usecases.task.create.TaskCreateInputData
import api.usecases.task.create.TaskCreateOutputData
import api.usecases.task.create.TaskCreateUseCase
import api.usecases.task.errors.TaskDoesNotExistsException
import api.usecases.task.update.TaskUpdateInputData
import api.usecases.task.update.TaskUpdateOutputData
import api.usecases.task.update.TaskUpdateUseCase
import arrow.core.Either
import javax.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Mono

@CrossOrigin
@RestController
class TaskController(
    private val taskCreateUseCase: TaskCreateUseCase,
    private val taskUpdateUseCase: TaskUpdateUseCase,
) {

    @PostMapping("/projects/{projectId}/tasks")
    fun create(
        @PathVariable projectId: String,
        @Valid @RequestBody requestBody: Mono<CreateTaskRequestBody>,
    ) = requestBody
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

    @PostMapping("/projects/{projectId}/tasks/{taskId}")
    fun update(
        @PathVariable projectId: String,
        @PathVariable taskId: String,
        @Valid @RequestBody requestBody: Mono<UpdateTaskRequestBody>,
    ): Mono<TaskUpdateOutputData> = requestBody
        .flatMap {
            taskUpdateUseCase.handle(
                TaskUpdateInputData(
                    taskId = taskId,
                    estimateStoryPoint = it.estimateStoryPoint,
                    resultStoryPoint = it.resultStoryPoint,
                    finishedAt = it.finishedAt,
                )
            )
        }
        .handle { result, sink ->
            when(result) {
                is Either.Right -> sink.next(result.value)
                is Either.Left ->
                    when(result.value) {
                        is TaskDoesNotExistsException -> sink.error(
                            ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)
                        )
                        else -> sink.error(ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR))
                    }
            }
        }
}
