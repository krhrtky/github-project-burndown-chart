package api.domains.applications.burndown

import api.domains.models.burndown.BurndownQueryService
import api.domains.models.project.ProjectId
import api.domains.models.types.BusinessCalendar
import api.usecases.burndown.get.BurndownGetException
import api.usecases.burndown.get.BurndownGetInputData
import api.usecases.burndown.get.BurndownGetOutputData
import api.usecases.burndown.get.BurndownGetUseCase
import api.usecases.task.create.ProjectNotExistsException
import arrow.core.Either
import java.time.LocalDate
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class BurndownGetInteractor(
    private val burndownQueryService: BurndownQueryService,
): BurndownGetUseCase {
    override fun handle(inputData: BurndownGetInputData): Mono<Either<BurndownGetException, BurndownGetOutputData>> {
        val fromDate = inputData.from?.toLocalDate() ?: LocalDate.now().minusMonths(1)
        val toDate = inputData.to?.toLocalDate() ?: BusinessCalendar.default.lastBusinessDay()

        val burndown = burndownQueryService.query(
            ProjectId(inputData.projectId),
            fromDate,
        ) ?: return Mono.just(Either.Left(ProjectNotExistsException()))

        val chart = burndown.chartByDateRange(
            fromDate,
            toDate
        )

        return Mono.just(
            Either.Right(
                BurndownGetOutputData(
                    dateRange = chart.dateRange,
                    ideal = chart.idealDigestion,
                    finished = chart.resultDigestion,
                    estimate = chart.estimate,
                    result = chart.result,
                )
            )
        )
    }
}
