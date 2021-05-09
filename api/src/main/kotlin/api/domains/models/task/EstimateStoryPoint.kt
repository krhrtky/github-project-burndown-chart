package api.domains.models.task

import api.extention.isCompositeNumber

data class EstimateStoryPoint(val value: Int) {
    init {
        if(value.isCompositeNumber()) {
            throw IllegalArgumentException("${this.javaClass.simpleName} must not be prime number.")
        }
    }
}
