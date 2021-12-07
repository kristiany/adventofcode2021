import kotlin.math.abs

fun main() {
    forLinesIn("day07/input.txt") {
        val positions = it.first().split(",").map { it.toInt() }.toList()
        println(positions)
        val costs = cost(positions, f={ crabPos, testPos -> abs(crabPos - testPos) })
        println("Part 1: ${costs.minOrNull()}")

        val costs2 = cost(positions, f={ crabPos, testPos -> fuel(abs(crabPos - testPos)) })
        println("Part 2: ${costs2.minOrNull()}")
    }
}

fun fuel(steps: Int): Int {
    if (steps == 0) {
        return 0
    }
    return (1..steps).sum()
}

fun cost(positions: List<Int>, f: (actualPos: Int, testPos: Int) -> Int): List<Int> {
    val min = positions.minOf { it }
    val max = positions.maxOf { it }
    val costs = ArrayList<Int>()
    for (test in min .. max) {
        val cost = positions.sumOf { f(it, test) }
        //println("pos $test cost $cost")
        costs.add(cost)
    }
    return costs
}