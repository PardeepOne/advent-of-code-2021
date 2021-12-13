import java.util.*
import java.util.stream.Collectors

class Day12 : Day {
    val START = "start"
    val END = "end"

    override fun part1(inputList: List<String>): Int {
        val map: Map<String, MutableList<String>> = createMaps(inputList)
        val paths = findPaths(map = map, start = START, end = END, visitedList = emptyList(), canDoubleVisitSmallCave = false)
        return paths.size
    }

    override fun part2(inputList: List<String>): Int {
        val map: Map<String, MutableList<String>> = createMaps(inputList)
        val paths = findPaths(map = map, start = START, end = END, visitedList = emptyList(), canDoubleVisitSmallCave = true)
        return paths.size
    }

    private fun findPaths(map: Map<String, List<String>>, start: String, end: String, visitedList: List<String>, canDoubleVisitSmallCave: Boolean): List<List<String>> {
        if (start == end) {
            return listOf(listOf(start))
        }

        val currentPath: MutableList<String> = visitedList.toMutableList()
        currentPath.add(start)

        val isSecondSmallCaveVisit: Boolean = start.lowercase(Locale.getDefault()) == start && visitedList.contains(start)

        val visitable = map[start]?.stream()
            ?.filter{ cave -> START != cave }
            ?.filter{ cave -> cave.uppercase(Locale.getDefault()) == cave || !visitedList.contains(cave) || (!isSecondSmallCaveVisit && canDoubleVisitSmallCave)}
            ?.collect(Collectors.toList())

        val paths = mutableListOf<List<String>>()

        visitable?.forEach { cave -> paths.addAll(findPaths(map = map, start = cave, end = end,visitedList = currentPath, !isSecondSmallCaveVisit && canDoubleVisitSmallCave)) }

        return paths
    }

    private fun createMaps(input: List<String>): MutableMap<String, MutableList<String>> {
        val map: MutableMap<String, MutableList<String>> = HashMap()

        input.stream().map { line: String ->
            line.split("-").toTypedArray()
        }.forEach { path: Array<String> ->
            run {
                map.computeIfAbsent(path[0]) { ArrayList() }.add(path[1])
                map.computeIfAbsent(path[1]) { ArrayList() }.add(path[0])
            }
        }

        return map
    }
}

fun main() {
    val day = Day12()
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    val part1 = day.part1(testInput)
    check(part1 == 10)
    val part2 = day.part2(testInput)
    check(part2 == 36)

    val input = readInput("Day12")
    println(day.part1(input))
    println(day.part2(input))
}

