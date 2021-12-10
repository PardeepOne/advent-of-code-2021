import java.math.BigInteger

fun main() {

    val openParenthesis = listOf('(', '[', '{', '<')
    val closeParenthesis = listOf(')', ']', '}', '>')
    val expectedExpClosingParenthesis = mapOf(
        '(' to ')',
        '[' to ']',
        '{' to '}',
        '<' to '>'
    )
    val rogueParenthesisCost = mapOf(
        ')' to 3,
        ']' to 57,
        '}' to 1197,
        '>' to 25137
    )
    val incompleteCost = mapOf<Char, Long>(
        ')' to 1,
        ']' to 2,
        '}' to 3,
        '>' to 4
    )

    var incompleteStringsList = mutableListOf<String>()
    var incompleteStringsMap = mutableMapOf<String, List<Char>>()

    fun part1(inputList: List<String>): Int {
        incompleteStringsList = mutableListOf()
        val openParenthesisList = mutableListOf<Char>()
        val rogueCloseParenthesisList = mutableListOf<Char>()
        for (i in inputList.indices) {
            val currentRow = inputList[i]
            val localOpenParenthesisList = mutableListOf<Char>()
            var isRowRogue = false
            for (c in currentRow) {
                if (openParenthesis.contains(c)) {
                    openParenthesisList.add(c)
                    localOpenParenthesisList.add(c)
                } else if (closeParenthesis.contains(c)) {
                    val expectedClosingParenthesis =
                        expectedExpClosingParenthesis[openParenthesisList.last()]
                    if (expectedClosingParenthesis != c) {
                        rogueCloseParenthesisList.add(c)
                        isRowRogue=true
                        break
                    } else {
                        openParenthesisList.removeLast()
                        localOpenParenthesisList.removeLast()
                    }
                }
            }
            if(localOpenParenthesisList.isNotEmpty() && !isRowRogue)
                incompleteStringsMap[currentRow] = localOpenParenthesisList
        }
        return rogueCloseParenthesisList.map { rogueParenthesisCost[it] }.toList()
            .sumOf { i: Int? -> i ?: 0 }
    }

    fun part2(inputList: List<String>): BigInteger {
        var scoreList = mutableListOf<BigInteger>()
        var completedStringsList = mutableListOf<String>()
        part1(inputList)

        // complete strings
        incompleteStringsMap.forEach{entry ->
            run {
                var localScore = BigInteger.ZERO
                var currStr = entry.key
                for (c in entry.value.reversed()) {
                    localScore *= BigInteger.valueOf(5)
                    val closingParenthesis = expectedExpClosingParenthesis[c]
                    currStr += closingParenthesis
                    localScore += BigInteger.valueOf(incompleteCost[closingParenthesis]!!)
                }
                scoreList.add(localScore)
                completedStringsList.add(currStr)
            }
        }

        scoreList.sort()
        val middleIdx = (0 + scoreList.size)/2
        println(scoreList)
        println(middleIdx)
        println(scoreList.size)
        return scoreList[middleIdx]
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 26397)
    val part2 = part2(testInput)
    //println(part2)
    check(part2 == BigInteger.valueOf(288957))

    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}
