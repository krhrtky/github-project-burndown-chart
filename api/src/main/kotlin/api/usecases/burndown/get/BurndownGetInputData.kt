package api.usecases.burndown.get

import java.time.LocalDateTime

data class BurndownGetInputData(
    val projectId: String,
    val from: LocalDateTime?,
    val to: LocalDateTime?,
)
