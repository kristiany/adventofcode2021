fun main() {
    forLinesIn("day01/input.txt") {
        val depths = it.toList().map { it.toInt() }
        val count = depths.mapIndexed { index, d -> check(index, d, depths) }.sum()
        println("Part 1: $count increases")

        val windowSums = depths.windowed(3).map { it.sum() }
        val count2 = windowSums.mapIndexed { index, d -> check(index, d, windowSums) }.sum()
        println("Part 2: $count2 increases")
    }
}

private fun check(index: Int, d: Int, depths: List<Int>) =
    if (index > 0 && d > depths[index - 1]) 1 else 0
