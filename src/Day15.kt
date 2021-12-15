import java.util.*
import kotlin.math.max
import kotlin.math.min
import kotlin.streams.toList

class Day15 : Day {
    override fun part1(inputList: List<String>): String {
        val locations = convertListToArray(inputList)
        val startY = locations.size - 1
        val startX = locations[0].size - 1
        return findShortestDijkstraDistance(
            Triple(
                startY,
                startX,
                Int.MAX_VALUE
            ), locations, createDistancesArray(locations)
        ).toString()
    }

    override fun part2(inputList: List<String>): String {
        val locations = convertListToArray(inputList)
        val expandedArr = expandArray(locations)
        val startY = expandedArr.size - 1
        val startX = expandedArr[0].size - 1
        return findShortestDijkstraDistance(
            Triple(startY, startX, Int.MAX_VALUE),
            expandedArr, createDistancesArray(expandedArr)
        ).toString()
    }

    private fun expandArray(locations: Array<IntArray>): Array<IntArray> {
        val maxMultiplier = 5
        val maxCost = 9
        val allLocations =
            Array(locations.size * maxMultiplier) { IntArray(locations[0].size * maxMultiplier) }
        val baseLineLength = locations[0].size

        for (y in locations.indices) {
            for (xMultiplier in 0 until maxMultiplier) {
                for (x in 0 until locations[0].size) {
                    var cost = locations[y % locations.size][x] + xMultiplier
                    if (cost > maxCost)
                        cost -= maxCost

                    allLocations[y][x + baseLineLength * xMultiplier] = cost
                }
            }
        }

        for (y in locations.size until allLocations.size) {
            for (x in  allLocations[0].indices){
                var cost = allLocations[y - locations.size][x] +1
                if (cost > maxCost)
                    cost -= maxCost

                allLocations[y][x] = cost
            }
        }
        return allLocations
    }

    private fun getNeighbours(
        current: Triple<Int, Int, Int>,
        locationsArray: Array<IntArray>
    ): MutableList<Triple<Int, Int, Int>> {
        val x = current.second
        val y = current.first
        val minX = max(0, x - 1)
        val maxX = min(locationsArray[y].size - 1, x + 1)
        val minY = max(0, y - 1)
        val maxY = min(locationsArray.size - 1, y + 1)

        val neighborsList: MutableList<Triple<Int, Int, Int>> = mutableListOf()

        if (y != minY)
            neighborsList.add(Triple(minY, x, locationsArray[minY][x]))
        if (y != maxY)
            neighborsList.add(Triple(maxY, x, locationsArray[maxY][x]))
        if (x != minX)
            neighborsList.add(Triple(y, minX, locationsArray[y][minX]))
        if (x != maxX)
            neighborsList.add(Triple(y, maxX, locationsArray[y][maxX]))

        return neighborsList
    }

    private fun findShortestDijkstraDistance(
        endPoint: Triple<Int, Int, Int>,
        locationsList: Array<IntArray>, distances: Array<IntArray>
    ): Int {
        val compareByLength: Comparator<Triple<Int, Int, Int>> = compareBy { it.third }
        val queue: PriorityQueue<Triple<Int, Int, Int>> = PriorityQueue(compareByLength)
        queue.add(Triple(0, 0, distances[0][0]))

        while (!queue.isEmpty()) {
            val current = queue.poll()
            val visitableNeighbors = getNeighbours(current, locationsList)

            for (neighbor in visitableNeighbors) {
                val currentNeighborDistance =
                    distances[current.first][current.second] + locationsList[neighbor.first][neighbor.second]

                if (distances[neighbor.first][neighbor.second] > currentNeighborDistance) {
                    if (distances[neighbor.first][neighbor.second] != Int.MAX_VALUE) {
                        queue.remove(neighbor)
                    }

                    distances[neighbor.first][neighbor.second] = currentNeighborDistance
                    queue.add(Triple(neighbor.first, neighbor.second, currentNeighborDistance))
                }
            }
        }

        return distances[endPoint.first][endPoint.second]
    }

    private fun createDistancesArray(locations: Array<IntArray>): Array<IntArray> {
        val distances: Array<IntArray> = Array(locations.size) { IntArray(locations[0].size) }
        for (i in distances.indices) {
            Arrays.fill(distances[i], Int.MAX_VALUE)
        }
        distances[0][0] = 0
        return distances
    }

    private fun printArray(distances: Array<IntArray>) {
        println()
        for (y in distances.indices) {
            for (x in 0 until distances[y].size) {
                print(if (distances[y][x] == Int.MAX_VALUE) "." else distances[y][x].toString())
                print(' ')
            }
            println()
        }
    }

    private fun convertListToArray(inputList: List<String>): Array<IntArray> {
        val array: Array<IntArray> = Array(inputList.size) { IntArray(inputList[0].length) }
        for (i in inputList.indices) {
            val str = inputList[i]
            array[i] = str.toCharArray().toList().stream().map { c -> c.digitToInt() }.toList()
                .toIntArray()
        }
        return array
    }
}

fun main() {
    val day = Day15()

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test")
    val part1 = day.part1(testInput)
    check(part1.toInt() == 40)
    val part2 = day.part2(testInput)
    check(part2.toInt() == 315)

    val input = readInput("Day15")
    println(day.part1(input))
    println(day.part2(input))
}
