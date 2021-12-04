
fun main() {
    forLinesIn("day04/input.txt") {
        val input = it.toList()
        val numbers = input.first().split(",").map { it.trim().toInt() }
        val boards = parseBoards(input.subList(1, input.size))
        //println(numbers)
        //println(boards)
        val wins = play(numbers, boards)
        val first = wins.first()
        println("Part 1: ${first.second.unmarked() * first.first}")

        val last = wins.last()
        println("Part 2: ${last.second.unmarked() * last.first}")
    }
}

private fun play(numbers: List<Int>, boards: List<Board>): List<Pair<Int, Board>> {
    val winOrder = ArrayList<Pair<Int, Board>>()
    val wins = HashSet<Int>()
    for (nr in numbers) {
        val remainingBoards = boards.filter { ! wins.contains(it.nr) }.toList()
        remainingBoards.forEach { it.check(nr) }
        for (board in remainingBoards) {
            if (board.control()) {
                println("Winning board ${board.nr}")
                winOrder.add(Pair(nr, board))
                wins.add(board.nr)
            }
        }
    }
    return winOrder
}

fun parseBoards(input: List<String>): List<Board> {
    val boards = ArrayList<Board>()
    var boardNr = 1
    for (chunk in input.chunked(6)) {
        val b = ArrayList<List<Int>>()
        for (line in chunk.subList(1, 6)) {
            b.add(line.trim().split(Regex("\\s")).filter { it.isNotEmpty() }.map { it.toInt() }.toList())
        }
        boards.add(Board(boardNr, b))
        boardNr++
    }
    return boards
}

data class Board(val nr: Int, val board: List<List<Int>>) {
    private val boardNumbers: Set<Int> = HashSet(board.flatten())
    private val checked: MutableSet<Int> = HashSet()

    fun check(nr: Int) {
        if (boardNumbers.contains(nr)) {
            checked.add(nr)
        }
    }

    fun control(): Boolean {
        for (row in board) {
            if (checked.containsAll(row)) {
                return true
            }
        }
        for (i in 0 until 5) {
            val col = ArrayList<Int>()
            for (row in board) {
                col.add(row[i])
            }
            if (checked.containsAll(col)) {
                return true
            }
        }
        return false
    }

    fun unmarked(): Int {
        return boardNumbers.minus(checked).sum()
    }
 }
