package api.controllers.task

import api.domains.types.StoryPoint
import api.usecases.task.create.ProjectNotExistsException
import api.usecases.task.create.TaskAlreadyExistsException
import api.usecases.task.create.TaskCreateInputData
import api.usecases.task.create.TaskCreateOutputData
import api.usecases.task.create.TaskCreateUseCase
import api.usecases.task.errors.TaskDoesNotExistsException
import api.usecases.task.finish.TaskAlreadyFinishedException
import api.usecases.task.finish.TaskFinishInputData
import api.usecases.task.finish.TaskFinishOutputData
import api.usecases.task.finish.TaskFinishUseCase
import api.usecases.task.update.TaskUpdateInputData
import api.usecases.task.update.TaskUpdateOutputData
import api.usecases.task.update.TaskUpdateUseCase
import arrow.core.Either
import java.time.LocalDateTime
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
    private val taskFinishUseCase: TaskFinishUseCase,
) {

    @PostMapping("/projects/{projectId}/tasks")
    fun create(
        @PathVariable projectId: String,
        @Valid @RequestBody requestBody: Mono<CreateTaskRequestBody>,
    ) = requestBody
            .flatMap { t ->
                taskCreateUseCase
                    .handle(
                        TaskCreateInputData(
                            projectId,
                            t.projectCardId ?: "",
                            StoryPoint(t.estimateStoryPoint ?: 0)))
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
                    estimateStoryPoint = it.estimateStoryPoint?.let { StoryPoint(it) },
                    resultStoryPoint = it.resultStoryPoint?.let { StoryPoint(it) },
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
                            ResponseStatusException(HttpStatus.NOT_FOUND)
                        )
                        else -> sink.error(ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR))
                    }
            }
        }

    @PostMapping("/tasks/{taskId}/finish")
    fun finish(
        @PathVariable taskId: String,
        @Valid @RequestBody requestBody: Mono<FinishTaskRequestBody>,
    ): Mono<TaskFinishOutputData> = requestBody
        .flatMap {
            taskFinishUseCase.handle(
                TaskFinishInputData(
                    taskId = taskId,
                    resultStoryPoint = StoryPoint(it.resultStoryPoint ?: 0),
                    finishedAt = it.finishedAt ?: LocalDateTime.now()
                )
            )
        }
        .handle { result, sink ->
            when(result) {
                is Either.Right -> sink.next(result.value)
                is Either.Left ->
                    when(result.value) {
                        is TaskDoesNotExistsException -> sink.error(
                            ResponseStatusException(HttpStatus.NOT_FOUND)
                        )
                        is TaskAlreadyFinishedException -> sink.error(
                            ResponseStatusException(HttpStatus.CONFLICT)
                        )
                        else -> sink.error(ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR))
                    }
            }
        }
}
