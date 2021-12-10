
fun main() {
    forLinesIn("day09/input.txt") {
        val map = it.map { it.toCharArray().map { it.digitToInt() }.toList() }.toList()
        //println(map)
        val lowPoints = lowPoints(map)
        println("Part 1: ${lowPoints.sumOf { it.value } + lowPoints.size}")

        val basins = basins(lowPoints, map)
        val largest3 = basins.sortedDescending().take(3)
        println("Part 2: ${largest3.reduce { a, b -> a * b }}")
    }
}

fun basins(lowPoints: List<ValuePoint>, map: List<List<Int>>): List<Int> {
    val result = ArrayList<Int>()
    for (lowPoint in lowPoints) {
        result.add(findBasinPoints(lowPoint.x, lowPoint.y, map, HashSet(setOf(lowPoint.toPoint()))).size)
    }
    return result
}

fun findBasinPoints(x: Int, y: Int, map: List<List<Int>>, acc: HashSet<Point>): Set<Point> {
    val points = adjacents(x, y, map).filter { ! acc.contains(it.toPoint()) }
    var result = acc
    for (p in points) {
        if (p.value < 9) {
            result.add(p.toPoint())
            result = HashSet(findBasinPoints(p.x, p.y, map, result))
        }
    }
    return result
}

fun lowPoints(map: List<List<Int>>): List<ValuePoint> {
    val result = ArrayList<ValuePoint>()
    for (y in map.indices) {
        for (x in 0 until map[0].size) {
            if (adjacents(x, y, map).map { it.value }.all { it > map[y][x] }) {
                result.add(ValuePoint(x, y, map[y][x]))
            }
        }
    }
    return result
}

fun adjacents(x: Int, y: Int, map: List<List<Int>>): List<ValuePoint> {
    val result = ArrayList<ValuePoint>()
    if (y > 0) {
        result.add(ValuePoint(x, y - 1, map[y - 1][x]))
    }
    if (y + 1 < map.size) {
        result.add(ValuePoint(x, y + 1, map[y + 1][x]))
    }
    if (x > 0) {
        result.add(ValuePoint(x - 1, y, map[y][x - 1]))
    }
    if (x + 1 < map[y].size) {
        result.add(ValuePoint(x + 1, y, map[y][x + 1]))
    }
    return result
}

data class ValuePoint(val x: Int, val y: Int, val value: Int) {
    fun toPoint(): Point {
        return Point(x, y)
    }
}

data class Point(val x: Int, val y: Int)