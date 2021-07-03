package api.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.context.ServerSecurityContextRepository
import reactor.core.publisher.Mono

@Configuration
@EnableWebFluxSecurity
class SecurityConfig(
    private val reactiveAuthenticationManager: ReactiveAuthenticationManager,
    private val serverSecurityContextRepository: ServerSecurityContextRepository,
) {
    @Bean
    fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain = http
        .csrf().disable()
        .cors().disable()
        .formLogin().disable()
        .httpBasic().disable()
        .authenticationManager(reactiveAuthenticationManager)
        .securityContextRepository(serverSecurityContextRepository)
        .exceptionHandling()
        .authenticationEntryPoint { swe, _ ->
            Mono.fromRunnable {
                swe.response.statusCode = HttpStatus.UNAUTHORIZED
                swe.response.headers.set("Access-Control-Allow-Origin", "*")
            }
        }
        .accessDeniedHandler { swe, _ ->
            Mono.fromRunnable {
                swe.response.statusCode = HttpStatus.FORBIDDEN
            }
        }
        .and()
        .authorizeExchange()
        .pathMatchers(HttpMethod.OPTIONS).permitAll()
        .pathMatchers("/v3/api-docs").permitAll()
        .anyExchange().authenticated()
        .and()
        .requestCache().disable()
        .build()
}

