
fun main() {
    forLinesIn("day02/input.txt") {
        val input = it.toList()
        val (horizontal, depth) = move(input)
        println("Part 1: ${horizontal * depth}")

        val (horizontal2, depth2) = move2(input)
        println("Part 2: ${horizontal2 * depth2}")
    }
}

fun move(ins: List<String>): Pair<Int, Int> {
    var horizontal = 0
    var depth = 0
    for (move in ins) {
        val steps = move.split(" ")[1].trim().toInt()
        if (move.startsWith("forward")) {
            horizontal += steps
        }
        else if (move.startsWith("down")) {
            depth += steps
        }
        else if (move.startsWith("up")) {
            depth -= steps
        }
    }
    return Pair(horizontal, depth)
}

fun move2(ins: List<String>): Pair<Int, Int> {
    var horizontal = 0
    var depth = 0
    var aim = 0
    for (move in ins) {
        val steps = move.split(" ")[1].trim().toInt()
        if (move.startsWith("forward")) {
            horizontal += steps
            depth += (steps * aim)
        }
        else if (move.startsWith("down")) {
            aim += steps
        }
        else if (move.startsWith("up")) {
            aim -= steps
        }
    }
    return Pair(horizontal, depth)
}
