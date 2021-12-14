import java.math.BigInteger

class Day14 : Day {
    override fun part1(inputList: List<String>): String {
        val pair: Pair<String, Map<String, String>> = processInput(inputList)
        val startingStr = pair.first
        var resStr = startingStr
        for (i in 0..9) {
            resStr = applyRules(resStr, pair.second)
        }
        val mapCharOccurrence: Map<Char, BigInteger> = mapCharOccurrence(resStr)
        val maxOccurrence = mapCharOccurrence.values.maxOf { it }
        val minOccurrence = mapCharOccurrence.values.minOf { it }
        return (maxOccurrence - minOccurrence).toString()
    }

    override fun part2(inputList: List<String>): String {
        val pair: Pair<String, Map<String, String>> = processInput(inputList)
        val startingStr = pair.first
        var resStr = startingStr
        for (i in 0..39) {
            resStr = applyRules(resStr, pair.second)
        }
        val mapCharOccurrence: Map<Char, BigInteger> = mapCharOccurrence(resStr)
        val maxOccurrence = mapCharOccurrence.values.maxOf { it }
        val minOccurrence = mapCharOccurrence.values.minOf { it }
        return (maxOccurrence - minOccurrence).toString()
    }

    private fun applyRules(str: String, rulesMap: Map<String, String>): String {
        var retStr = str
        val newCharsToInsert = mutableListOf<String>()
        for (i in 0..str.length - 2) {
            val currStrPair = str[i].toString() + str[i + 1]
            val insertStr = rulesMap[currStrPair]
            newCharsToInsert.add(insertStr!!)
        }
        var i = 1
        while (i < retStr.length) {
            val beforeNewChar = retStr.substring(0, i)
            val afterNewChar = retStr.substring(i)
            retStr = beforeNewChar + newCharsToInsert.removeFirst() + afterNewChar
            i += 2
        }
        return retStr
    }

    private fun mapCharOccurrence(str: String): Map<Char, BigInteger> {
        val map = mutableMapOf<Char, BigInteger>()
        str.toCharArray().forEach { c ->
            run {
                if (map.contains(c))
                    map[c] = map[c]!!.inc()
                else
                    map[c] = BigInteger.ONE
            }
        }
        return map
    }

    private fun processInput(inputList: List<String>): Pair<String, Map<String, String>> {
        val startingStr = inputList[0]
        val insertionRulesList = inputList.subList(2, inputList.size)
        val mapInsertionRules = mutableMapOf<String, String>()
        insertionRulesList.stream().map { s -> s.split(" ") }.forEach { s ->
            mapInsertionRules[s[0]] = s[2]
        }
        return Pair(startingStr, mapInsertionRules)
    }
}

fun main() {
    val day = Day14()

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
    val part1 = day.part1(testInput)
    check(part1.toBigInteger() == BigInteger.valueOf(1588))
//    val part2 = day.part2(testInput)
//    check(part2.toBigInteger() == BigInteger.valueOf(2188189693529))

    val input = readInput("Day14")
    println(day.part1(input))
//    println(day.part2(input))
}
