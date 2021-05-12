package api.usecases.task.create

import api.usecases.core.UseCase

interface TaskCreateUseCase: UseCase<TaskCreateInputData, TaskCreateOutputData, CreateTaskException>
