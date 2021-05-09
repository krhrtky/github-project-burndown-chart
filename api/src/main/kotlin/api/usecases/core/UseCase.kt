package api.usecases.core

import arrow.core.Either

interface UseCase<InputData, OutputData> {
    fun handle(inputData: InputData): Either<Exception, OutputData>
}
