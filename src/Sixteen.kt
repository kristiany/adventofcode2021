import java.lang.IllegalArgumentException
import java.lang.IllegalStateException

fun main() {
    forLinesIn("day16/input.txt") {
        //val transmission = "D2FE28" // Test - type id 4
        //val transmission = "38006F45291200" // Test 2 - length type ID 0
        //val transmission = "EE00D40C823060" // Test 3 - length type ID 1
        //val transmission = "8A004A801A8002F478" // Test 4
        //val transmission = "620080001611562C8802118E34" // Test 5
        //val transmission = "C0015000016115A2E0802F182340" // Test 6
        //val transmission = "A0016C880162017C3686B18A3D4780" // Test 7

        // Part 2 tests
        //val transmission = "C200B40A82" // Test 8
        //val transmission = "04005AC33890" // Test 9
        //val transmission = "880086C3E88112" // Test 10
        //val transmission = "CE00C43D881120" // Test 11
        //val transmission = "D8005AC2A8F0" // Test 12
        //val transmission = "F600BC2D8F" // Test 13
        //val transmission = "9C005AC2F8F0" // Test 14
        //val transmission = "9C0141080250320F1802104A08" // Test 15

        val transmission = it.first() // Input

        val bits = transmission.toCharArray().map { toBits(it) }.joinToString("")
        println(bits)
        val parsed = parsePackets(bits)!!
        println(parsed)
        println("Part 1: ${sumOfVersions(parsed.first)}")

        println("Part 2: ${evaluate(parsed.first)}")
    }
}

fun evaluate(pkt: Pkt): Long {
    return when (pkt.type) {
        4 -> pkt.value!!
        0 -> pkt.pkts.sumOf { evaluate(it) }
        1 -> pkt.pkts.map { evaluate(it) }.reduce { a, b -> a * b }
        2 -> pkt.pkts.minOf { evaluate(it) }
        3 -> pkt.pkts.maxOf { evaluate(it) }
        5 -> if (evaluate(pkt.pkts[0]) > evaluate(pkt.pkts[1])) 1 else 0
        6 -> if (evaluate(pkt.pkts[0]) < evaluate(pkt.pkts[1])) 1 else 0
        7 -> if (evaluate(pkt.pkts[0]) == evaluate(pkt.pkts[1])) 1 else 0
        else -> throw IllegalArgumentException("Unknown type ${pkt.type}")
    }
}

private fun parsePackets(bits: String): Pair<Pkt, Int>? {
    if (bits.length < 6) {
        return null
    }
    val version = bits.substring(0, 3).toInt(2)
    val typeId = bits.substring(3, 6).toInt(2)
    if (typeId == 4) {
        var packetBits = ""
        var nrBits = 6 // version + typeId
        for (g in bits.substring(6, bits.length).chunked(5)) {
            packetBits += g.substring(1, g.length)
            nrBits += 5
            if (g.first() == '0') {
                break
            }
        }
        return Pair(Pkt(version, typeId, packetBits.toLong(2)), nrBits)
    }
    else if (bits[6] == '0') {
        val subPacketsStart = 7 + 15
        if (bits.length < subPacketsStart) {
            return null
        }
        val subPacketsLength = bits.substring(7, subPacketsStart).toInt(2)
        var sub = bits.substring(subPacketsStart, subPacketsStart + subPacketsLength)
        var consumedBits = 0

        val subs = ArrayList<Pkt>()
        while (consumedBits < subPacketsLength) {
            val parsed = parsePackets(sub) ?: break
            subs.add(parsed.first)
            val nrBits = parsed.second
            consumedBits += nrBits
            sub = sub.substring(nrBits)
        }
        return Pair(Pkt(version, typeId, null, subs), subPacketsStart + subPacketsLength)
    }
    else if (bits[6] == '1') {
        val subPacketsStart = 7 + 11
        if (bits.length < subPacketsStart) {
            return null
        }
        val subPacketsCount = bits.substring(7, subPacketsStart).toInt(2)
        var sub = bits.substring(subPacketsStart)
        var consumedBits = 0
        val subs = ArrayList<Pkt>()
        for (packetNr in 1..subPacketsCount) {
            val parsed = parsePackets(sub) ?: break
            subs.add(parsed.first)
            val nrBits = parsed.second
            consumedBits += nrBits
            sub = sub.substring(nrBits)
        }
        return Pair(Pkt(version, typeId, null, subs), subPacketsStart + consumedBits)
    }
    throw IllegalStateException("WAT!")
}

private fun sumOfVersions(pkt: Pkt): Int {
    var result = pkt.version
    if (pkt.value != null) {
        return result
    }
    for (p in pkt.pkts) {
        result += sumOfVersions(p)
    }
    return result
}

data class Pkt(val version: Int, val type: Int, val value: Long?, val pkts: List<Pkt> = listOf())

fun toBits(c: Char): String {
    return when (c) {
        '0' -> "0000"
        '1' -> "0001"
        '2' -> "0010"
        '3' -> "0011"
        '4' -> "0100"
        '5' -> "0101"
        '6' -> "0110"
        '7' -> "0111"
        '8' -> "1000"
        '9' -> "1001"
        'A' -> "1010"
        'B' -> "1011"
        'C' -> "1100"
        'D' -> "1101"
        'E' -> "1110"
        'F' -> "1111"
        else -> throw IllegalArgumentException("Invalid input $c")
    }
}
