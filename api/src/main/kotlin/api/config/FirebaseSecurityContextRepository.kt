package api.config

import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.security.web.server.context.ServerSecurityContextRepository
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class FirebaseSecurityContextRepository(private val authenticationManager: ReactiveAuthenticationManager):
    ServerSecurityContextRepository {
    private val authTokenPrefix = "bearer "

    override fun save(exchange: ServerWebExchange, context: SecurityContext): Mono<Void> {
        throw UnsupportedOperationException("Not yet supported!")
    }

    override fun load(exchange: ServerWebExchange): Mono<SecurityContext> {
        val authHeader = exchange.request.headers.getFirst(HttpHeaders.AUTHORIZATION) ?: ""
        val token = authHeader.replace(authTokenPrefix, "", true)

        return if (
            authHeader.startsWith(authTokenPrefix, true) &&
            token.isNotBlank()
        ) {
            val auth = UsernamePasswordAuthenticationToken(token, token)
            authenticationManager.authenticate(auth).map { authentication: Authentication? ->
                SecurityContextImpl(
                    authentication
                )
            }
        } else {
            Mono.empty()
        }
    }
}
