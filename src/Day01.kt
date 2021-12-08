import kotlin.streams.toList

fun main() {
    fun part1(input: List<String>): Int {
        val vals = input.stream().mapToInt(Integer::parseInt).toList();
        var resCounter = 0
        var prevElement = vals[0]
        for (index in 0 until vals.size) {
            val currentElem = vals[index]
            if (index > 0 && prevElement < currentElem)
                resCounter++
            prevElement = currentElem
        }
        return resCounter
    }

    fun part2(input: List<String>): Int {
        val vals = input.stream().mapToInt(Integer::parseInt).toList();
        var resCounter = 0
        var prevSum = 0
        for (index in 0 until vals.size) {
            var currentSum = 0
            if (index + 2 < vals.size)
                currentSum += vals[index] + vals[index + 1] + vals[index + 2]
            if (index > 0 && currentSum > prevSum)
                resCounter++
            prevSum = currentSum
        }
        return resCounter
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 7)
    check(part2(testInput) == 5)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
