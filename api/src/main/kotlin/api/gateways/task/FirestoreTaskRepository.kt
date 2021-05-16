package api.gateways.task

import api.domains.models.task.EstimateStoryPoint
import api.domains.models.task.FinishedAt
import api.domains.models.task.ProjectCardId
import api.domains.models.task.ResultStoryPoint
import api.domains.models.task.Task
import api.domains.models.task.TaskId
import api.domains.models.task.TaskPokoBuilder
import api.domains.models.task.TaskRepository
import com.google.cloud.firestore.FieldPath
import com.google.cloud.firestore.Firestore
import java.time.LocalDateTime
import org.springframework.stereotype.Repository

@Repository
class FirestoreTaskRepository(
    private val firestore: Firestore
): TaskRepository {
    private val collection = firestore.collection("task")

    override fun store(task: Task) {
        val builder = TaskPokoBuilder()
        task.notify(builder)
        val taskData = builder.build()

        collection
            .document(taskData.taskId)
            .set(
                DocumentData(
                    projectCardId = taskData.projectCardId,
                    estimateStoryPoint = taskData.estimateStoryPoint,
                    resultStoryPoint = taskData.resultStoryPoint,
                    finishedAt = taskData.finishedAt,
                )
            )
    }

    override fun findById(taskId: TaskId): Task? =
        collection
            .document(taskId.value)
            .get()
            .get()
            .toObject(DocumentData::class.java)?.let {
                Task(
                    taskId = taskId,
                    projectCardId = ProjectCardId(it.projectCardId),
                    estimateStoryPoint = EstimateStoryPoint(it.estimateStoryPoint),
                    resultStoryPoint = it.resultStoryPoint?.let {
                            resultStoryPoint -> ResultStoryPoint(resultStoryPoint)
                    },
                    finishedAt = it.finishedAt?.let {
                            finishedAt -> FinishedAt(finishedAt)
                    }
                )
            }

    override fun findByIds(taskIds: List<TaskId>): List<Task> =
        if (taskIds.isEmpty()) emptyList()
        else collection
            .whereIn(FieldPath.documentId(), taskIds.map { it.value })
            .get()
            .get()
            .mapNotNull { doc ->
                val data = doc.toObject(DocumentData::class.java)
                Task(
                    taskId = TaskId(doc.id),
                    projectCardId = ProjectCardId(data.projectCardId),
                    estimateStoryPoint = EstimateStoryPoint(data.estimateStoryPoint),
                    resultStoryPoint = data.resultStoryPoint?.let { ResultStoryPoint(data.resultStoryPoint!!) },
                    finishedAt = data.finishedAt?.let { FinishedAt(it) }
                )
            }
}

private data class DocumentData(
    var projectCardId: Int = -1,
    var estimateStoryPoint: Int = 0,
    var resultStoryPoint: Int? = null,
    var finishedAt: LocalDateTime? = null,
)
