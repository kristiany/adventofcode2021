import kotlin.math.abs

fun main() {
    forLinesIn("day19/input.txt") {
        val scanners = parseScanners(it.toList())
        scanners.entries.forEach { println("Scanner: ${it.key}, beacons: ${it.value}") }

        val beacons = HashSet(scanners[0]!!)
        val rotations = HashMap<Int, List<List<Coord>>>()
        for (i in 1 until scanners.size) {
            rotations[i] = allRotations2(scanners[i]!!)
        }
        val completed = HashSet<Int>()
        completed.add(0)
        val foundAndRotated = HashMap<Int, List<Coord>>()
        foundAndRotated[0] = scanners[0]!!.toList()
        val scannerPos = HashMap<Int, Coord>()
        val offsets = HashMap<Int, V>()
        offsets[0] = V(0, 0, 0)
        while (completed.size < scanners.size) {
            var anyMatch = false
            val nextScanners = (1 until scanners.size).minus(completed)
            println("Next scanners $nextScanners")
            for (i in nextScanners) {
                //val other = scanners[i]!!
                search@ for (rotation in rotations[i]!!) {
                    for (prevFound in foundAndRotated.keys) {
                        val diff = matches(foundAndRotated[prevFound]!!, rotation)
                        if (diff != null) {
                            println("Match $prevFound on $i! $diff")
                            foundAndRotated[i] = rotation.toList()
                            val fullOffset = offsets[prevFound]!!.add(diff)
                            offsets[i] = fullOffset
                            val translated = rotation.map { it.add(fullOffset) }.toList()
                            beacons.addAll(translated)
                            scannerPos[i] = Coord(fullOffset.x, fullOffset.y, fullOffset.z)
                            completed.add(i)
                            anyMatch = true
                            break@search
                        }
                    }
                }
            }
            if (! anyMatch) {
                println("Premature break, no further matches")
                break
            }
        }
        println("Part 1: ${beacons.size}")

        //scannerPos.forEach { println("${it.key} to ${it.value}") }
        var maxDistance = 0
        for (a in scannerPos.keys) {
            for (b in scannerPos.keys.minus(a)) {
                val sa = scannerPos[a]!!
                val sb = scannerPos[b]!!
                val dist = abs(sa.x - sb.x) + abs(sa.y - sb.y) + abs(sa.z - sb.z)
                if (dist > maxDistance) maxDistance = dist
            }
        }
        println("Part 2: $maxDistance")
    }
}

// Inspired by https://github.com/dclamage/aoc2021-rust/blob/main/Day19/src/main.rs
fun matches(world: Collection<Coord>, other: List<Coord>): V? {
    val pairs = world.flatMap { wp -> other.map { wp to it } }
    for (pair in pairs) {
        val translation = pair.first.diff(pair.second)
        val matches = world.count { point ->
            other.map { it.add(translation) }
                .any { it == point }
        }
        if (matches > 11) {
            println("${pair.first} == ${pair.second}")
            return translation
        }
    }
    return null
}

// Stolen from https://github.com/DarthGandalf/advent-of-code/blob/master/2021/solutions/day19.ts
fun allRotations2(points: List<Coord>): List<List<Coord>> {
    val result = ArrayList<List<Coord>>()
    val rotations = listOf(
        {p :Coord -> Coord(p.x, p.y, p.z)},
        {p :Coord -> Coord(p.x, -p.y, -p.z)},
        {p :Coord -> Coord(p.x, p.z, -p.y)},
        {p :Coord -> Coord(p.x, -p.z, p.y)},

        {p :Coord -> Coord(-p.x, -p.y, p.z)},
        {p :Coord -> Coord(-p.x, p.y, -p.z)},
        {p :Coord -> Coord(-p.x, p.z, p.y)},
        {p :Coord -> Coord(-p.x, -p.z, -p.y)},

        {p :Coord -> Coord(p.y, p.z, p.x)},
        {p :Coord -> Coord(p.y, -p.z, -p.x)},
        {p :Coord -> Coord(p.y, p.x, -p.z)},
        {p :Coord -> Coord(p.y, -p.x, p.z)},

        {p :Coord -> Coord(-p.y, -p.z, p.x)},
        {p :Coord -> Coord(-p.y, p.z, -p.x)},
        {p :Coord -> Coord(-p.y, -p.x, -p.z)},
        {p :Coord -> Coord(-p.y, p.x, p.z)},

        {p :Coord -> Coord(p.z, p.x, p.y)},
        {p :Coord -> Coord(p.z, -p.x, -p.y)},
        {p :Coord -> Coord(p.z, p.y, -p.x)},
        {p :Coord -> Coord(p.z, -p.y, p.x)},

        {p :Coord -> Coord(-p.z, -p.x, p.y)},
        {p :Coord -> Coord(-p.z, p.x, -p.y)},
        {p :Coord -> Coord(-p.z, p.y, p.x)},
        {p :Coord -> Coord(-p.z, -p.y, -p.x)}
    )
    for (rotation in rotations) {
        result.add(points.map(rotation))
    }
    return result
}

fun parseScanners(input: List<String>): Map<Int, List<Coord>> {

    var beacons = ArrayList<Coord>()
    var scannerNr = 0
    val result = HashMap<Int, List<Coord>>()
    for (line in input) {
        if (line.startsWith("---")) {
            scannerNr = line.split("scanner ")[1].split(" ")[0].trim().toInt()
        }
        else if (line.isBlank()) {
            result[scannerNr] = beacons
            beacons = ArrayList()
        }
        else {
            beacons.add(Coord(line.split(",").map { it.toInt() }.toList()))
        }
    }
    // Final part
    result[scannerNr] = beacons
    return result
}


data class Coord(val x: Int, val y: Int, val z: Int) {
    constructor(cs: List<Int>): this(cs[0], cs[1], cs[2])

    fun diff(o: Coord): V {
        return V(o, this)
    }

    fun add(v: V): Coord {
        return Coord(x + v.x, y + v.y, z + v.z)
    }

    override fun toString(): String {
        return "($x,$y,$z)"
    }
}

data class V(val x: Int, val y: Int, val z: Int) {
    constructor(a: Coord, b: Coord): this(b.x - a.x, b.y - a.y, b.z - a.z)

    fun add(v: V): V {
        return V(x + v.x, y + v.y, z + v.z)
    }
}
