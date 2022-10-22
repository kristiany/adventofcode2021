fun main() {
    forLinesIn("day25/input.txt") { seq ->
        val lines = seq.toList()
        val width = lines[0].length
        val height = lines.size
        var y = 0
        val ccs = HashMap<Pos, Char>()
        for (line in lines) {
            var x = 0
            for (c in line) {
                if (c != '.') {
                    ccs[Pos(x, y)] = c
                }
                ++x
            }
            ++y
        }

        var step = 0
        var moved = true
        var cur = HashMap(ccs)
        while (moved) {
            ++step
            moved = false
            var map = HashMap(cur)
            cur = HashMap<Pos, Char>()
            for (cc in map.keys) {
                if (map[cc] == '>') {
                    val nextX = Pos((cc.x + 1) % width, cc.y)
                    if (map[nextX] == null) {
                        cur[nextX] = map[cc]!!
                        moved = true
                    }
                    else {
                        cur[cc] = map[cc]!!
                    }
                } else {
                    cur[cc] = map[cc]!!
                }
            }
            map = HashMap(cur)
            cur = HashMap()
            for (cc in map.keys) {
                if (map[cc] == 'v') {
                    val nextY = Pos(cc.x, (cc.y + 1) % height)
                    if (map[nextY] == null) {
                        cur[nextY] = map[cc]!!
                        moved = true
                    } else {
                        cur[cc] = map[cc]!!
                    }
                } else {
                    cur[cc] = map[cc]!!
                }
            }
        }
        println("Part 1: $step")
    }

}
