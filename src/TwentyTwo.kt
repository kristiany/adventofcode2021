import kotlin.math.max
import kotlin.math.min

fun main() {
    forLinesIn("day22/input.txt") {
        val ranges = parseRanges(it.toList())
        //println(ranges)

        val size = calc2(ranges, true)
        println("Part 1: $size")

        val size2 = calc2(ranges)
        println("Part 2: ${size2}")
    }
}

private fun calc2(ranges: List<Ranges>, limit50: Boolean = false): Long {
    val processed = ArrayList<Ranges>()
    val adjustedRanges = if (limit50) ranges
        .filter {
            !(it.x.first > 50 || it.x.last < -50)
                    && !(it.y.first > 50 || it.y.last < -50)
                    && !(it.z.first > 50 || it.z.last < -50)
        }
        .map {
            Ranges(
                it.on,
                IntRange(max(it.x.first, -50), min(it.x.last, 50)),
                IntRange(max(it.y.first, -50), min(it.y.last, 50)),
                IntRange(max(it.z.first, -50), min(it.z.last, 50))
            )
        } else ranges

    val reversed = adjustedRanges.reversed()
    processed.add(reversed.first())
    for (cur in reversed.subList(1, reversed.size)) {
        //println(r)
        var sections = listOf(cur)
        for (p in processed) {
            sections = ArrayList(noOverlapping(sections, p))
        }
        if (sections.size > 0) {
            processed.addAll(sections)
            //println("$r Overlap with $p: on ${overlap.second} reduction $overlap, total $overlapReduction")
        }
    }

    return processed.filter { it.on }.sumOf { it.size() }// - processed.filter { ! it.on }.map { it.size() }.sum()
}

fun noOverlapping(sections: List<Ranges>, p: Ranges): List<Ranges> {
    return sections.flatMap { withNoOverlapsOf(it, p) }
}

fun withNoOverlapsOf(cur: Ranges, p: Ranges): List<Ranges> {
    if (cur.x.last < p.x.first || cur.x.first > p.x.last) {
        return listOf(cur)
    }
    if (cur.y.last < p.y.first || cur.y.first > p.y.last) {
        return listOf(cur)
    }
    if (cur.z.last < p.z.first || cur.z.first > p.z.last) {
        return listOf(cur)
    }
    val result = ArrayList<Ranges>()
    val y = cur.y
    val z = cur.z
    val xs = filterCurrentRange(cur.x, p.x)
    xs.forEach { result.add(Ranges(cur.on, it, y, z)) }
    val ys = filterCurrentRange(cur.y, p.y)
    val minx = filterMin(xs, cur.x)
    val maxx = filterMax(xs, cur.x)
    ys.forEach { result.add(Ranges(cur.on, minx..maxx, it, z)) }
    val zs = filterCurrentRange(cur.z, p.z)
    val miny = filterMin(ys, cur.y)
    val maxy = filterMax(ys, cur.y)
    zs.forEach { result.add(Ranges(cur.on, minx..maxx, miny..maxy, it)) }

    return result
}

private fun filterMax(rs: List<IntRange>, cur: IntRange): Int {
    val maxr = if (rs.size == 1) if (rs.first().last < cur.last) cur.last else rs.first().first - 1
    else if (rs.size == 2) rs.get(1).first - 1
    else cur.last
    return maxr
}

private fun filterMin(rs: List<IntRange>, cur: IntRange): Int {
    val minr = if (rs.size == 1) if (rs.first().first > cur.first) cur.first else rs.first().last + 1
    else if (rs.size == 2) rs.get(0).last + 1
    else cur.first
    return minr
}

fun filterCurrentRange(cur: IntRange, p: IntRange): List<IntRange> {
    // cur is contained in p
    if (cur.first > p.first && cur.last < p.last) {
        return listOf()
    }
    // p is contained in cur
    if (p.first > cur.first && p.last < cur.last) {
        return listOf(IntRange(cur.first, p.first - 1), IntRange(p.last + 1, cur.last))
    }
    // cur overlaps from the right
    if (cur.first >= p.first && cur.last > p.last) {
        return listOf(IntRange(p.last + 1, cur.last))
    }
    // p overlaps from the right
    if (p.first > cur.first && p.first <= cur.last) {
        return listOf(IntRange(cur.first, p.first - 1))
    }
    return listOf()
}

fun parseRanges(input: List<String>): List<Ranges> {
    val result = ArrayList<Ranges>()
    for (line in input) {
        val on = line.startsWith("on")
        val parts = line.split(" ")[1].split(",")
        val x = singleRange(parts[0])
        val y = singleRange(parts[1])
        val z = singleRange(parts[2])
        result.add(Ranges(on, x, y, z))
    }
    return result
}

private fun singleRange(input: String): IntRange {
    val nrs = input.split("=")[1].split("..")
    return IntRange(nrs[0].toInt(), nrs[1].toInt())
}

data class Ranges(val on: Boolean, val x: IntRange, val y: IntRange, val z: IntRange) {
    fun size(): Long {
        return x.count().toLong() * y.count() * z.count()
    }

    fun contains(ix: Int, iy: Int, iz: Int): Boolean {
        return x.contains(ix) && y.contains(iy) && z.contains(iz)
    }
}
