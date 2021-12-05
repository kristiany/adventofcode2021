import kotlin.math.max
import kotlin.math.min

fun main() {
    forLinesIn("day05/input.txt") {
        val lines = parse(it.toList())
        //println(lines)
        val map = toMap(lines, false)
        //println(map.filter { it.value > 1 })
        println("Part 1: ${ map.values.count{ it > 1 }}")

        val map2 = toMap(lines, true)
        //println(map2.filter { it.value > 1 })
        println("Part 2: ${ map2.values.count{ it > 1 }}")
    }
}

fun toMap(lines: List<Line>, mapDiagonal: Boolean): Map<Pair<Int, Int>, Int> {
    val map = HashMap<Pair<Int, Int>, Int>()
    lines.forEach {
        val minx = min(it.start.first, it.end.first)
        val miny = min(it.start.second, it.end.second)
        val maxx = max(it.start.first, it.end.first)
        val maxy = max(it.start.second, it.end.second)

        if (minx == maxx) {
            val x = it.start.first
            for (y in miny .. maxy) {
                map[Pair(x, y)] = (map[Pair(x, y)] ?: 0) + 1
            }
        }
        else if (miny == maxy) {
            val y = it.start.second
            for (x in minx .. maxx) {
                map[Pair(x, y)] = (map[Pair(x, y)] ?: 0) + 1
            }
        }
        // Diagonal 45 degrees only
        else if (mapDiagonal) {
            val startToEnd = it.start.first < it.end.first
            var y = if (startToEnd) it.start.second else it.end.second
            val incry =
                if (startToEnd && it.end.second < it.start.second || !startToEnd && it.start.second < it.end.second)
                    -1
                else
                    1
            for (x in minx .. maxx) {
                map[Pair(x, y)] = (map[Pair(x, y)] ?: 0) + 1
                y += incry
            }
        }
    }
    return map
}

fun parse(input: List<String>): List<Line> {
    val result = ArrayList<Line>()
    for (line in input) {
        val parts = line.split("->")
        val start = parts[0].trim().split(",").map { it.toInt() }
        val end = parts[1].trim().split(",").map { it.toInt() }
        result.add(Line(Pair(start[0], start[1]), Pair(end[0], end[1])))
    }
    return result
}

data class Line(val start: Pair<Int, Int>, val end: Pair<Int, Int>)
