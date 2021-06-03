package api.domains.applications.task

import api.domains.models.task.FinishedAt
import api.domains.models.task.ResultStoryPoint
import api.domains.models.task.TaskId
import api.domains.models.task.TaskRepository
import api.usecases.task.errors.TaskDoesNotExistsException
import api.usecases.task.finish.TaskAlreadyFinishedException
import api.usecases.task.finish.TaskFinishException
import api.usecases.task.finish.TaskFinishInputData
import api.usecases.task.finish.TaskFinishOutputData
import api.usecases.task.finish.TaskFinishUseCase
import arrow.core.Either
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class TaskFinishInteractor(
    private val taskRepository: TaskRepository,
): TaskFinishUseCase {

    override fun handle(inputData: TaskFinishInputData): Mono<Either<TaskFinishException, TaskFinishOutputData>> {
        val task = taskRepository.find(TaskId(inputData.taskId))
            ?: return Mono.just(Either.Left(TaskDoesNotExistsException()))

        if (task.isFinished()) {
            return Mono.just(Either.Left(TaskAlreadyFinishedException()))
        }

        val finishedTask = task.copy(
            resultStoryPoint = ResultStoryPoint(inputData.resultStoryPoint),
            finishedAt = FinishedAt(inputData.finishedAt)
        )

        taskRepository.store(finishedTask)

        return Mono.just(Either.Right(TaskFinishOutputData()))
    }
}
