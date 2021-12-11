fun main() {
    forLinesIn("day11/input.txt") {
        val map = it.map {
            it.toCharArray().map { it.digitToInt() }.toMutableList()
        }.toMutableList()

        val flashes = count(map)
        println("Part 1: $flashes")

        val step = findSynch(map)
        println("Part 2: $step")
    }
}

private fun findSynch(map: List<List<Int>>): Int {
    val energy = map.map { it.toMutableList() }.toMutableList()
    //debug(energy)
    var step = 1
    var notSynch = true
    while (notSynch) {
        simStep(step, energy)
        step++
        notSynch = ! energy.all { it.all { it == 0 } }
    }
    return step - 1
}

private fun count(map: List<List<Int>>): Int {
    val energy = map.map { it.toMutableList() }.toMutableList()
    var flashes = 0
    //debug(energy)
    for (i in 1..100) {
        flashes += simStep(i, energy)
    }
    return flashes
}

private fun simStep(step: Int, energy: MutableList<MutableList<Int>>): Int {
    println("Step $step")
    for (y in energy.indices) {
        for (x in energy[0].indices) {
            energy[y][x] += 1
        }
    }
    val flashPositions = HashSet<Pos>()
    val flashed = HashSet<Pos>()
    for (y in energy.indices) {
        for (x in energy[0].indices) {
            if (energy[y][x] > 9) {
                flashed.add(Pos(x, y))
                enerigize(x, y, energy, flashPositions)
            }
        }
    }
    var additionals = HashSet(flashPositions)
    while (additionals.isNotEmpty()) {
        val newAdditionals = HashSet<Pos>()
        for (additional in additionals.minus(flashed)) {
            val pos = Pos(additional.x, additional.y)
            if (!flashed.contains(pos)) {
                enerigize(additional.x, additional.y, energy, newAdditionals)
                flashed.add(pos)
            }
        }
        additionals = newAdditionals
    }
    var flashes = 0
    for (y in energy.indices) {
        for (x in energy[0].indices) {
            if (energy[y][x] > 9) {
                energy[y][x] = 0
                flashes++
            }
        }
    }
    //println("Flashes: $flashes")
    //debug(energy)
    return flashes
}

fun debug(energy: List<List<Int>>) {
    energy.forEach {
        println(it.joinToString(""))
    }
    println()
}

fun enerigize(x: Int, y: Int, energy: MutableList<MutableList<Int>>, flashes: HashSet<Pos>) {
    listOfNotNull(
        posOrNull(x - 1, y - 1, energy),
        posOrNull(x, y - 1, energy),
        posOrNull(x + 1, y - 1, energy),
        posOrNull(x - 1, y, energy),
        posOrNull(x + 1, y, energy),
        posOrNull(x - 1, y + 1, energy),
        posOrNull(x, y + 1, energy),
        posOrNull(x + 1, y + 1, energy)
    )
        .forEach {
            energy[it.y][it.x] += 1
            if (energy[it.y][it.x] > 9) {
                flashes.add(it)
            }
        }
}

private fun posOrNull(maybex: Int, maybey: Int, energy: List<List<Int>>): Pos? {
    return energy.getOrNull(maybey)?.let { xs ->
        xs.getOrNull(maybex)?.let { _ -> Pos(maybex, maybey) }
    }
}

data class Pos(val x: Int, val y: Int)
