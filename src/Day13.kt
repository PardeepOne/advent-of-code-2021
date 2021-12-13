import kotlin.streams.toList

class Day13 : Day {
    override fun part1(inputList: List<String>): Int {
        val pair = createMatrix(inputList)
        val dotsArr = pair.first
        val foldsList = pair.second
        val firstFold = foldsList[0]
        val resArr = foldArr(dotsArr, firstFold.first, firstFold.second)
        return countVisibleCells(resArr)
    }

    override fun part2(inputList: List<String>): Int {
        val pair = createMatrix(inputList)
        val dotsArr = pair.first
        val foldsList = pair.second
        var resArr = dotsArr
        for (idx in foldsList.indices) {
            val foldPoint = foldsList[idx]
            resArr = foldArr(resArr, foldPoint.first, foldPoint.second)
        }
        return countVisibleCells(resArr)
    }

    private fun transformInStringArr(arr: Array<IntArray>): Array<Array<String>> {
        val arrChar: Array<Array<String>> = Array(arr.size) { Array(arr[0].size) {
        "  ."
        } }
        for (i in arr.indices) {
            for (j in arr[0].indices){
                if (arr[i][j] == 1)
                    arrChar[i][j] = "#"
            }
        }
        return arrChar
    }

    private fun countVisibleCells(arr: Array<IntArray>): Int {
        var counter = 0
        for (i in arr.indices) {
            for( j in arr[0].indices) {
                if(arr[i][j] == 1)
                    counter++
            }
        }
        return counter
    }

    private fun foldArr(arr: Array<IntArray>, x: Int, y: Int): Array<IntArray> {
        val halfArr: Array<IntArray>
        if(y!=0) {
            halfArr = arr.copyOfRange(0, y)
            val foldedPart = arr.copyOfRange(y+1,arr.size)
            // horizontal fold
            for(row in foldedPart.size-1 downTo  0){
                for (column in 0 until arr[0].size) {
                    if(foldedPart[row][column] == 1) {
                        halfArr[foldedPart.size-1-row][column]=1
                    }
                }
            }
        } else {
            //vertical fold
            halfArr = Array(arr.size) { IntArray(x) }
            val foldedPart = Array(arr.size) { IntArray(x) }
            for (row in arr.indices) {
                for (column in 0 until x) {
                    halfArr[row][column] = arr[row][column]
                    foldedPart[row][column] = arr[row][column+foldedPart[0].size+1]
                }
            }

            for (row in halfArr.indices) {
                for (column in 0 until halfArr[0].size) {
                    if(foldedPart[row][column] == 1) {
                        halfArr[row][halfArr[0].size-1-column]=1
                    }
                }
            }
        }
        return halfArr
    }

    private fun createMatrix(inputList: List<String>): Pair<Array<IntArray>, List<Pair<Int, Int>>> {
        val emptyRowIdx = inputList.indexOf(inputList.first { s: String -> s.isEmpty() })
        val coordinatesList = inputList.subList(0,emptyRowIdx)
        val coordinatesPairs: List<Pair<Int, Int>> =
            coordinatesList.stream().map { s -> s.split(",") }.map { s ->
                Pair(s[0].toInt(), s[1].toInt())
            }.toList()
        val foldLists = inputList.subList(emptyRowIdx+1, inputList.size)

        val maxX: Int = coordinatesPairs.stream().mapToInt { p -> p.first }.max().orElse(0)
        val maxY: Int = coordinatesPairs.stream().mapToInt { p -> p.second }.max().orElse(0)
        val arr: Array<IntArray> = Array(maxY + 1) { IntArray(maxX + 1) }
        coordinatesPairs.stream().forEach { p ->
            arr[p.second][p.first] = 1
        }

        val foldingPairsList: List<Pair<Int, Int>> = foldLists.map { s -> s.split(" ") }
            .map { stringList -> stringList[stringList.size - 1] }
            .map { s: String ->
                if (s.contains("y")) Pair(
                    0,
                    s.substring(s.indexOf("=") + 1).toInt()
                ) else Pair(s.substring(s.indexOf("=") + 1).toInt(), 0)
            }.toList()
        return Pair(arr, foldingPairsList)
    }
}

fun main() {
    val day = Day13()

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
    check(day.part1(testInput) == 17)
    val part2 = day.part2(testInput)
    check(part2 == 16)

    val input = readInput("Day13")
    println(day.part1(input))
    println(day.part2(input))
}
