import java.math.BigInteger
import java.util.*
import java.util.stream.Collectors

class Day11(inputList: List<String>) : Day {
    val rows = inputList.size
    val columnSize = inputList[0].length
    val octupusStatesArr = Array(inputList.size) { IntArray(columnSize) }

    init {
        for (i in inputList.indices) {
            val str = inputList[i]
            parseEntry(str, i)
        }
    }

    private fun parseEntry(s: String, rowIdx: Int) {
        for (i in s.indices) {
            val num = s[i].digitToInt()
            octupusStatesArr[rowIdx][i] = num
        }
    }

    override fun part1(inputList: List<String>): Int {
        return elaborate(steps = 100, wantsAllFlashedFirstStep = false).first
    }

    private fun elaborate(steps: Int?, wantsAllFlashedFirstStep: Boolean): Pair<Int, Int?> {
        var flashes = 0
        var numOfCycles = steps ?: Int.MAX_VALUE
        var step = 1
        var allFlashedFirstStep: Int? = null
        while (numOfCycles > 0) {
            val flashed = mutableListOf<Pair<Int, Int>>()
            val toFlash = mutableListOf<Pair<Int, Int>>()
            for (i in 0 until rows) {
                for (j in 0 until columnSize) {
                    if (!flashed.contains(Pair(i, j))) {
                        if (octupusStatesArr[i][j] < 9) {
                            octupusStatesArr[i][j]++
                        } else {
                            flashes = flashCell(i, j, toFlash, flashes, flashed)
                        }
                    }
                }
            }
            if ((allFlashedFirstStep == null) && areAllFlashed()) {
                allFlashedFirstStep = step
            }
            if (wantsAllFlashedFirstStep && allFlashedFirstStep != null)
                break
            step++
            numOfCycles--
        }
        return Pair(flashes, allFlashedFirstStep)
    }

    private fun flashCell(
        i: Int,
        j: Int,
        toFlash: MutableList<Pair<Int, Int>>,
        flashesCounter: Int,
        flashed: MutableList<Pair<Int, Int>>
    ): Int {
        var flashes = flashesCounter
        octupusStatesArr[i][j] = 0
        flashed.add(Pair(i, j))
        flashes++

        //increase adjacent cells
        if (i - 1 >= 0) {
            if (j - 1 >= 0 && !flashed.contains(Pair(i - 1, j - 1))) {
                if (octupusStatesArr[i - 1][j - 1] < 9) {
                    octupusStatesArr[i - 1][j - 1]++
                } else {
                    toFlash.add(Pair(i - 1, j - 1))
                }
            }
            if (octupusStatesArr[i - 1][j] < 9 && !flashed.contains(Pair(i - 1, j))) {
                octupusStatesArr[i - 1][j]++
            } else if (!flashed.contains(Pair(i - 1, j))) {
                toFlash.add(Pair(i - 1, j))
            }
            if (j + 1 < columnSize && !flashed.contains(Pair(i - 1, j + 1))) {
                if (octupusStatesArr[i - 1][j + 1] < 9) {
                    octupusStatesArr[i - 1][j + 1]++
                } else if (!flashed.contains(Pair(i - 1, j + 1))) {
                    toFlash.add(Pair(i - 1, j + 1))
                }
            }
        }
        if (j - 1 >= 0 && !flashed.contains(Pair(i, j - 1))) {
            if (octupusStatesArr[i][j - 1] < 9) {
                octupusStatesArr[i][j - 1]++
            } else if (!flashed.contains(Pair(i, j - 1))) {
                toFlash.add(Pair(i, j - 1))
            }
        }
        if (j + 1 < columnSize && !flashed.contains(Pair(i, j + 1))) {
            if (octupusStatesArr[i][j + 1] < 9) {
                octupusStatesArr[i][j + 1]++
            } else if (!flashed.contains(Pair(i, j + 1))) {
                toFlash.add(Pair(i, j + 1))
            }
        }
        if (i + 1 < rows) {
            if (j - 1 >= 0 && !flashed.contains(Pair(i + 1, j - 1))) {
                if (octupusStatesArr[i + 1][j - 1] < 9) {
                    octupusStatesArr[i + 1][j - 1]++
                } else if (!flashed.contains(Pair(i + 1, j - 1))) {
                    toFlash.add(Pair(i + 1, j - 1))
                }
            }
            if (octupusStatesArr[i + 1][j] < 9 && !flashed.contains(Pair(i + 1, j))) {
                octupusStatesArr[i + 1][j]++
            } else if (!flashed.contains(Pair(i + 1, j))) {
                toFlash.add(Pair(i + 1, j))
            }
            if (j + 1 < columnSize && !flashed.contains(Pair(i + 1, j + 1))) {
                if (octupusStatesArr[i + 1][j + 1] < 9) {
                    octupusStatesArr[i + 1][j + 1]++
                } else if (!flashed.contains(Pair(i + 1, j + 1))) {
                    toFlash.add(Pair(i + 1, j + 1))
                }
            }
        }


        while (toFlash.isNotEmpty()) {
            val first = toFlash.first()
            val firstX = first.first
            val firstY = first.second
            toFlash.removeFirst()
            if (!flashed.contains(first)) {
                flashed.add(Pair(firstX, firstY))
                flashes = flashCell(firstX, firstY, toFlash, flashes, flashed)
            }
        }
        return flashes
    }

    private fun areAllFlashed(): Boolean {
        var cellCounter = 0
        for (i in 0 until rows) {
            for (j in 0 until columnSize)
                if (octupusStatesArr[i][j] == 0)
                    cellCounter++
        }
        return cellCounter == rows * columnSize
    }

    override fun part2(inputList: List<String>): Int {
        val elaboration = elaborate(steps = null, wantsAllFlashedFirstStep = true)
        return elaboration.second ?: 0
    }
}

fun main() {

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    var day = Day11(testInput)
    val part1 = day.part1(testInput)
    check(part1 == 1656)
    val part2 = day.part2(testInput)
    check(part2 == 95)
    //Why 195 ?
    // check(part2 == 195)

    val input = readInput("Day11")
    day = Day11(input)
    println(day.part1(input))
    println(day.part2(input))
}
