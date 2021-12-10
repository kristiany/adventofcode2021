
fun main() {
    forLinesIn("day08/input.txt") {
        val data = parseLine(it.toList())
        //println(lines)
        val uniqueSegments = setOf(2, 3, 4, 7)
        val count = data.map { it.output.count { uniqueSegments.contains(it.length()) } }.sum()
        println("Part 1: $count")

        val segments = listOf(6, 2, 5, 5, 4, 5, 6, 3, 7, 6)
        val outputs = ArrayList<Int>()
        for (line in data) {
            //println("Line $line")
            val patterns = line.patterns.sortedBy { it.length() }.toList()
            val searchResult = findEdges(ArrayList(patterns), segments, patterns)
            //println("Final edge map $edgeMap")
            val numbersMap = searchResult.numbers!!
            //println("Final nr map $numbersMap")
            outputs.add(line.output.map { numbersMap[it] }.joinToString("").toInt())
            //println(outputs)
        }
        println("Part 2: ${outputs.sum()}")

    }
}

private fun toNumbersMap(patterns: List<Pattern>, edgeMap: Map<Int, Char>): Map<Pattern, Int> {
    val numbersMap = HashMap<Pattern, Int>()
    for (nr in 0..9) {
        val edges = edgesFor(nr)
        for (pattern in patterns) {
            if (pattern.match(edges.map { edgeMap[it]!! }.toSet())) {
                numbersMap[pattern] = nr
            }
        }
    }
    return numbersMap
}

fun findEdges(patterns: List<Pattern>,
              segments: List<Int>,
              allPatterns: List<Pattern>,
              result: Map<Int, Char> = HashMap()): SearchResult {
    if (patterns.isEmpty()) {
        return SearchResult(result)
    }
    val pattern = patterns.first()
    val tail = patterns.subList(1, patterns.size)
    val allNrsOf = allIndexesOf(segments, pattern.length())
    for (nr in allNrsOf) {
        val searchableEdges = edgesFor(nr).minus(result.keys)
        if (searchableEdges.isEmpty()) {
            continue
        }
        val searchedResultPerNr = HashMap(result) // todo reset per nr really?
        val validCodes = ArrayList(pattern.chars.minus(result.values.toSet()))
        val permutedCodes = if (validCodes.size > 1) listOf(validCodes, validCodes.reversed()) else listOf(validCodes)
        for (codes in permutedCodes) {
            val toSearchResult = HashMap(searchedResultPerNr)
            val zip = codes.zip(searchableEdges)
            for (searchEdge in zip) {
                toSearchResult[searchEdge.second] = searchEdge.first
            }
            val searchedResult = findEdges(tail, segments, allPatterns, toSearchResult)
            // If we have 7 edges it's a full map
            if (searchedResult.edges.size == 7) { // todo too early return?
                val nrs = searchedResult.numbers ?: toNumbersMap(allPatterns, searchedResult.edges)
                if (nrs.size == 10 && nrs.values.toSet().containsAll((0..9).toList())) {
                    //println("Full match with nrs $nrs")
                    return SearchResult(searchedResult.edges, nrs)
                }
            }
        }
    }
    return SearchResult(result)
}


fun allIndexesOf(segments: List<Int>, patternLength: Int): List<Int> {
    val result = ArrayList<Int>()
    for (i in segments.indices) {
        if (segments[i] == patternLength) {
            result.add(i)
        }
    }
    return result
}

/*
     0000
    1    2
    1    2
     3333
    4    5
    4    5
     6666
 */
fun edgesFor(nr: Int): List<Int> {
    return when (nr) {
       0 -> listOf(0, 1, 2, 4, 5, 6)
       1 -> listOf(2, 5)
       2 -> listOf(0, 2, 3, 4, 6)
       3 -> listOf(0, 2, 3, 5, 6)
       4 -> listOf(1, 2, 3, 5)
       5 -> listOf(0, 1, 3, 5, 6)
       6 -> listOf(0, 1, 3, 4, 5, 6)
       7 -> listOf(0, 2, 5)
       8 -> listOf(0, 1, 2, 3, 4, 5, 6)
       9 -> listOf(0, 1, 2, 3, 5, 6)
       else -> throw IllegalArgumentException("Nr $nr not valid")
    }
}

fun parseLine(input: List<String>): List<Data> {
    return input.map { val parts = it.split("|")
        Data(parts[0].trim().split(" ").map { Pattern(it.toSet()) },
            parts[1].trim().split(" ").map { Pattern(it.toSet()) })
    }.toList()
}

data class Data(val patterns: List<Pattern>, val output: List<Pattern>)

data class Pattern(val chars: Set<Char>) {
    fun length(): Int {
        return chars.size
    }

    fun match(other: Set<Char>): Boolean {
        return chars == other
    }
}

data class SearchResult(val edges: Map<Int, Char>, val numbers: Map<Pattern, Int>? = null)
