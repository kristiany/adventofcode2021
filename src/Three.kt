
fun main() {
    forLinesIn("day03/input.txt") {
        val input = it.toList()
        val length = input[0].length
        val halfSize = input.size / 2.0
        var gammaRateBinary = ""
        for (i in 0 until length) {
            gammaRateBinary += if (input.map { it[i] }.count { it == '1' } > halfSize) '1' else '0'
        }
        val gammaRate = gammaRateBinary.toInt(2)
        val eRate = gammaRateBinary.map { if (it == '1') '0' else '1' }.joinToString("").toInt(2)
        println("Part 1: $gammaRate * $eRate = ${gammaRate * eRate}")

        val oxyGen = find(input) { oneCount, halfSize -> oneCount >= halfSize }
        val oxyGenRate = oxyGen.toInt(2)
        //println("Oxygen $oxyGenRate")

        val co2Scrub = find(input) { oneCount, halfSize -> oneCount < halfSize }
        val co2ScrubRate = co2Scrub.toInt(2)
        //println("co2Scrub $co2ScrubRate")

        println("Part 2: $oxyGenRate * $co2ScrubRate = ${oxyGenRate * co2ScrubRate}")
    }

}

fun find(input: List<String>, criteria: (oneCount: Int, halfSize: Double) -> Boolean): String {
    var result = input
    for (i in 0 until input[0].length) {
        val compareToBit = if (criteria(result.map { it[i] }.count { it == '1' }, result.size / 2.0)) '1' else '0'
        //println("index $i compared bit $compareToBit")
        result = result.filter { it[i] == compareToBit }
        if (result.size == 1) {
            return result.first()
        }
    }
    throw IllegalStateException("Couldn't find one number, got ${result.size}")
}

