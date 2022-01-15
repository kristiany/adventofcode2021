
fun main() {
    // A 11-12
    // B 13-14
    // C 15-16
    // D 17-18

    /* Input
    #############
    #...........#
    ###D#B#A#C###
      #B#D#A#C#
      #########
    */
    val amphipods = arrayOf(
        15, 16, // A
        12, 13, // B
        17, 18, // C
        11, 14) // D
        .toIntArray()


    /* Test
    #############
    #...........#
    ###B#C#B#D###
      #A#D#C#A#
      #########
    */
    /*val amphipods = arrayOf(
        12, 18, // A
        11, 15, // B
        13, 16, // C
        14, 17) // D
        .toIntArray()
    */
    val start = System.currentTimeMillis()
    val part1 = movePods(amphipods, 0, 2)!!
    println("Part 1: ${part1.first}, ${part1.second.toList()}, ${(System.currentTimeMillis() - start) / 1000} sec")

    // A 11-14
    // B 15-18
    // C 19-22
    // D 23-26
    /* Input
    #############
    #...........#
    ###D#B#A#C###
      #D#C#B#A#
      #D#B#A#C#
      #B#D#A#C#
      #########
     */
    val amphipods2 = arrayOf(
        19, 21, 22, 24, // A
        14, 15, 17, 20, // B
        16, 23, 25, 26, // C
        11, 12, 13, 18) // D
        .toIntArray()

    /* Test
    #############
    #...........#
    ###B#C#B#D###
      #D#C#B#A#
      #D#B#A#C#
      #A#D#C#A#
      #########
    */
    // A 11-14
    // B 15-18
    // C 19-22
    // D 23-26
    /*val amphipods2 = arrayOf(
        14, 21, 24, 26, // A
        11, 17, 19, 20, // B
        15, 16, 22, 25, // C
        12, 13, 18, 23) // D
        .toIntArray()
     */

    val start2 = System.currentTimeMillis()
    val part2 = movePods(amphipods2, 0, 4)!!
    println("Part 2: ${part2.first}, ${part2.second.toList()}, ${(System.currentTimeMillis() - start2) / 1000} sec")
}

fun toType(podsSize: Int, index: Int): Char {
    val typeSize = podsSize / 4
    if (index < typeSize) {
        return 'A'
    }
    if (index < typeSize * 2) {
        return 'B'
    }
    if (index < typeSize * 3) {
        return 'C'
    }
    return 'D'
}

fun movePods(amphipods: IntArray, energy: Int, roomSize: Int): Pair<Int, IntArray>? {
    if (isComplete(amphipods, roomSize)) {
        return Pair(energy, amphipods)
    }

    val movables = movablePods(amphipods)
    if (movables.isEmpty()) {
        return null
    }
    //println("Corridor ${rooms.toList().subList(0, 11)}, rooms ${rooms.toList().subList(11, 19)}")
    //println("Movables $movables")
    var lowestEnergy: Pair<Int, IntArray>? = null
    for (movablePod in movables.sortedByDescending { weights[toType(amphipods.size, it)] }) {
        val moves = possibleMoves(amphipods, movablePod)
        for (move in moves) {
            //println("Move ${move.pos}, ${move.energy}, ${move.rooms.toList()}")
            val moved = movePods(move.amphipods, energy + move.energy, roomSize)
            if (moved != null) {
                //println("Moved ${moved.first}, ${moved.second.toList()}")
                if (isComplete(moved.second, roomSize) && moved.first < (lowestEnergy?.first ?: 100000000)) {
                    lowestEnergy = moved
                }
            }
            else {
                //println("Failed ${move.energy} | ${move.amphipods.toList()}")
                //println("Movable $movable")
            }
        }
    }
    return lowestEnergy
}

fun isComplete(amphipods: IntArray, roomSize: Int) = amphipods.asList().chunked(roomSize)
    .zip((11 until (11 + roomSize * 4)).chunked(roomSize))
    .all { p -> p.first.all { p.second.contains(it) } }

val doorways = setOf(2, 4, 6, 8)
val weights = mapOf(
    'A' to 1,
    'B' to 10,
    'C' to 100,
    'D' to 1000
)

val homeDoorwayIndex = mapOf(
    'A' to 2,
    'B' to 4,
    'C' to 6,
    'D' to 8
)

fun possibleMoves(amphipods: IntArray, movablePod: Int): List<Move> {
    val current = amphipods[movablePod]
    var possibleIndexes = ArrayList<Pair<Int, Int>>()
    val type = toType(amphipods.size, movablePod)
    val (movableIndex, roomStep) = moveOutOfRoom(current, amphipods.size / 4)
    var foundRoom = false
    // Search for places to move left of room entrance
    if (roomStep > 0 && movableIndex > 0) {
        val res = searchLeft(roomStep, movableIndex, amphipods, possibleIndexes, type)
        possibleIndexes = res.first
        foundRoom = res.second
    }
    // Search for places to move right of room entrance
    if (! foundRoom && roomStep > 0 && movableIndex < 10) {
        val res = searchRight(roomStep, movableIndex, amphipods, possibleIndexes, type)
        possibleIndexes = res.first
        foundRoom = res.second
    }
    // Search for corresponding room to get to
    if (roomStep == 0) {
        val doorwayIndex = homeDoorwayIndex[type]!!
        val range = if (movableIndex < doorwayIndex) {
            movableIndex + 1..doorwayIndex
        } else {
            movableIndex - 1 downTo doorwayIndex
        }
        var steps = 1
        var clearPath = true
        val pos = amphipods.toSet()
        for (index in range) {
            if (pos.contains(index)) {
                clearPath = false
                break
            }
            steps++
        }
        if (clearPath) {
            val foundRoom = getRoomIndexes(type, doorwayIndex, amphipods, steps - 1)
            // Discard all other positions
            foundRoom?.let { possibleIndexes = arrayListOf(it) }
        }
    }
    val result = ArrayList<Move>()
    val w = weights[type]!!
    for (i in possibleIndexes) {
        val copy = amphipods.copyOf()
        copy[movablePod] = i.second
        //println("Move $type from $current to ${i.second} with steps ${i.first} energy ${i.first * w}")
        //println("Copy ${copy.toList()}")
        result.add(Move(i.first * w, copy, i.second))
    }
    return result
}

