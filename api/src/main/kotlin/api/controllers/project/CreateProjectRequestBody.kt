package api.controllers.project

data class CreateProjectRequestBody(
    val organization: String,
    val projectNumber: Int,
)
