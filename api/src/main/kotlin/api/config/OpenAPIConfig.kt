package api.config

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.security.SecurityScheme
import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.headers.Header
import io.swagger.v3.oas.models.media.StringSchema
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@SecurityScheme(
    name = "BearerAuth",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "Firebase Authentication",
    scheme = "Bearer"
)
class OpenAPIConfig {
    @Bean
    fun getOpenAPI(): OpenAPI = OpenAPI()
        .components(
            Components()
                .addHeaders(
                    "Authorization",
                    Header().description("Auth header").schema(StringSchema()))
        )
}
