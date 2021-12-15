import kotlin.math.abs

fun main() {
    forLinesIn("day15/input.txt") {
        val map = it.map { it.map { it.digitToInt() }.toList() }.toList()

        val risk = findRisk(map)
        println("Part 1: $risk")

        val extendedMap = extend(map)
        //extendedMap.forEach { println(it.joinToString("")) }
        val risk2 = findRisk(extendedMap)
        println("Part 2: $risk2")
    }
}

fun extend(map: List<List<Int>>): List<List<Int>> {
    val result = ArrayList<List<Int>>()
    for (ywise in 0..4) {
        for (y in map.indices) {
            val xs = ArrayList<Int>()
            for (xwise in 0..4) {
                xs.addAll(map[y].map {
                    val newRisk = it + ywise + xwise
                    if (newRisk > 9) newRisk - 9 else newRisk
                })
            }
            result.add(xs)
        }
    }
    return result
}

fun findRisk(map: List<List<Int>>): Int {
    val traverse = HashSet<Pos>()
    val start = Pos(0, 0)
    traverse.add(start)
    val end = Pos(map[0].size - 1, map.size - 1)
    val backConnections = HashMap<Pos, Pos>()
    val riskScore = HashMap<Pos, Int>()
    riskScore[start] = 0
    val totalScore = HashMap<Pos, Int>()
    totalScore[start] = 0
    while (traverse.isNotEmpty()) {
        val current = findBestStep(traverse, totalScore)
        traverse.remove(current)
        // Found it!
        if (current == end) {
            return riskScore[backConnections[current]!!]!! + riskScore(end, map)
        }
        for (adj in adjacents(current, map)) {
            val w = riskScore[current]!! + riskScore(adj, map)
            if (w < (riskScore[adj] ?: 10000000)) {
                backConnections[adj] = current
                riskScore[adj] = w
                totalScore[adj] = w + distance(start, adj)
                traverse.add(adj)
            }
        }
    }
    return -1
}

fun riskScore(pos: Pos, map: List<List<Int>>) = map[pos.y][pos.x]

fun adjacents(current: Pos, map: List<List<Int>>): Set<Pos> {
    val result = HashSet<Pos>()
    if (current.x > 0) {
        result.add(Pos(current.x - 1, current.y))
    }
    if (current.x + 1 < map[0].size) {
        result.add(Pos(current.x + 1, current.y))
    }
    if (current.y > 0) {
        result.add(Pos(current.x, current.y - 1))
    }
    if (current.y + 1 < map.size) {
        result.add(Pos(current.x, current.y + 1))
    }
    return result
}

fun findBestStep(paths: Set<Pos>, score: Map<Pos, Int>): Pos {
    return paths.map { Pair(it, score[it]!!) }
        .minByOrNull { it.second }!!
        .first
}

// Taxicab/Manhattan distance
fun distance(pos1: Pos, pos2: Pos): Int {
    return abs(pos2.x - pos1.x) + abs(pos2.y - pos1.y)
}
