package api.domains.applications.task

import api.domains.models.task.EstimateStoryPoint
import api.domains.models.task.FinishedAt
import api.domains.models.task.ResultStoryPoint
import api.domains.models.task.TaskId
import api.domains.models.task.TaskPokoBuilder
import api.domains.models.task.TaskRepository
import api.domains.types.StoryPoint
import api.usecases.task.errors.TaskDoesNotExistsException
import api.usecases.task.update.TaskUpdateInputData
import api.usecases.task.update.TaskUpdateOutputData
import api.usecases.task.update.TaskUpdateUseCase
import api.usecases.task.update.UpdateTaskException
import arrow.core.Either
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class TaskUpdateInteractor(
    private val taskRepository: TaskRepository,
): TaskUpdateUseCase {
    override fun handle(inputData: TaskUpdateInputData): Mono<Either<UpdateTaskException, TaskUpdateOutputData>> {

        val task = taskRepository.find(TaskId(inputData.taskId))
            ?: return Mono.just(Either.Left(TaskDoesNotExistsException()))

        val rawValue = TaskPokoBuilder()
            .extract(task)
            .build()

        // TODO: To be simplify or spin out to Factory.
        val resultStoryPoint = inputData.resultStoryPoint ?: rawValue.resultStoryPoint?.let { StoryPoint(it) }
        val finishedAt = inputData.finishedAt ?: rawValue.finishedAt
        val updatedTask = task.copy(
            estimateStoryPoint = EstimateStoryPoint(
                inputData.estimateStoryPoint ?: StoryPoint(rawValue.estimateStoryPoint)
            ),
            resultStoryPoint = if (resultStoryPoint == null) null else ResultStoryPoint(resultStoryPoint),
            finishedAt = if (finishedAt == null) null else FinishedAt(finishedAt)
        )

        taskRepository.store(updatedTask)

        return Mono.just(
            Either.Right(
                TaskUpdateOutputData(
                    inputData.taskId,
                    rawValue.projectCardId,
                    inputData.estimateStoryPoint?.toInt() ?: rawValue.estimateStoryPoint,
                    resultStoryPoint?.toInt(),
                    finishedAt,
                )
            )
        )
    }
}
