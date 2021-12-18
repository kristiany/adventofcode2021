import java.lang.IllegalStateException
import kotlin.math.ceil

fun main() {
    forLinesIn("day18/input.txt") {
        val lines = parsePairs(it.toList())
        //lines.forEach { println(it) }

        val linesCopy = lines.map { it.map { Nr(it.value, it.depth) }.toList() }.toList()
        var acc = linesCopy.first().toMutableList()
        for (line in linesCopy.subList(1, linesCopy.size)) {
            //println("------------")
            acc.addAll(line)
            acc.forEach { it.increaseDepth() }
            //println("added $acc")
            acc = reduce(acc).toMutableList()
            //println("after $acc")
        }
        val magnitude = magnitude(acc)
        println("Part 1: $magnitude")

        var maxMagnitude = 0
        val linesCopy2 = lines.map { it.map { Nr(it.value, it.depth) }.toList() }.toList()
        for (line1 in linesCopy2) {
            for (line2 in linesCopy2.filter { it != line1 }) {
                val magnitude2 = magnitudeOfSumOf(line1, line2)
                if (maxMagnitude < magnitude2) {
                    maxMagnitude = magnitude2
                }
            }
        }
        println("Part 2: $maxMagnitude")
    }
}

private fun magnitudeOfSumOf(line1: List<Nr>, line2: List<Nr>): Int {
    val acc = line1.map { Nr(it.value, it.depth) }.toMutableList()
    acc.addAll(line2.map { Nr(it.value, it.depth) })
    acc.forEach { it.increaseDepth() }
    val accResult = reduce(acc)
    return magnitude(accResult)
}

fun magnitude(prs: List<Nr>): Int {
    var result = ArrayList<Nr>(prs)
    //println(result)
    while (result.size > 1) {
        val reduced = ArrayList<Nr>()
        var i = 1
        while (i < result.size) {
            val left = result[i - 1]
            val right = result[i]
            if (left.depth == right.depth) {
                reduced.add(Nr(left.value * 3 + right.value * 2, left.depth - 1))
                ++i // Extra step
            } else {
                reduced.add(left)
            }
            ++i
        }
        result = reduced
        //println(result)
    }
    return result.first().value
}

fun parsePairs(input: List<String>): List<ArrayList<Nr>> {
    val result = ArrayList<ArrayList<Nr>>()
    for (line in input) {
        val pairs = ArrayList<Nr>()
        parsePair(line, 0, pairs)
        result.add(pairs)
    }
    return result
}

fun parsePair(line: String, depth: Int, acc: ArrayList<Nr>): String {
    if (line.first() == '[') {
        val left = parsePart(',', line.substring(/* Remove [ */ 1), depth, acc)
        val right = parsePart(']', left.substring(/* Remove , */ 1), depth, acc)
        return right.substring(/* Remove ] */ 1)
    }
    throw IllegalStateException("Should not happen! Tail: $line")
}

fun parsePart(endChar: Char, line: String, depth: Int, acc: ArrayList<Nr>): String {
    if (line.first().isDigit()) {
        val i = line.indexOfFirst { it == endChar }
        val nr = line.substring(0, i).toInt()
        acc.add(Nr(nr, depth))
        return line.substring(i)
    }
    return parsePair(line, depth + 1, acc)
}

fun reduce(prs: List<Nr>): List<Nr> {
    var applied = true
    var reduced = ArrayList(prs)
    while (applied) {
        val exp = explodeFirst(reduced)
        reduced = exp.first
        applied = exp.second
        if (applied) {
            //println("Exploded: $reduced")
        }
        else {
            val split = splitFirst(reduced)
            reduced = split.first
            applied = split.second
            if (applied) {
                //println("Split: $reduced")
            }
        }
    }
    return reduced
}

fun splitFirst(prs: ArrayList<Nr>): Pair<ArrayList<Nr>, Boolean> {
    val i = prs.indexOfFirst { it.value > 9 }
    if (i < 0) {
        return Pair(prs, false)
    }
    val pr = prs[i]
    val leftValue = pr.value / 2
    val rightValue = ceil(pr.value / 2.0).toInt()
    val added = ArrayList<Nr>(if (i == 0) listOf() else prs.subList(0, i))
    added.add(Nr(leftValue, pr.depth + 1))
    added.add(Nr(rightValue, pr.depth + 1))
    if (i + 1 < prs.size) {
        added.addAll(prs.subList(i + 1, prs.size))
    }
    return Pair(added, true)
}

fun explodeFirst(prs: ArrayList<Nr>): Pair<ArrayList<Nr>, Boolean> {
    val i = findExplodableLeaf(prs)
    if (i < 0) {
        return Pair(prs, false)
    }
    val toExplodeLeft = prs[i]
    val toExplodeRight = prs[i + 1]
    if (i > 0) {
        prs[i - 1].addValue(toExplodeLeft.value)
    }
    if (i + 2 < prs.size) {
        prs[i + 2].addValue(toExplodeRight.value)
    }
    val exploded = ArrayList<Nr>(if (i == 0) listOf() else prs.subList(0, i))
    exploded.add(Nr(0, toExplodeLeft.depth - 1))
    if (i + 2 < prs.size) {
        exploded.addAll(prs.subList(i + 2, prs.size))
    }
    return Pair(exploded, true)
}

fun findExplodableLeaf(prs: List<Nr>): Int {
    for (i in 1 until prs.size) {
        val left = prs[i - 1]
        val right = prs[i]
        if (left.depth == right.depth && left.depth > 3) {
            return i - 1
        }
    }
    return -1
}

data class Nr(var value: Int, var depth: Int) {
    fun increaseDepth() {
        depth++
    }

    override fun toString(): String {
        return "[$value at $depth]"
    }

    fun addValue(v: Int) {
        value += v
    }
}