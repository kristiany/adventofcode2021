
fun main() {
    forLinesIn("day20/input.txt") {
        val input = it.toList()
        val algo = input.first().trim().map { it == '#' }
        val map = HashMap<Pos, Boolean>()
        var ly = 0
        val mapInput = input.subList(2, input.size)
        for (line in mapInput) {
            for (x in line.indices) {
                if (line[x] == '#') {
                    map[Pos(x, ly)] = true
                }
            }
            ly++
        }
        //println(algo)
        //println(map)
        var e = map
        val width = input[2].length
        val height = mapInput.size
        var edges = Pair(Pos(-1, -1), Pos(width + 1, height + 1))
        //debug(e)
        for (i in 1..2) {
            val res = enhance(i, e, algo, edges)
            e = res.first
            edges = res.second
            println("$i")
            //debug(e)
        }
        val view = e.filter { within(it.key, edges) }.toMap()
        debug(view)
        println("Part 1: ${view.values.count { it }}")

        for (i in 3..50) {
            val res = enhance(i, e, algo, edges)
            e = res.first
            edges = res.second
            //println(edges)
            println("$i")
            //debug(e)
        }
        val view2 = e.filter { within(it.key, edges) }.toMap()
        debug(view2)
        println("Part 2: ${view2.values.count { it }}")
    }

}

fun within(pos: Pos, edges: Pair<Pos, Pos>): Boolean {
    return pos.x >= edges.first.x
            && pos.x <= edges.second.x
            && pos.y >= edges.first.y
            && pos.y <= edges.second.y

}

private fun enhance(step: Int,
                    map: HashMap<Pos, Boolean>,
                    algo: List<Boolean>,
                    edges: Pair<Pos, Pos>): Pair<HashMap<Pos, Boolean>, Pair<Pos, Pos>> {
    val enhanced = HashMap<Pos, Boolean>()
    val exp = 1
    val minx = edges.first.x - exp
    val maxx = edges.second.x + exp
    val miny = edges.first.y - exp
    val maxy = edges.second.y + exp
    var topleftX = 0
    var topleftY = 0
    var rightbottomX = 0
    var rightbottomY = 0
    for (y in miny..maxy) {
        for (x in minx..maxx) {
            val nr = value(x, y, map, step, edges)
            //println("$nr -> ${algo[nr]}")
            if (algo[nr]) {
                enhanced[Pos(x, y)] = true
                if (x < topleftX) {
                    topleftX = x
                }
                if (x > rightbottomX) {
                    rightbottomX = x
                }
                if (y < topleftY) {
                    topleftY = y
                }
                if (y > rightbottomY) {
                    rightbottomY = y
                }
            }
        }
    }
    return Pair(enhanced,
        Pair(Pos(topleftX, topleftY), Pos(rightbottomX, rightbottomY)))
}

private fun debug(map: Map<Pos, Boolean>) {
    val minx = map.keys.minOf { it.x } - 1
    val maxx = map.keys.maxOf { it.x } + 1
    val miny = map.keys.minOf { it.y } - 1
    val maxy = map.keys.maxOf { it.y } + 1
    for (y in miny..maxy) {
        var line = ""
        for (x in minx..maxx) {
            line += if (map[Pos(x, y)] == true) '#' else '.'
        }
        println(line)
    }
}

fun value(x: Int, y: Int, map: Map<Pos, Boolean>, step: Int, edges: Pair<Pos, Pos>): Int {
    var bin = ""
    for (vy in y - 1..y + 1) {
        for (vx in x - 1..x + 1) {
            bin += if (map[Pos(vx, vy)] == true || (step % 2 == 0 && ! within(Pos(vx, vy), edges))) "1" else "0"
        }
    }
    //println(bin)
    return bin.toInt(2)
}
