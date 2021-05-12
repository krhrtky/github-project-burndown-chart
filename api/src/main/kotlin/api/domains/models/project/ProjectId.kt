package api.domains.models.project

data class ProjectId(val value: String) {
    init {
        if (value.isEmpty()) {
            throw IllegalArgumentException("${this.javaClass.simpleName} must not be empty.")
        }
    }
}
