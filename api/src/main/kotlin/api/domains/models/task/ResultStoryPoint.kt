package api.domains.models.task

import api.extention.isFibonacci

data class ResultStoryPoint(val value: Int) {
    init {
        if(!value.isFibonacci()) {
            throw IllegalArgumentException("${this.javaClass.simpleName} must be fibonacci. got $value")
        }
    }
}
