package api.controllers.task

import javax.validation.constraints.NotNull

data class CreateTaskRequestBody(
    @field:NotNull val projectCardId: Int?,
    @field:NotNull val estimateStoryPoint: Int?,
)
