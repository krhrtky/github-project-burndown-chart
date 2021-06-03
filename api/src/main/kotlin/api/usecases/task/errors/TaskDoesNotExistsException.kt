package api.usecases.task.errors

import api.usecases.task.finish.TaskFinishException
import api.usecases.task.update.UpdateTaskException

class TaskDoesNotExistsException: Exception(), UpdateTaskException, TaskFinishException
