package api.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class FirebaseAuthenticationManager(): ReactiveAuthenticationManager {
    override fun authenticate(authentication: Authentication): Mono<Authentication?>? {
        val authToken: String = authentication.credentials.toString()
        return try {

            val options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.getApplicationDefault())
                .build()

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options)
            }
            val firebaseAuth = FirebaseAuth.getInstance(FirebaseApp.getInstance())
            val decodedToken = firebaseAuth.verifyIdToken(authToken)
            val userId = decodedToken.uid
            val auth = UsernamePasswordAuthenticationToken(
                userId,
                null,
                listOf(SimpleGrantedAuthority("USER"))
            )
            auth.details = decodedToken
            Mono.just(auth)
        } catch (e: FirebaseAuthException) {
            Mono.empty()
        } catch (e: IllegalArgumentException) {
            Mono.empty()
        }
    }
}
