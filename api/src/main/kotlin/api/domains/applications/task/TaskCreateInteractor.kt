package api.domains.applications.task

import api.domains.models.project.ProjectId
import api.domains.models.project.ProjectTasks
import api.domains.models.task.EstimateStoryPoint
import api.domains.models.task.ProjectCardId
import api.domains.models.task.Task
import api.domains.models.task.TaskId
import api.domains.models.task.TaskRepository
import api.domains.specifications.project.ProjectTasksSpecification
import api.gateways.project.FirestoreProjectRepository
import api.usecases.task.create.CreateTaskException
import api.usecases.task.create.ProjectNotExistsException
import api.usecases.task.create.TaskAlreadyExistsException
import api.usecases.task.create.TaskCreateInputData
import api.usecases.task.create.TaskCreateOutputData
import api.usecases.task.create.TaskCreateUseCase
import arrow.core.Either
import java.util.UUID
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Mono

@Transactional
@Service
class TaskCreateInteractor(
    private val projectRepository: FirestoreProjectRepository,
    private val taskRepository: TaskRepository,
): TaskCreateUseCase {

    override fun handle(inputData: TaskCreateInputData): Mono<Either<
            CreateTaskException,
            TaskCreateOutputData,
            >> {

        val project = projectRepository.findById(ProjectId(inputData.projectId))
            ?: return Mono.just(Either.Left(ProjectNotExistsException()))

        val id = UUID.randomUUID().toString()
        val newTaskId = TaskId(id)
        val newTask = Task.create(
            taskId = newTaskId,
            projectCardId = ProjectCardId(inputData.projectCardId),
            estimateStoryPoint = EstimateStoryPoint(inputData.estimateStoryPoint),
        )

        val tasks = taskRepository.findByIds(project.tasks)

        if (ProjectTasksSpecification.isSatisfiedBy(ProjectTasks(tasks.plus(newTask)))) {
            val projectAddedTask = project.addTask(newTaskId)

            projectRepository.store(projectAddedTask)
            taskRepository.store(newTask)

            return Mono.just(Either.Right(TaskCreateOutputData(id)))
        }

        return Mono.just(Either.Left(TaskAlreadyExistsException()))
    }
}
