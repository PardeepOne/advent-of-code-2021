import java.lang.Integer.parseInt


fun main() {
    fun part1(inputList: List<String>): Int {
        val len = if (inputList.isNotEmpty()) inputList[0].length else 0
        var gamma = ""
        var epsilon = ""
        val ones = IntArray(len)
        val zeroes = IntArray(len)
        for (s in inputList) {
            for (i in s.indices) {
                val c = s[i];
                if (c == '0') zeroes[i] = zeroes[i] + 1;
                else ones[i] = ones[i] + 1;
            }
        }

        for (i in ones.indices) {
            val oneVal = ones[i];
            val zeroVal = zeroes[i];
            if (oneVal > zeroVal) {
                gamma += '1';
                epsilon += '0';
            } else {
                gamma += '0';
                epsilon += '1';
            }
        }
        return parseInt(gamma, 2) * parseInt(epsilon, 2);
    }

    fun processArr(inputList: List<String>, charIdxToCheck: Int, isOxygen: Boolean): String {
        if (inputList.size == 1) {
            return inputList[0];
        }
        val newArr: List<String>
        var zeroCounter = 0;
        var onesCounter = 0;
        for (i in inputList.indices) {
            val str = inputList[i];
            if (str[charIdxToCheck] == '0') {
                zeroCounter++;
            } else {
                onesCounter++;
            }
        }
        newArr = if (zeroCounter > onesCounter) {
            if (isOxygen) inputList.filter { s -> s[charIdxToCheck] == '0' }
            else inputList.filter { s -> s[charIdxToCheck] == '1' }
        } else {
            if (isOxygen) inputList.filter { s -> s[charIdxToCheck] == '1' }
            else inputList.filter { s -> s[charIdxToCheck] == '0' }
        }
        return processArr(newArr, charIdxToCheck + 1, isOxygen);
    }

    fun part2(inputList: List<String>): Int {
        val oxygen = processArr(inputList, 0, true)
        val co2 = processArr(inputList, 0, false)

        return parseInt(oxygen,2) * parseInt(co2,2)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 198)
    check(part2(testInput) == 230)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}
