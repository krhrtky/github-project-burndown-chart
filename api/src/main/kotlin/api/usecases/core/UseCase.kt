package api.usecases.core

import arrow.core.Either

interface UseCase<InputData, OutputData, E: Exception> {
    fun handle(inputData: InputData): Either<E, OutputData>
}
