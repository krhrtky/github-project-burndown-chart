package api.domains.models.task

data class ProjectCardId(val value: String) {
    init {
        if(value.isBlank()) {
            throw IllegalArgumentException("${this.javaClass.simpleName} must not be blank.")
        }
    }
}
