package api.controllers.task

import api.controllers.validators.fibonacci.Fibonacci
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class CreateTaskRequestBody(
    @field:NotBlank val projectCardId: String?,
    @field:NotNull @Fibonacci val estimateStoryPoint: Int?,
)
