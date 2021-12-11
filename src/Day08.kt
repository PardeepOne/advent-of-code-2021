import java.util.*
import java.util.stream.Collectors

class Day8 : Day {
    override fun part1(inputList: List<String>): Int {
        val displays = parse(inputList)
        return displays.stream()
            .map { d: SevenSegmentDisplay -> d.digitsList }
            .flatMap { obj: List<String> -> obj.stream() }
            .filter { d: String ->
                simpleDigitCandidatesBySize.containsKey(
                    d.length
                )
            }
            .count().toInt()
    }

    override fun part2(inputList: List<String>): Int {
        return inputList.stream().map { entry: String ->
            parseEntry(
                entry
            )
        }.mapToInt { e: SevenSegmentDisplay -> e.output() }.sum()
    }

    class SevenSegmentDisplay(var signalPatternsList: List<String>, var digitsList: List<String>) {
        private var foundDigitsMap: MutableMap<String, Char> = mutableMapOf()
        private var digitPatternsMap: MutableMap<Char, String> = mutableMapOf()
        private var segmentMap: Map<Char, Char> = mutableMapOf()
        private fun determineOutput(): String {
            return digitsList.stream().map { test: String ->
                getMatchingDigit(
                    test
                )
            }.collect(Collectors.joining(""))
        }

        fun output(): Int {
            findDigits()
            return determineOutput().toInt()
        }

        private fun getMatchingDigit(test: String): String {
            return foundDigitsMap.entries.stream()
                .filter { (key): Map.Entry<String, Char?> ->
                    val a = key.toCharArray()
                    val b = test.toCharArray()
                    Arrays.sort(a)
                    Arrays.sort(b)
                    a.contentEquals(b)
                }
                .map { (_, value): Map.Entry<String, Char?> -> value.toString() }
                .findFirst()
                .get()
        }

        private fun findDigits() {
            foundDigitsMap = HashMap()
            digitPatternsMap = HashMap()
            for (patternStr in signalPatternsList) {
                if (simpleDigitCandidatesBySize.contains(patternStr.length)) {
                    foundDigitsMap[patternStr] =
                        simpleDigitCandidatesBySize[patternStr.length]!!
                    digitPatternsMap[simpleDigitCandidatesBySize[patternStr.length]!!] =
                        patternStr
                }
            }
            segmentMap = findSegments()
            val patternsToFind = signalPatternsList.stream().filter { p: String ->
                !foundDigitsMap.containsKey(
                    p
                )
            }.collect(Collectors.toList())
            foundDigitsMap[patternsToFind.stream().filter { s: String ->
                isZero(
                    s
                )
            }.findFirst().get()] = '0'
            foundDigitsMap[patternsToFind.stream().filter { s: String ->
                isTwo(
                    s
                )
            }.findFirst().get()] = '2'
            foundDigitsMap[patternsToFind.stream().filter { s: String ->
                isThree(
                    s
                )
            }.findFirst().get()] = '3'
            foundDigitsMap[patternsToFind.stream().filter { s: String ->
                isFive(
                    s
                )
            }.findFirst().get()] = '5'
            foundDigitsMap[patternsToFind.stream().filter { s: String ->
                isSix(
                    s
                )
            }.findFirst().get()] = '6'
            foundDigitsMap[patternsToFind.stream().filter { s: String ->
                isNine(
                    s
                )
            }.findFirst().get()] = '9'
        }

        private fun isNine(s: String): Boolean {
            return s.length == 6 && matches("a,b,c,d,f,g", s)
        }

        private fun isSix(s: String): Boolean {
            return s.length == 6 && matches("a,b,d,e,f,g", s)
        }

        private fun isFive(s: String): Boolean {
            return s.length == 5 && matches("a,b,d,f,g", s)
        }

        private fun isThree(s: String): Boolean {
            return s.length == 5 && matches("a,c,d,f,g", s)
        }

        private fun isTwo(s: String): Boolean {
            return s.length == 5 && matches("a,c,d,e,g", s)
        }

