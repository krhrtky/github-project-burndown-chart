package api.controllers.task

import java.time.LocalDateTime
import javax.validation.constraints.NotNull

data class FinishTaskRequestBody(
    @field:NotNull val resultStoryPoint: Int?,
    @field:NotNull val finishedAt: LocalDateTime?,
)
