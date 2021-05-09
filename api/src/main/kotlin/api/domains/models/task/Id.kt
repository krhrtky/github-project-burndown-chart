package api.domains.models.task

data class Id(val value: String) {
    init {
        if (value.isEmpty()) {
            throw IllegalArgumentException("${this.javaClass.simpleName} must not be empty.")
        }
    }
}