        private fun matches(csvValue: String, test: String): Boolean {
            return Arrays.stream(csvValue.split(",").toTypedArray()).map { c: String ->
                segmentMap[c[0]]
            }.allMatch { c ->containsChar(test,c)}
        }

        private fun isZero(s: String): Boolean {
            return s.length == 6 && matches("a,b,c,e,f,g", s)
        }

        private fun findSegments(): Map<Char, Char> {
            val segments = HashMap<Char, Char>()
            val partsOfOne = digitPatternsMap['1']!!.toCharArray()
            if (isC(partsOfOne[0])) {
                segments['c'] = partsOfOne[0]
                segments['f'] = partsOfOne[1]
            } else {
                segments['c'] = partsOfOne[1]
                segments['f'] = partsOfOne[0]
            }
            val a = digitPatternsMap['7']!!
                .replace("" + partsOfOne[0], "").replace("" + partsOfOne[1], "")[0]
            segments['a'] = a
            val partsOfEight = digitPatternsMap['8']!!.toCharArray()
            for (c in partsOfEight) {
                if (isD(c)) {
                    segments['d'] = c
                } else if (isE(c)) {
                    segments['e'] = c
                } else if (isG(c)) {
                    segments['g'] = c
                }
            }
            for (c in partsOfEight) {
                if (segments.containsValue(c)) {
                    continue
                }
                segments['b'] = c
            }
            return segments
        }

        private fun isG(c: Char): Boolean {
            return containsChar(digitPatternsMap['8'], c) &&
                    !containsChar(digitPatternsMap['1'], c) &&
                    !containsChar(digitPatternsMap['4'], c) &&
                    !containsChar(digitPatternsMap['7'], c) && countAppearancesPatternsWithLength(
                c,
                6
            ) == 3L && countAppearancesPatternsWithLength(c, 5) == 3L
        }

        private fun isE(c: Char): Boolean {
            return containsChar(digitPatternsMap['8'], c) && countAppearancesPatternsWithLength(
                c,
                6
            ) == 2L && countAppearancesPatternsWithLength(c, 5) == 1L
        }

        private fun isD(c: Char): Boolean {
            return containsChar(digitPatternsMap['8'], c) && countAppearancesPatternsWithLength(
                c,
                6
            ) == 2L && countAppearancesPatternsWithLength(c, 5) == 3L
        }

        private fun isC(c: Char): Boolean {
            return containsChar(digitPatternsMap['1'], c) && countAppearancesPatternsWithLength(
                c,
                6
            ) == 2L && countAppearancesPatternsWithLength(c, 5) == 2L
        }

        private fun containsChar(value: String?, test: Char?): Boolean {
            return value!!.contains(test.toString() + "")
        }

        private fun countAppearancesPatternsWithLength(c: Char, length: Int): Long {
            return signalPatternsList.stream()
                .filter { p: String -> p.length == length }
                .filter { p: String? ->
                    containsChar(
                        p,
                        c
                    )
                }
                .count()
        }
    }

    companion object {
        var simpleDigitCandidatesBySize = createSimpleDigitDefinitions()
        private fun createSimpleDigitDefinitions(): Map<Int, Char> = mapOf(
            2 to '1',
            4 to '4',
            3 to '7',
            7 to '8'
        )

        fun parse(input: List<String>): List<SevenSegmentDisplay> {
            return input.stream().map { s -> parseEntry(s) }.collect(Collectors.toList())
        }

        fun parseEntry(entry: String): SevenSegmentDisplay {
            var segmentElements: List<String> = entry.substring(0,entry.indexOf('|')).split(" ")
            segmentElements = segmentElements.filter { it.isNotEmpty() }
            var digitElements: List<String> = entry.substring(entry.indexOf('|')+1).split(" ")
            digitElements = digitElements.filter { it.isNotEmpty() }
            return SevenSegmentDisplay(
                segmentElements,
                digitElements
            )
        }
    }
}

fun main() {
    val day = Day8()

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    val part1 = day.part1(testInput)
    val part2 = day.part2(testInput)
    check(part1 == 26)
    check(part2 == 61229)

    val input = readInput("Day08")
    println(day.part1(input))
    println(day.part2(input))
}
