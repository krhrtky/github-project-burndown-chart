package api.domains.models.project

data class ProjectNumber(val value: Int) {
    init {
        if(value < 0) {
            throw IllegalArgumentException("${this.javaClass.simpleName} must be positive number.")
        }
    }
}
