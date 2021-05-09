package api.domains.models.project

data class Id(val value: String) {
    init {
        if (value.isEmpty()) {
            throw IllegalArgumentException("${this.javaClass.simpleName} must not be empty.")
        }
    }
}
