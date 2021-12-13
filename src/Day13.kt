import java.math.BigInteger
import kotlin.streams.toList

class Day13 : Day {
    override fun part1(inputList: List<String>): Int {
        inputList.stream().map { s -> s.split(",") }.collect()
    }

    override fun part2(inputList: List<String>): Int {

    }

    private fun createMatrix(inputList: List<String>): Array<Array<String>> {
        var coordinatesPairs: List<Pair<Int, Int>> = inputList.stream().map { s -> s.split(",") }.map {
            s -> Pair(s[0].toInt(), s[1].toInt())
        }.toList()

        var maxX: Int = coordinatesPairs.stream().mapToInt { p -> p.first }.max().orElse(0)
        var maxY: Int = coordinatesPairs.stream().mapToInt{ p-> p.second }.max().orElse(0)
        var arr = Array(maxY+1) { IntArray(maxX+1) }
        coordinatesPairs.stream().forEach {
            p -> arr[p.first][p.second]=1
        }
    }
}

fun main() {
    val day = Day13()

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(day.part1(testInput) == 26397)
    val part2 = day.part2(testInput)
    //println(part2)
//    check(part2 == BigInteger.valueOf(288957))

    val input = readInput("Day10")
    println(day.part1(input))
    println(part.2(input))
}
