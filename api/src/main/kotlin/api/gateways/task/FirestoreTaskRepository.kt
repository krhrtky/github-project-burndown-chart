package api.gateways.task

import api.domains.models.task.EstimateStoryPoint
import api.domains.models.task.FinishedAt
import api.domains.models.task.ProjectCardId
import api.domains.models.task.ResultStoryPoint
import api.domains.models.task.Task
import api.domains.models.task.TaskId
import api.domains.models.task.TaskPokoBuilder
import api.domains.models.task.TaskRepository
import api.domains.types.StoryPoint
import com.google.cloud.Timestamp
import com.google.cloud.firestore.FieldPath
import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.QueryDocumentSnapshot
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Date
import org.springframework.stereotype.Repository

@Repository
class FirestoreTaskRepository(
    private val firestore: Firestore
): TaskRepository {
    private val collection = firestore.collection("task")
    private val mapToModel = { queryDocumentSnapshot: QueryDocumentSnapshot ->
        val data = queryDocumentSnapshot.toObject(DocumentData::class.java)
        Task(
            taskId = TaskId(queryDocumentSnapshot.id),
            projectCardId = ProjectCardId(data.projectCardId),
            estimateStoryPoint = EstimateStoryPoint(StoryPoint(data.estimateStoryPoint)),
            resultStoryPoint = data.resultStoryPoint?.let { ResultStoryPoint(StoryPoint(data.resultStoryPoint!!)) },
            finishedAt = data.finishedAt?.let { FinishedAt(it.toDate().toLocalDateTime()) }
        )
    }

    override fun store(task: Task) {
        val taskData = TaskPokoBuilder()
            .extract(task)
            .build()

        collection
            .document(taskData.taskId)
            .set(
                DocumentData(
                    projectCardId = taskData.projectCardId,
                    estimateStoryPoint = taskData.estimateStoryPoint,
                    resultStoryPoint = taskData.resultStoryPoint,
                    finishedAt = taskData.finishedAt?.let {
                        Timestamp.of(it.toDate())
                    },
                )
            )
    }

    override fun find(taskId: TaskId): Task? =
        collection
            .document(taskId.value)
            .get()
            .get()
            .toObject(DocumentData::class.java)?.let {
                Task(
                    taskId = taskId,
                    projectCardId = ProjectCardId(it.projectCardId),
                    estimateStoryPoint = EstimateStoryPoint(StoryPoint(it.estimateStoryPoint)),
                    resultStoryPoint = it.resultStoryPoint?.let {
                            resultStoryPoint -> ResultStoryPoint(StoryPoint(resultStoryPoint))
                    },
                    finishedAt = it.finishedAt?.let {
                            finishedAt -> FinishedAt(finishedAt.toDate().toLocalDateTime())
                    }
                )
            }

    override fun find(projectCardId: ProjectCardId): Task? =
        collection
            .whereEqualTo("projectCardId", projectCardId.value)
            .get()
            .get()
            .mapNotNull(mapToModel)
            .first()

    override fun find(taskIds: List<TaskId>): List<Task> =
        if (taskIds.isEmpty()) emptyList()
        else collection
            .whereIn(FieldPath.documentId(), taskIds.map { it.value })
            .get()
            .get()
            .mapNotNull(mapToModel)
}

private fun Date.toLocalDateTime() = this.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
private fun LocalDateTime.toDate() = Date.from(ZonedDateTime.of(this, ZoneId.systemDefault()).toInstant())

private data class DocumentData(
    var projectCardId: String = "",
    var estimateStoryPoint: Int = 0,
    var resultStoryPoint: Int? = null,
    var finishedAt: Timestamp? = null,
)
