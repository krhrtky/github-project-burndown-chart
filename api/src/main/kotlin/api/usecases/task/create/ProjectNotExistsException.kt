package api.usecases.task.create

import api.usecases.burndown.get.BurndownGetException

class ProjectNotExistsException: Exception(), CreateTaskException, BurndownGetException
