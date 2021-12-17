
fun main() {
    // Test
    //val targetX = 20..30
    //val targetY = -10..-5

    // Input target area: x=169..206, y=-108..-68
    val targetX = 169..206
    val targetY = -108..-68

    var maxY = 0
    var goodInitialVels = 0
    for (x in 1..1000) {
        for (y in -1000..1000) {
            val trj = trajectory(Vel(x, y), targetX, targetY)
            val newY = trj?.maxOf { it.y } ?: 0
            if (newY > maxY) {
                maxY = newY
            }
            if (trj != null) {
                goodInitialVels++
                println(trj)
            }
        }
    }
    println("Part 1: $maxY")
    println("Part 2: $goodInitialVels")
}

private fun trajectory(initVel: Vel, targetX: IntRange, targetY: IntRange): List<Pos>? {
    val positions = ArrayList<Pos>()
    var current = Pos(0, 0)
    positions.add(current) // Start
    var vel = initVel
    val maxX = targetX.maxOrNull()!!
    val minY = targetY.minOrNull()!!
    while (!withinTarget(targetX, targetY, current)) {
        current = Pos(current.x + vel.x, current.y + vel.y)
        positions.add(current)
        vel = Vel(drag(vel.x), vel.y - 1)

        if (current.x > maxX || current.y < minY) {
            //println("Missed the target, $current, max x: $maxX, min y: $minY")
            return null
        }
    }
    return positions
}

private fun withinTarget(targetX: IntRange, targetY: IntRange, current: Pos)
= targetX.contains(current.x) && targetY.contains(current.y)

fun drag(x: Int): Int {
    if (x == 0) {
        return 0
    }
    return if (x < 0) x + 1 else x - 1
}

data class Vel(val x: Int, val y: Int)
