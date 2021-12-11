import java.lang.Integer.parseInt


fun main() {
    val uniqueNumSegments = intArrayOf(2, 3, 4, 7);
    fun part1(inputList: List<String>): Int {
        var uniqueSegDigits = 0
        for (s in inputList) {
            val outputDigits = (s.split("|")[1]).split(" ").filter { it.isNotEmpty() }
            for (i in outputDigits.indices) {
                val digit = outputDigits[i];
                if(uniqueNumSegments.contains(digit.length))
                    uniqueSegDigits++
            }
        }

        return uniqueSegDigits
    }

    fun part2(inputList: List<String>): Int {
        val oxygen = processArr(inputList, 0, true)
        val co2 = processArr(inputList, 0, false)

        return parseInt(oxygen,2) * parseInt(co2,2)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 26)
//    check(part2(testInput) == 230)

    val input = readInput("Day07")
    println(part1(input))
//    println(part2(input))
}
