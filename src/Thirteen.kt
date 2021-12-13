
fun main() {
    forLinesIn("day13/input.txt") {
        val (coords, folds) = parseInput(it.toList())
        println(coords)
        println(folds)

        val folded = fold(folds.first(), coords)
        println("Part 1: ${folded.size}")

        var folded2 = coords.toSet()
        for (fold in folds) {
            folded2 = fold(fold, folded2)
        }
        println("Part 2:")
        val minx = folded2.minOf { it.x }
        val maxx = folded2.maxOf { it.x }
        val miny = folded2.minOf { it.y }
        val maxy = folded2.maxOf { it.y }
        for (y in miny .. maxy) {
            var row = ""
            for (x in minx .. maxx) {
                row += if (folded2.contains(Pos(x, y))) '#' else ' '
            }
            println(row)
        }
    }
}

private fun fold(fold: Fold, coords: Collection<Pos>): Set<Pos> {
    if (fold.axis == "y") {
        val upper = coords.filter { it.y < fold.coord }.toMutableSet()
        val lower = coords.filter { it.y > fold.coord }
            .map { Pos(it.x, fold.coord - (it.y - fold.coord)) }
            .toSet()
        return upper.plus(lower)
    }
    // Else x
    val left = coords.filter { it.x < fold.coord }.toMutableSet()
    val right = coords.filter { it.x > fold.coord }
        .map { Pos(fold.coord - (it.x - fold.coord), it.y) }
        .toSet()
    return left.plus(right)
}

fun parseInput(input: List<String>): Pair<List<Pos>, List<Fold>> {
    val coords = input.filter { it.isNotBlank() && ! it.startsWith("fold") }
        .map { it.split(",") }
        .map { Pos(it[0].toInt(), it[1].toInt()) }
        .toList()
    val folds = input.filter { it.isNotBlank() && it.startsWith("fold") }
        .map { it.split(" ")[2].split("=") }
        .map { Fold(it[0], it[1].toInt()) }
        .toList()
    return Pair(coords, folds)
}

data class Fold(val axis: String, val coord: Int)

