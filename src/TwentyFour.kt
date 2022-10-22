fun main() {
    forLinesIn("day24/input.txt") {
        val prg = parsePrg(it.toList())
        //prg.debug()

        // Using nilanshu96's excellently described approach by going backwards,
        // first finding the pattern in the program and then optimize for wanted outcome when z = 0
        // https://github.com/nilanshu96/Advent-Of-Code/tree/main/2021/24
        val importantValues = prg.importantValues()
        //println(importantValues)

        val largeMonad = optimiseForLargest(importantValues)
        // Just a sanity check
        println(prg.exec(largeMonad))
        println("Part 1: ${largeMonad.joinToString("") { it.toString() }}")

        val smallMonad = optimiseForSmallest(importantValues)
        // Just a sanity check
        println(prg.exec(smallMonad))
        println("Part 2: ${smallMonad.joinToString("") { it.toString() }}")
    }
}

// Optimize for the largest possible nr
// https://github.com/nilanshu96/Advent-Of-Code/blob/main/2021/24/Part1.java#L20-L53
fun optimiseForLargest(importantValues: List<Values>): List<Int> {
    val monad = arrayListOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    val stack = ArrayDeque<Values>()
    for (v in importantValues) {
        if (v.x >= 10) {
            stack.addFirst(v)
        } else {
            val cur: Int = v.index
            val p: Values = stack.removeFirst()
            if (p.y + v.x >= 0) {
                monad[cur] = 9
                monad[p.index] = monad[cur] - (p.y + v.x)
            } else {
                monad[p.index] = 9
                monad[cur] = monad[p.index] + p.y + v.x
            }
        }
    }
    return monad
}

// Optimize for the smallest possible nr
// https://github.com/nilanshu96/Advent-Of-Code/blob/main/2021/24/Part2.java#L20-L53
fun optimiseForSmallest(importantValues: List<Values>): List<Int> {
    val monad = arrayListOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    val stack = ArrayDeque<Values>()
    for (v in importantValues) {
        if (v.x >= 10) {
            stack.addFirst(v)
        } else {
            val cur: Int = v.index
            val p: Values = stack.removeFirst()
            if (p.y + v.x >= 0) {
                monad[p.index] = 1
                monad[cur] = monad[p.index] + p.y + v.x
            } else {
                monad[cur] = 1
                monad[p.index] = monad[cur] - (p.y + v.x)
            }
        }
    }
    return monad
}

fun parsePrg(input: List<String>): Prg {
    return Prg(input.map {
        val spl = it.split(" ")
        val ins = spl[0]
        val a = spl[1][0]
        val b = if (spl.size > 2) spl[2].toLongOrNull() ?: spl[2][0] else null
        Ins(ins, a, b)
    })
}

data class Values(val index: Int, val x: Int, val y: Int)

data class Prg(val ins: List<Ins>) {

    fun exec(input: List<Int>): Map<Char, Long> {
        val mem = mutableMapOf(
            'x' to 0L,
            'y' to 0L,
            'z' to 0L,
            'w' to 0L
        )

        val params = input.toMutableList()
        for (instruction in ins) {
            if (instruction.ins == "inp") {
                val inputNr = params.removeFirst().toLong()
                mem[instruction.a] = inputNr
            } else {
                instruction.exec(mem)
            }
        }
        return mem
    }

    fun debug() {
        for (i in ins.indices step 18) {
            println("i: $i, nr 6: ${ins[i + 5]}, nr 16: ${ins[i + 15]}")
        }
    }

    // Repeating pattern for each input nr, resulting in just two important numbers for x and y
    // https://github.com/nilanshu96/Advent-Of-Code/blob/main/2021/24/Part1.java#L10-L18
    fun importantValues(): List<Values> {
        val result = ArrayList<Values>()
        for (i in ins.indices step 18) {
            println("i: $i, nr 6: ${ins[i + 5]}, nr 16: ${ins[i + 15]}")
            result.add(Values(i % 17, toInt(ins[i + 5].b), toInt(ins[i + 15].b)));
        }
        return result
    }

    private fun toInt(value: Any?): Int {
        if (value is Long) {
            return value.toInt()
        }
        throw IllegalStateException("Not expected $value")
    }
}

data class Ins(val ins: String, val a: Char, val b: Any?) {
    fun exec(mem: MutableMap<Char, Long>) {
        val mema = mem[a]!!
        val memb = if (b is Long) b else mem[b as Char]!!
        when (ins) {
            "add" -> mem[a] = mema + memb
            "mul" -> mem[a] = mema * memb
            "div" -> {
                //println("Div $mema / $memb = ${mema / memb}")
                if (memb == 0L) {
                    throw IllegalArgumentException("Div by zero yo!")
                }
                mem[a] = mema / memb
            }

            "mod" -> {
                //println("Mod $mema % $memb = ${mema % memb}")
                if (mema < 0L || memb <= 0L) {
                    throw IllegalArgumentException("Mod by illegal numbers $mema % $memb yo!")
                }
                mem[a] = mema % memb
            }

            "eql" -> mem[a] = if (mema == memb) 1L else 0L
            else -> throw IllegalArgumentException("Nah-ah, $ins is not a valid instruction!")
        }
    }
}