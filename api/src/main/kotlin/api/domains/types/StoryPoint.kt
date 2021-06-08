package api.domains.types

import api.domains.models.types.isFibonacci
import java.lang.IllegalArgumentException

class StoryPoint(private val value: Int) {
    init {
        if (!value.isFibonacci()) {
            throw IllegalArgumentException("${this.javaClass.simpleName} must be fibonacci. got $value")
        }
    }
    companion object {
        fun fromInt(maybeStoryPoint: Int): StoryPoint? =
            if (maybeStoryPoint.isFibonacci()) StoryPoint(maybeStoryPoint) else null
    }
    fun toInt(): Int = this.value
}
