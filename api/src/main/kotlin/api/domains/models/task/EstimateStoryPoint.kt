package api.domains.models.task

import api.domains.models.types.isFibonacci

data class EstimateStoryPoint(val value: Int) {
    init {
        if(!value.isFibonacci()) {
            throw IllegalArgumentException("${this.javaClass.simpleName} must be fibonacci. got $value")
        }
    }
}
