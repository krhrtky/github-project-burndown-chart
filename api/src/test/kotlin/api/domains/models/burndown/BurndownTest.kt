package api.domains.models.burndown

import api.domains.models.project.ProjectId
import api.domains.models.task.AddedAt
import api.domains.models.task.EstimateStoryPoint
import api.domains.models.task.ProjectCardId
import api.domains.models.task.Task
import api.domains.models.task.TaskId
import api.domains.types.StoryPoint
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate
import java.time.LocalDateTime

class BurndownTest: DescribeSpec({
    describe(".chartByDateRange") {
        val burnDown = Burndown(
            projectId = ProjectId("test"),
            taskList = listOf(
                Task(
                    taskId = TaskId("task-a"),
                    projectCardId = ProjectCardId("project-a"),
                    estimateStoryPoint = EstimateStoryPoint(StoryPoint(3)),
                    addedAt = AddedAt(LocalDateTime.parse("2021-08-18T19:00:00.00")),
                ),
                Task(
                    taskId = TaskId("task-b"),
                    projectCardId = ProjectCardId("project-b"),
                    estimateStoryPoint = EstimateStoryPoint(StoryPoint(5)),
                    addedAt = AddedAt(LocalDateTime.parse("2021-08-20T19:00:00.00")),
                )
            )
        )
        describe("指定した期間がどちらもタスク作成日より前の場合") {
            it("ポイントを集計しない") {
                val from = LocalDate.parse("2021-08-16")
                val to = LocalDate.parse("2021-08-17")

                val result = burnDown.chartByDateRange(
                    from,
                    to
                )

                result.dateRange.shouldBe(listOf(from, to))
                result.result.shouldBe(listOf(0, 0))
                result.resultDigestion.shouldBe(listOf(0, 0))
                result.estimate.shouldBe(listOf(0, 0))
                result.idealDigestion.shouldBe(listOf(0, 0))
            }
        }

        describe("指定した期間の終点がタスク作成日の場合") {
            it("ポイントが加算されるのみ") {
                val from = LocalDate.parse("2021-08-17")
                val to = LocalDate.parse("2021-08-18")

                val result = burnDown.chartByDateRange(
                    from,
                    to
                )

                result.dateRange.shouldBe(listOf(from, to))
                result.result.shouldBe(listOf(0, 0))
                result.resultDigestion.shouldBe(listOf(0, 3))
                result.estimate.shouldBe(listOf(0, 0))
                result.idealDigestion.shouldBe(listOf(0, 0))
            }
        }

        describe("指定した期間の終点がタスク作成日をまたいでいる場合") {
            it("ポイントの消化見込みに加算される") {
                val from = LocalDate.parse("2021-08-17")
                val to = LocalDate.parse("2021-08-19")

                val result = burnDown.chartByDateRange(
                    from,
                    to
                )

                result.dateRange.shouldBe(listOf(from, LocalDate.parse("2021-08-18"), to))
                result.result.shouldBe(listOf(0, 0))
                result.resultDigestion.shouldBe(listOf(0, 1.5))
                result.estimate.shouldBe(listOf(0, 0))
                result.idealDigestion.shouldBe(listOf(0, 0))
            }
        }
    }
})
