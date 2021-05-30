package api.config

import io.swagger.v3.oas.models.OpenAPI
import org.springdoc.core.customizers.OpenApiCustomiser
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenAPIConfig {

    @Bean
    fun nullableCustomiser(): OpenApiCustomiser? {
        return OpenApiCustomiser { openAPI: OpenAPI ->
            openAPI.servers.forEach {
                if (!it.url.contains("localhost")) {
                    it.url = it.url.replace("http", "https")
                }
            }
        }
    }
}