private fun searchRight(
    roomStep: Int,
    movableIndex: Int,
    amphipods: IntArray,
    possibleIndexes: ArrayList<Pair<Int, Int>>,
    type: Char
): Pair<ArrayList<Pair<Int, Int>>, Boolean> {
    return searchRange(roomStep, amphipods, possibleIndexes, type, (movableIndex + 1..10))
}

private fun searchLeft(
    roomStep: Int,
    movableIndex: Int,
    amphipods: IntArray,
    possibleIndexes: ArrayList<Pair<Int, Int>>,
    type: Char
): Pair<ArrayList<Pair<Int, Int>>, Boolean> {
    return searchRange(roomStep, amphipods, possibleIndexes, type, (movableIndex - 1 downTo 0))
}

private fun searchRange(
    roomStep: Int,
    amphipods: IntArray,
    possibleIndexes: ArrayList<Pair<Int, Int>>,
    type: Char,
    range: IntProgression
): Pair<ArrayList<Pair<Int, Int>>, Boolean> {
    var steps = 1 + roomStep
    val pos = amphipods.toSet()
    for (index in range) {
        if (pos.contains(index)) {
            break
        }
        val foundRoom = getRoomIndexes(type, index, amphipods, steps)
        foundRoom?.let { return arrayListOf(it) to true }
        if (doorways.contains(index)) {
            steps++
            continue
        }
        possibleIndexes.add(Pair(steps, index))
        steps++
    }
    return possibleIndexes to false
}

private fun moveOutOfRoom(movableIndex: Int, roomSize: Int): Pair<Int, Int> {
    if (movableIndex < 11) {
        return movableIndex to 0
    }
    // returns (movableIndex, roomStep)
    val index = ((movableIndex - 11) / roomSize + 1) * 2
    val roomStep = (movableIndex - 11) % roomSize + 1
    return index to roomStep
}

private fun getRoomIndexes(
    type: Char,
    index: Int,
    amphipods: IntArray,
    steps: Int
): Pair<Int, Int>? {
    val roomSize = amphipods.size / 4
    if (type == 'A' && index == 2) {
        return getAvailableRoom(amphipods, 11, steps + 1, 'A')
    }
    else if (type == 'B' && index == 4) {
        return getAvailableRoom(amphipods, 11 + roomSize, steps + 1, 'B')
    }
    else if (type == 'C' && index == 6) {
        return getAvailableRoom(amphipods, 11 + roomSize * 2, steps + 1, 'C')
    }
    else if (type == 'D' && index == 8) {
        return getAvailableRoom(amphipods, 11 + roomSize * 3, steps + 1, 'D')
    }
    return null
}

fun getAvailableRoom(amphipods: IntArray, front: Int, steps: Int, homeType: Char): Pair<Int, Int>? {
    val roomSize = amphipods.size / 4
    val taken = amphipods.toSet()
    val back = front + roomSize - 1
    for (i in back downTo front) {
        if (! taken.contains(i)) {
            // returns steps, index
            return Pair(steps + (i - front), i)
        }
        else {
            val type = toType(amphipods.size, amphipods.indexOf(i))
            if (type != homeType) {
                return null
            }
        }
    }
    return null
}

fun movablePods(amphipods: IntArray): List<Int> {
    val result = ArrayList<Int>()
    val takenPos = amphipods.toSet()
    if (takenPos.contains(0) && ! takenPos.contains(1)) {
       result.add(amphipods.indexOf(0))
    }
    for (i in listOf(1, 3, 5, 7, 9)) {
        if (takenPos.contains(i)) {
            result.add(amphipods.indexOf(i))
        }
    }
    if (takenPos.contains(10) && ! takenPos.contains(9)) {
        result.add(amphipods.indexOf(10))
    }
    val roomSize = amphipods.size / 4
    val homeTypes = mapOf(1 to 'A', 2 to 'B', 3 to 'C', 4 to 'D')
    for (room in 1 .. 4) {
        val homeType = homeTypes[room]
        val roomStart = 11 + (room - 1) * roomSize
        var top: Int? = null
        var allHomeTypes = true
        for (i in roomStart until roomStart + roomSize) {
            if (takenPos.contains(i)) {
                val index = amphipods.indexOf(i)
                if (homeType != toType(amphipods.size, index)) {
                    allHomeTypes = false
                }
                if (top == null) {
                    top = index
                }
            }
            else {
                continue
            }
        }
        // todo If its a hometype we move out, we wanna stay close to home?
        if (! allHomeTypes && top != null) {
           result.add(top)
        }
    }
    return result
}

data class Move(val energy: Int, val amphipods: IntArray, val pos: Int)
