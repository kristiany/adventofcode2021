val lefties = setOf('(', '[', '{', '<')
val expectLeft = mapOf(
    ')' to '(',
    ']' to '[',
    '}' to '{',
    '>' to '<',
)
val expectRight = mapOf(
    '(' to ')',
    '[' to ']',
    '{' to '}',
    '<' to '>',
)
val errorScores = mapOf(
    ')' to 3,
    ']' to 57,
    '}' to 1197,
    '>' to 25137
)

val completionScores = mapOf(
    ')' to 1,
    ']' to 2,
    '}' to 3,
    '>' to 4
)

fun main() {
    forLinesIn("day10/input.txt") {
        val input = it.toList()
        val (completes, illegals) = compile(input)
        println("Part 1: ${illegals.sumOf { errorScores[it]!! }}")

        val scores = completes.map { completeScore(it) }.sorted()
        println("Part 2: ${scores[scores.size / 2]}")
    }
}

fun completeScore(line: List<Char>): Long {
    var total = 0L
    for (c in line) {
        total *= 5L
        total += completionScores[c]!!
    }
    return total
}

fun compile(input: List<String>): Pair<List<List<Char>>, List<Char>> {
    val illegals = ArrayList<Char>()
    val completes = ArrayList<List<Char>>()
    for (line in input) {
        val stack = ArrayDeque<Char>()
        var error = false
        for (c in line.toCharArray()) {
            if (lefties.contains(c)) {
                stack.addLast(c)
            } else {
                if (stack.isEmpty()) {
                    println("Empty stack, expected nothing found $c")
                }
                val top = stack.removeLast()
                if (expectLeft[c] != top) {
                    println("$line expected ${expectRight[top]} found $c")
                    illegals.add(c)
                    error = true
                    break
                }
            }
        }
        if (!error && stack.isNotEmpty()) {
            val complete = ArrayList<Char>()
            for (top in stack.reversed()) {
                complete.add(expectRight[top]!!)
            }
            completes.add(complete)
            println("$line complete ${complete.joinToString("")}")
        }
    }
    return Pair(completes, illegals)
}
