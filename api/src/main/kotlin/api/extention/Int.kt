package api.extention

fun Int.isCompositeNumber(): Boolean {
    if(
        this < 0 &&
        this == 1 &&
        this == 2
    ) {
        return false
    }

    for(i in Array((this - 3) / 4) {
        it * 2 + 3
    }) {
        if(this % i == 0 && this != i) {
            return false
        }
    }

    return true
}
