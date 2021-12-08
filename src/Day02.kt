import java.lang.Integer.parseInt

fun main() {
    fun part1(inputList: List<String>): Int {
        var x: Int = 0;
        var y: Int = 0;
        inputList.forEach {
            val splitElement = it.split(" ");
            val currentMove = splitElement[0]
            val currentMoveVal = parseInt(splitElement[1]);
            when (currentMove) {
                "forward" -> {
                    x += currentMoveVal;
                }
                "up" -> {
                    y -= currentMoveVal;
                }
                "down" -> {
                    y += currentMoveVal;
                }
            }
        }
        return x * y;
    }

    fun part2(inputList: List<String>): Int {
        var x: Int = 0;
        var y: Int = 0;
        var aim = 0;
        inputList.forEach {
            val splitElement = it.split(" ");
            val currentMove = splitElement[0]
            val currentMoveVal = parseInt(splitElement[1]);
            when (currentMove) {
                "forward" -> {
                    x += currentMoveVal;
                    y += aim * currentMoveVal;
                }
                "up" -> {
                    aim -= currentMoveVal;
                }
                "down" -> {
                    aim += currentMoveVal;
                }
            }
        }
        return x * y;
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 150)
    check(part2(testInput) == 900)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}
