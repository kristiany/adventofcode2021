
fun main() {
    forLinesIn("day06/input.txt") {
        val fish = it.first().split(",").map { it.toInt() }.toList()
        println(fish)
        val gen = sim(fish, 80)
        println("Part 1: ${gen.sum()}")

        val gen2 = sim(fish, 256)
        println("Part 2: ${gen2.sum()}")
    }
}

private fun sim(fish: List<Int>, days: Int): ArrayList<Long> {
    val gen = ArrayList<Long>((0..8).map { 0L }.toList())
    for (age in fish) {
        gen[age] = gen[age] + 1L
    }
    for (day in 1..days) {
        val restarts = gen[0]
        for (age in 1..8) {
            gen[age - 1] = gen[age]
        }
        gen[8] = restarts
        gen[6] += restarts
        //println("After day $day: $gen2")
    }
    return gen
}
