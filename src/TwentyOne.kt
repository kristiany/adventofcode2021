
fun main() {
    // test
    //val ply1 = 4
    //val ply2 = 8
    // Input
    val ply1 = 2
    val ply2 = 5

    var dice = 0
    val plyPos = ArrayList(listOf(ply1, ply2))
    val plyScore = ArrayList(listOf(0, 0))
    var cur = 0
    var rollCount = 0
    while (plyScore.all { it < 1000 }) {
        println("dice $dice")
        var roll = 0
        for (i in 1..3) {
            dice = roll(dice, 100)
            roll += dice
            ++rollCount
        }
        plyPos[cur] = (plyPos[cur] + roll - 1) % 10 + 1
        plyScore[cur] += plyPos[cur]
        println("Player ${cur + 1} rolled $roll, moves to ${plyPos[cur]}, score: ${plyScore[cur]}")
        cur = (cur + 1) % 2
    }
    println(plyScore)
    println("Part 1: ${plyScore.minOf { it } * rollCount}")

    val result = part2(ply1, ply2)
    println("Part 2: $result")
}


// Heavily inspired by https://github.com/calebwilson706/AOC2021/blob/master/2021KotlinSolutions/src/main/kotlin/Day21.kt
private fun part2(ply1: Int, ply2: Int): Long {
    val sides = setOf(1L, 2L, 3L)
    val p = sides.flatMap {
            one -> sides.flatMap {
            two -> sides.map {
            three -> listOf(one, two, three).sum() }}
    }.groupBy { it }.map { it.key to it.value.count().toLong() }
    println(p)

    fun play(game: Game): Pair<Long, Long> {
        return p.map { (roll, probability) ->
            val plyPos = game.plyPos.clone()
            val plyScore = game.plyScore.clone()
            plyPos[game.cur] = (plyPos[game.cur] + roll.toInt() - 1) % 10 + 1
            plyScore[game.cur] += plyPos[game.cur]
            val nextCur = (game.cur + 1) % 2
            val state = Game(plyPos, plyScore, nextCur)
            if (plyScore.any { it >= 21 }) {
                if (game.cur == 0) {
                    probability to 0L
                } else {
                    0L to probability
                }
            } else {
                val nextResult = play(state)
                nextResult.first * probability to nextResult.second * probability
            }
        }.reduce { acc, p -> Pair(acc.first + p.first, acc.second + p.second) }
    }
    val result = play(Game(intArrayOf(ply1, ply2), intArrayOf(0, 0), 0))
    //println("${result.first} ${result.second}")
    return maxOf(result.first, result.second)
}

fun roll(dice: Int, sides: Int): Int {
    return dice % sides + 1
}

data class Game(val plyPos: IntArray, val plyScore: IntArray, val cur: Int)