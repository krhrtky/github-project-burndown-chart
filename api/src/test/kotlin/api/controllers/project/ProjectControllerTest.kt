package api.controllers.project

import api.config.SecurityConfig
import api.usecases.project.create.ProjectCreateInputData
import api.usecases.project.create.ProjectCreateOutPutData
import api.usecases.project.create.ProjectCreateUseCase
import arrow.core.Either
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.StringSpec
import io.mockk.every
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody

@WebFluxTest
@Import(SecurityConfig::class)
@AutoConfigureWebTestClient
class ProjectControllerTest(
    @MockkBean
    private val projectCreateUseCase: ProjectCreateUseCase,
    private val webTestClient: WebTestClient,
): StringSpec({
    "Project が未作成の場合、作成した Project の ID をレスポンスする" {
        val requestBody = CreateProjectRequestBody("test-organization", 1)

        every {
            projectCreateUseCase
                .handle(ProjectCreateInputData("test-organization", 1))
        } returns Either.Right(ProjectCreateOutPutData("created-project-id"))

        webTestClient
            .post()
            .uri("/projects")
            .bodyValue(requestBody)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().is2xxSuccessful
            .expectBody<ProjectCreateOutPutData>().isEqualTo(ProjectCreateOutPutData("created-project-id"))
    }
})
