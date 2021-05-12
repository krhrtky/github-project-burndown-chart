package api.usecases.core

import arrow.core.Either
import reactor.core.publisher.Mono

interface UseCase<InputData, OutputData, E> {
    fun handle(inputData: InputData): Mono<Either<E, OutputData>>
}
