
fun main() {
    forLinesIn("day14/input.txt") {
        val input = it.toList()
        val template = input.first()
        val rules = HashMap<String, String>()
        for (rule in input.subList(2, input.size)) {
            val split = rule.split("->")
            rules[split[0].trim()] = split[1].trim()
        }

        val (most, least) = sim(template, rules, 10)
        println("Part 1: ${most - least}")

        val (most2, least2) = sim(template, rules, 40)
        println("Part 2: ${most2 - least2}")
    }
}

fun sim(template: String, rules: HashMap<String, String>, steps: Int): Pair<Long, Long> {
    var pairs = HashMap<String, Long>()
    for (pair in template.toCharArray().toList().windowed(2).map { it.joinToString("") }) {
        pairs[pair] = (pairs[pair] ?: 0) + 1
    }
    println(pairs)

    for (step in 1..steps) {
        val b = HashMap<String, Long>()
        for (pair in pairs.keys) {
            val insert = rules[pair]!!
            val pair1 = pair[0] + insert
            val pair2 = insert + pair[1]
            b[pair1] = (pairs[pair] ?: 0) + (b[pair1] ?: 0)
            b[pair2] = (pairs[pair] ?: 0) + (b[pair2] ?: 0)
        }
        pairs = b
        //println("Step $step: ${b}")
    }
    val charCounts = pairs.map { it.key[0] to it.value }
        .groupBy { it.first }
        .mapValues { it.value.sumOf { it.second } }
        .toMutableMap()
    // Update with the last char that's not handled by the algo
    charCounts[template.last()] = (charCounts[template.last()] ?: 0) + 1L
    println(charCounts)
    return Pair(charCounts.maxOf { it.value }, charCounts.minOf { it.value })
}
