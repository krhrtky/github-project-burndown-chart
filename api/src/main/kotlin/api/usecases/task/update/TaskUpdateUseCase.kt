package api.usecases.task.update

import api.usecases.core.UseCase

interface TaskUpdateUseCase: UseCase<TaskUpdateInputData, TaskUpdateOutputData, UpdateTaskException>
