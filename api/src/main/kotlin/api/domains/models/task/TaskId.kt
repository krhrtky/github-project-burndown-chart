package api.domains.models.task

data class TaskId(val value: String) {
    init {
        if (value.isEmpty()) {
            throw IllegalArgumentException("${this.javaClass.simpleName} must not be empty.")
        }
    }
}
