package api.controllers.project

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class CreateProjectRequestBody(
    @field:NotBlank val organization: String?,
    @field:NotNull val projectNumber: Int?,
)
