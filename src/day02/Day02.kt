package day02

import readInput

fun main() {
    fun parseGames(input: List<String>) = input.map {
        val firstSplit = it.split(":")
        val gameId = firstSplit.first().replace("Game ", "").toInt()

        val draws = firstSplit.last().split(";").map {
            it.split(",").map {
                val cube = it.trim().split(" ")
                cube.last() to cube.first().toInt()
            }.toMap()
        }.map {
            Draw(blueNum = it["blue"] ?: 0, redNum = it["red"] ?: 0, greenNum = it["green"] ?: 0)
        }

        Game(id = gameId, draws = draws)

    }

    fun part1(input: List<String>): Int {
        return parseGames(input).filter {
            it.maxOfBluesPerRound() <= 14 &&
            it.maxOfGreensPerRound() <= 13 &&
            it.maxOfRedsPerRound() <= 12
        }.sumOf { it.id }
    }


    fun part2(input: List<String>): Int {
        return parseGames(input).sumOf {
            it.maxOfRedsPerRound() * it.maxOfGreensPerRound() * it.maxOfBluesPerRound()
        }
    }

    val testInput = readInput("day02/Day02_test")
    val testInput2 = readInput("day02/Day02_test2")
    val input = readInput("day02/Day02")
    println("First part for test input:  ${part1(testInput)}")

    println("First part for input:  ${part1(input)}")

    println("Second part for test input:  ${part2(testInput2)}")

    println("Second part for input:  ${part2(input)}")

}

data class Game(
        val id: Int,
        val draws: List<Draw>
) {
    fun maxOfBluesPerRound() = draws.maxOf { it.blueNum }
    fun maxOfGreensPerRound() = draws.maxOf { it.greenNum }
    fun maxOfRedsPerRound() = draws.maxOf { it.redNum }
}

data class Draw(
        val blueNum: Int,
        val redNum: Int,
        val greenNum: Int
)
