package api.extention

fun Int.isFibonacci(): Boolean {
    var first = 0
    var second = 1

    if (this == 0) {
        return true
    }
    var num = 0

    while (this > num) {
        num = first + second
        first = second
        second = num

        if (num == this) {
            return true
        }
    }

    return false
}
