package api.controllers.task

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class CreateTaskRequestBody(
    @field:NotBlank val projectCardId: String?,
    @field:NotNull val estimateStoryPoint: Int?,
)
