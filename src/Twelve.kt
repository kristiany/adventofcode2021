
fun main() {
    forLinesIn("day12/input.txt") {
        val caves = parseCaves(it)
        println(caves.map { "${it.key} -> ${it.value}" }.joinToString("\n"))

        val paths = findPaths(caves)
        println("Part 1: $paths")

        val paths2 = findPaths(caves, true)
        println("Part 2: $paths2")
    }
}

fun findPaths(caves: Map<String, List<String>>, twiceVisits: Boolean = false): Int {
    val visited = HashSet<String>()
    val pathsResult = ArrayList<List<String>>()
    paths(caves, "start", visited, ArrayList(), pathsResult, twiceVisits)
    //println(pathsResult.joinToString("\n"))
    return pathsResult.size
}

fun paths(caves: Map<String, List<String>>,
          node: String,
          visited: HashSet<String>,
          acc: ArrayList<String>,
          pathsResult: ArrayList<List<String>>,
          twiceVisits: Boolean) {
    if (node != "start" && node != "end" && node.none { it.isUpperCase() }) {
        visited.add(node)
    }
    acc.add(node)
    if (node == "end") {
        pathsResult.add(acc)
        return
    }
    for (child in caves[node] ?: emptyList()) {
        if (! visited.contains(child)) {
            paths(caves, child, HashSet(visited), ArrayList(acc), pathsResult, twiceVisits)
        }
        else if (twiceVisits) {
            val updated = HashSet(visited)
            updated.remove(child)
            paths(caves, child, updated, ArrayList(acc), pathsResult, false)
        }
    }
}

private fun parseCaves(it: Sequence<String>): Map<String, List<String>> {
    val caves = it.map {
        val connection = it.split("-")
        connection[0] to connection[1]
    }
    val result = HashMap<String, ArrayList<String>>()
    for (connection in caves) {
        val from = connection.first
        val to = connection.second
        if (result[from] == null) {
            result[from] = ArrayList()
        }
        if (from != "end" && to != "start") {
            result[from]!!.add(to)
        }
        if (from != "start" && to != "end") {
            if (result[to] == null) {
                result[to] = ArrayList()
            }
            result[to]!!.add(from)
        }
    }
    return result
}


