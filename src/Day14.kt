import java.math.BigInteger
import java.util.*
import java.util.stream.Collectors

class Day14 : Day {
    override fun part1(inputList: List<String>): String {
        return polymerize(inputList, rounds = 10)
    }

    override fun part2(inputList: List<String>): String {
        return polymerize(inputList, rounds = 40)
    }

    private fun createBucket(polymerTemplate: String): LinkedHashMap<String, BigInteger> {
        val bucket = LinkedHashMap<String, BigInteger>()

        val templateList = polymerTemplate.toCharArray().toList()

        for (i in 0 until templateList.size - 1) {
            val pairStr = String(charArrayOf(templateList[i], templateList[i + 1]))
            if (bucket.containsKey(pairStr)) {
                bucket[pairStr] = bucket[pairStr]!!.add(BigInteger.ONE)
            } else {
                bucket[pairStr] = BigInteger.ONE
            }
        }

        return bucket
    }

    private fun countChars(polymerTemplate: LinkedHashMap<String, BigInteger>): MutableMap<Char, BigInteger> {
        val charsCountsMap = mutableMapOf<Char, BigInteger>()

        for (entry in polymerTemplate.entries) {
            val charsArr = entry.key.toCharArray()

            if (charsCountsMap.isEmpty()) {
                charsCountsMap[charsArr[0]] = polymerTemplate[entry.key]!!
            }

            if (!charsCountsMap.containsKey(charsArr[1])) {
                charsCountsMap[charsArr[1]] = polymerTemplate[entry.key]!!
            } else {
                charsCountsMap[charsArr[1]] =
                    charsCountsMap[charsArr[1]]!!.add(polymerTemplate[entry.key])
            }
        }
        return charsCountsMap
    }

    private fun polymerize(input: List<String>, rounds: Int): String {
        var bucket: LinkedHashMap<String, BigInteger> = createBucket(input[0])
        val insertionPairs: MutableMap<String, Char> = input.stream().skip(2).map { i: String ->
            i.split(
                " -> "
            ).toTypedArray()
        }.collect(
            Collectors.toUnmodifiableMap(
                { i: Array<String> ->
                    i[0]
                },
                { i: Array<String> ->
                    i[1][0]
                })
        )
        for (i in 0 until rounds) {
            bucket = polymerize(bucket, insertionPairs)
        }
        val countCharacters: Map<Char, BigInteger> = countChars(bucket)
        return countCharacters.values.stream().max(Comparator.naturalOrder()).get().subtract(
            countCharacters.values.stream().min(
                Comparator.naturalOrder()
            ).get()
        ).toString()
    }

    private fun polymerize(
        bucket: LinkedHashMap<String, BigInteger>,
        insertionPairsMap: Map<String, Char>
    ): LinkedHashMap<String, BigInteger> {
        val newBucket = LinkedHashMap<String, BigInteger>()

        for (key in bucket.keys) {
            val c: Char = insertionPairsMap[key]!!
            val firstNewPair = String(charArrayOf(key[0], c))
            val secondNewPair = String(charArrayOf(c, key[1]))
            if (newBucket.containsKey(firstNewPair)) {
                newBucket[firstNewPair] = newBucket[firstNewPair]!!.add(bucket[key])
            } else {
                newBucket[firstNewPair] = bucket[key]!!
            }
            if (newBucket.containsKey(secondNewPair)) {
                newBucket[secondNewPair] = newBucket[secondNewPair]!!.add(bucket[key])
            } else {
                newBucket[secondNewPair] = bucket[key]!!
            }
        }

        return newBucket
    }
}

fun main() {
    val day = Day14()

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
    val part1 = day.part1(testInput)
    check(part1.toBigInteger() == BigInteger.valueOf(1588))
    val part2 = day.part2(testInput)
    check(part2.toBigInteger() == BigInteger.valueOf(2188189693529))

    val input = readInput("Day14")
    println(day.part1(input))
    println(day.part2(input))
}
