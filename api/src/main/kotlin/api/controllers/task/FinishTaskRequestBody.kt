package api.controllers.task

import api.controllers.validators.fibonacci.Fibonacci
import java.time.LocalDateTime
import javax.validation.constraints.NotNull

data class FinishTaskRequestBody(
    @field:NotNull @Fibonacci val resultStoryPoint: Int?,
    @field:NotNull val finishedAt: LocalDateTime?,
)
