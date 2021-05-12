package api.gateways.project

import api.domains.models.project.Organization
import api.domains.models.project.Project
import api.domains.models.project.ProjectId
import api.domains.models.project.ProjectNumber
import api.domains.models.project.ProjectPokoBuilder
import api.domains.models.project.ProjectRepository
import api.domains.models.task.TaskId
import com.google.cloud.firestore.Firestore
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository

@Repository
class FirestoreProjectRepository(
    private val firestore: Firestore,
): ProjectRepository {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val collection = firestore.collection("project")

    override fun store(project: Project) {
        val builder = ProjectPokoBuilder()
        project.notify(builder)
        val projectData = builder.build()

        collection
            .document(projectData.id)
            .set(
                DocumentData(
                    projectNumber = projectData.projectNumber,
                    organization = projectData.organization,
                    tasks = projectData.tasks,
                )
            )
    }

    override fun findByOrganizationAndNumber(project: Project): Project? {
        val builder = ProjectPokoBuilder()
        project.notify(builder)
        val projectData = builder.build()

        val maybeDoc = collection
            .whereEqualTo("projectNumber", projectData.projectNumber)
            .whereEqualTo("organization", projectData.organization)
            .limit(1)
            .get()
            .get()

        if (maybeDoc.isEmpty) {
            return null
        }

        return maybeDoc.map {
            val doc = it.toObject(DocumentData::class.java)
            Project(
                projectId = ProjectId(it.id),
                organization = Organization(doc.organization),
                projectNumber = ProjectNumber(doc.projectNumber),
                tasks = doc.tasks.map { taskId -> TaskId(taskId) },
            )
        }.first()
    }

    override fun findById(projectId: ProjectId): Project? {
        val documentSnapshot = collection.document(projectId.value).get().get()

        val doc = documentSnapshot.toObject(DocumentData::class.java) ?: return null
        return Project(
            projectId = projectId,
            organization = Organization(doc.organization),
            projectNumber = ProjectNumber(doc.projectNumber),
            tasks = doc.tasks.map { taskId -> TaskId(taskId) },
        )
    }
}

private data class DocumentData(
    var projectNumber: Int = -1,
    var organization: String = "",
    var tasks: List<String> = emptyList(),
)
