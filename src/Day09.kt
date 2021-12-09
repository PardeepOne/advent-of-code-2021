fun main() {

    fun isElemLessThanOthers(element: Int, otherElements: List<Int>): Boolean {
        if (otherElements.isEmpty())
            return false
        for (elem in otherElements) {
//            println("elem < element: $elem $element")
            if (element < elem)
                continue
            else {
                return false
            }
        }
        // println("Done isElemLessThanOther")
        return true
    }

    fun getLowPoints(inputList: List<String>): MutableList<Int> {
        val listLen = inputList.size
        val supportArray = Array(listLen) { Array(inputList[0].length) { 0 } }
        val lowPoints: MutableList<Int> = mutableListOf()
        for (i in inputList.indices) {
            val currentRow = inputList[i]
            currentRow.indices.forEach { j ->
                supportArray[i][j] = currentRow[j].digitToInt()
            }
        }

        for (i in supportArray.indices) {
            val row = supportArray[i]
            for (j in row.indices) {
                val listElemenbsToCompare: MutableList<Int> = mutableListOf()
                if (i - 1 >= 0) {
                    listElemenbsToCompare.add(supportArray[i - 1][j])
                }
                if (j - 1 >= 0) {
                    listElemenbsToCompare.add(row[j - 1])
                }
                if (j + 1 < row.size) {
                    listElemenbsToCompare.add(row[j + 1])
                }
                if (i + 1 < listLen) {
                    listElemenbsToCompare.add(supportArray[i + 1][j])
                }

                val isLowPoint = isElemLessThanOthers(row[j], listElemenbsToCompare)
                if (isLowPoint)
                    lowPoints.add(row[j])
            }
        }
        return lowPoints
    }

    fun part1(inputList: List<String>): Int {
        val lowPoints: MutableList<Int> = getLowPoints(inputList)
        return lowPoints.asSequence().map { it + 1 }.sum()
    }

//    fun isCornerLocation(row: Int, column: Int, rows: Int, columns: Int) {
//    }


    fun part2(inputList: List<String>): Int {
        val lowPoints: MutableList<Int> = getLowPoints(inputList)
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 15)
    // check(part2(testInput) == 230)

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}
