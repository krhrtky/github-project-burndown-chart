package api.domains.models.task

data class ProjectCardId(val value: Int) {
    init {
        if(value < 0) {
            throw IllegalArgumentException("${this.javaClass.simpleName} must be positive number.")
        }
    }
}
