package api.controllers.validators.fibonacci

import api.domains.models.types.isFibonacci
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import javax.validation.ReportAsSingleViolation
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@ReportAsSingleViolation
@Constraint(validatedBy = [FibonacciValidator::class])
annotation class Fibonacci(
    val message: String = "message",
    val groups: Array<KClass<out Any>> = [],
    val payload: Array<KClass<out Payload>> = []
)

class FibonacciValidator: ConstraintValidator<Fibonacci, Int> {
    override fun initialize(constraintAnnotation: Fibonacci) {}

    override fun isValid(value: Int?, context: ConstraintValidatorContext?): Boolean {
        if (value == null) return false
        return value.isFibonacci()
    }
}
