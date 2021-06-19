package api.domains.models.burndown

import api.domains.models.project.ProjectId
import api.domains.models.task.AddedAt
import api.domains.models.task.EstimateStoryPoint
import api.domains.models.task.FinishedAt
import api.domains.models.task.ProjectCardId
import api.domains.models.task.ResultStoryPoint
import api.domains.models.task.Task
import api.domains.models.task.TaskId
import api.domains.types.StoryPoint
import com.google.cloud.Timestamp
import com.google.cloud.firestore.FieldPath
import com.google.cloud.firestore.Firestore
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Date
import org.springframework.stereotype.Service

@Service
class BurndownQueryService(
    private val firestore: Firestore
) {
    private val projectCollection = firestore.collection("project")
    private val taskCollection = firestore.collection("task")

    fun query(projectId: ProjectId, from: LocalDate): Burndown? {

        val projectDocumentSnapshot = projectCollection.document(projectId.value).get().get()

        val projectDoc = projectDocumentSnapshot.toObject(ProjectDocumentData::class.java) ?: return null

        if (projectDoc.tasks.isEmpty()) {
            return Burndown(
                projectId = projectId,
                taskList = emptyList(),
            )
        }

        val tasks = taskCollection
            .whereIn(FieldPath.documentId(), projectDoc.tasks)
            .get()
            .get()
            .mapNotNull { queryDocumentSnapshot ->
                val data = queryDocumentSnapshot.toObject(TaskDocumentData::class.java)
                Task(
                    taskId = TaskId(queryDocumentSnapshot.id),
                    projectCardId = ProjectCardId(data.projectCardId),
                    estimateStoryPoint = EstimateStoryPoint(StoryPoint(data.estimateStoryPoint)),
                    addedAt = AddedAt(data.addedAt.toDate().toLocalDateTime()),
                    resultStoryPoint = data.resultStoryPoint?.let {
                        ResultStoryPoint(StoryPoint(data.resultStoryPoint!!))
                    },
                    finishedAt = data.finishedAt?.let { FinishedAt(it.toDate().toLocalDateTime()) }
                )
            }

        return Burndown(
            projectId = projectId,
            taskList = tasks,
        )
    }
}

private data class ProjectDocumentData(
    var projectNumber: Int = -1,
    var organization: String = "",
    var tasks: List<String> = emptyList(),
)
private fun Date.toLocalDate() = this.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
private fun LocalDate.toDate() = Date.from(this.atStartOfDay(ZoneId.systemDefault()).toInstant())
private fun Date.toLocalDateTime() = this.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
private fun LocalDateTime.toDate() = Date.from(ZonedDateTime.of(this, ZoneId.systemDefault()).toInstant())

private data class TaskDocumentData(
    var projectCardId: String = "",
    var estimateStoryPoint: Int = 0,
    val addedAt: Timestamp = Timestamp.now(),
    var resultStoryPoint: Int? = null,
    var finishedAt: Timestamp? = null,
)
