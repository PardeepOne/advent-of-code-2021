fun main() {

    fun isElemLessThanOthers(element: Int, otherElements: List<Int>): Boolean {
        if (otherElements.isEmpty())
            return false
        for (elem in otherElements) {
            if (element < elem)
                continue
            else {
                return false
            }
        }
        return true
    }

    fun getLowPoints(inputList: List<String>): MutableMap<Pair<Int, Int>, Int> {
        val listLen = inputList.size
        val supportArray = Array(listLen) { Array(inputList[0].length) { 0 } }
        val lowPoints = mutableMapOf<Pair<Int, Int>, Int>()
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
                if (isLowPoint) {
                    lowPoints.apply { put(Pair(i, j), (row[j])) }
                }
            }
        }
        return lowPoints
    }

    fun part1(inputList: List<String>): Int {
        val lowPoints: MutableMap<Pair<Int, Int>, Int> = getLowPoints(inputList)
        return lowPoints.values.asSequence().map { it + 1 }.sum()
    }

    fun markCell(
        coordinatesToEvaluate: MutableList<Triple<Int, Int, Boolean>>,
        currentX: Int,
        currentY: Int
    ) {
        val elemInList =
            coordinatesToEvaluate.find { it.first == currentX && it.second == currentY }
        if (elemInList == null) {
            coordinatesToEvaluate.add(Triple(currentX, currentY, false))
        } else {
            coordinatesToEvaluate.remove(elemInList)
            coordinatesToEvaluate.add(Triple(currentX, currentY, false))
        }
    }

    fun evaluteNeighbours(
        currentX: Int,
        startX: Int,
        currentY: Int,
        startY: Int,
        prevX: Int,
        inputList: List<String>,
        supportArray: Array<Array<Int>>,
        coordinatesToEvaluate: MutableList<Triple<Int, Int, Boolean>>,
        strType: String
    ) {
        var currentX = startX
        var currentY = startY
        var prevX = currentX
        prevX = currentX
        var prevY = currentY
        while (true) {
            if (strType == "bottom" && currentX + 1 < inputList.size)
                currentX++
            else if (strType == "top" && currentX - 1 >= 0)
                currentX--
            else if (strType=="left" && currentY - 1 >= 0)
                currentY--
            else if (strType == "right" && currentY + 1 < inputList[0].length)
                currentY++
            else
                break

            if (supportArray[currentX][currentY] == 9) {
                break
            }

            if (supportArray[prevX][currentY] < supportArray[currentX][currentY] && strType == "top") {
                prevX = currentX
                markCell(coordinatesToEvaluate, currentX, currentY)
            }else if (strType == "left" && supportArray[currentX][prevY] < supportArray[currentX][currentY]) {
                prevY = currentY
                markCell(coordinatesToEvaluate, currentX, currentY)
            }else if (strType == "bottom" && supportArray[prevX][currentY] < supportArray[currentX][currentY]) {
                prevX = currentX
                markCell(coordinatesToEvaluate, currentX, currentY)
            } else if (strType == "right" && supportArray[currentX][prevY] < supportArray[currentX][currentY]) {
                prevY = currentY
                markCell(coordinatesToEvaluate, currentX, currentY)
            }else break
        }
    }

    fun traverseCoordinates(
        lowPoint: Pair<Int, Int>,
        supportArray: Array<Array<Int>>,
        inputList: List<String>,
        coordinatesToEvaluate: MutableList<Triple<Int, Int, Boolean>>
    ): MutableList<Triple<Int, Int, Boolean>> {
        val startX = lowPoint.first
        val startY = lowPoint.second
        var currentX = startX
        var prevX = currentX
        var currentY = startY

        evaluteNeighbours(
            currentX,
            startX,
            currentY,
            startY,
            prevX,
            inputList,
            supportArray,
            coordinatesToEvaluate,
            "top"
        )

        evaluteNeighbours(
            currentX,
            startX,
            currentY,
            startY,
            prevX,
            inputList,
            supportArray,
            coordinatesToEvaluate,
            "left"
        )

        evaluteNeighbours(
            currentX,
            startX,
            currentY,
            startY,
            prevX,
            inputList,
            supportArray,
            coordinatesToEvaluate,
            "bottom"
        )

        evaluteNeighbours(
            currentX,
            startX,
            currentY,
            startY,
            prevX,
            inputList,
            supportArray,
            coordinatesToEvaluate,
            "right"
        )
        if(coordinatesToEvaluate.contains(Triple(lowPoint.first, lowPoint.second, false)) ||
            coordinatesToEvaluate.contains(Triple(lowPoint.first, lowPoint.second, true))) {
            val find =
                coordinatesToEvaluate.find { triple -> triple.first == lowPoint.first && triple.second == lowPoint.second }
            coordinatesToEvaluate.remove(find)

            coordinatesToEvaluate.add(Triple(lowPoint.first, lowPoint.second,true))
        }else
            coordinatesToEvaluate.add(Triple(lowPoint.first, lowPoint.second, true))
        return coordinatesToEvaluate
    }

    fun part2(inputList: List<String>): Int {
        val lowPoints: MutableMap<Pair<Int, Int>, Int> = getLowPoints(inputList)
        val supportArray = Array(inputList.size) { Array(inputList[0].length) { 0 } }
        val basinList: MutableList<Int> = arrayListOf()
        for (i in inputList.indices) {
            val currentRow = inputList[i]
            currentRow.indices.forEach { j ->
                supportArray[i][j] = currentRow[j].digitToInt()
            }
        }
        for (lowPoint in lowPoints) {
            var traverseCoordinatesList: MutableList<Triple<Int, Int, Boolean>> = arrayListOf()
            traverseCoordinatesList = traverseCoordinates(lowPoint.key, supportArray, inputList, traverseCoordinatesList)

            while (traverseCoordinatesList.any { !it.third }) {
                var supportList: MutableList<Triple<Int, Int, Boolean>> = traverseCoordinatesList.toMutableList()
                for (elem in traverseCoordinatesList) {
                    if(!elem.third)
                        supportList = traverseCoordinates(Pair(elem.first, elem.second), supportArray, inputList, supportList)
                }
                traverseCoordinatesList = supportList.toMutableList()
            }
            basinList.add(traverseCoordinatesList.size)
        }
        basinList.sortDescending()
        return basinList.get(0)*basinList.get(1)*basinList.get(2)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 15)
    val part2 = part2(testInput)
    //println(part2)
    check(part2 == 1134)

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}
