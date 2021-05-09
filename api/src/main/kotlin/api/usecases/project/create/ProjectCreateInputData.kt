package api.usecases.project.create

import api.usecases.core.InputData

data class ProjectCreateInputData(
    val organization: String,
    val projectNumber: Int,
): InputData
