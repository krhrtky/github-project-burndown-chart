package api.domains.models.task

import api.domains.types.StoryPoint

data class ResultStoryPoint(private val storyPoint: StoryPoint) {
    val value: Int
        get() = this.storyPoint.toInt()
}
