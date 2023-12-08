package day04

import readInput

fun main() {

    fun part1(input: List<String>): Int {
        return input.map {
            val numbers = it.split(":").last().split("|")
            val winningNumbers = numbers.first().trim().split(" ").filter { it.isNotBlank() }.map { it.toInt() }
            val playedNumbers = numbers.last().trim().split(" ").filter { it.isNotBlank() }.map { it.toInt() }

            return@map playedNumbers.count { winningNumbers.contains(it) }
        }.map { Math.pow(2.0, it - 1.0).toInt()  }.sum()
    }


    fun part2(input: List<String>): Int {
        val scratchCardCounter = mutableMapOf<Int, Int>()

        input.forEachIndexed { rowNum, row ->
            val numbers = row.split(":").last().split("|")
            val winningNumbers = numbers.first().trim().split(" ").filter { it.isNotBlank() }.map { it.toInt() }
            val playedNumbers = numbers.last().trim().split(" ").filter { it.isNotBlank() }.map { it.toInt() }

            val hits = playedNumbers.count { winningNumbers.contains(it) }

            val currentCardCount = scratchCardCounter[rowNum + 1] ?: 1

            if (scratchCardCounter[rowNum + 1] == null) {
                scratchCardCounter[rowNum + 1] = 1
            }

            for (i in 1..hits) {
                val cardCounter = scratchCardCounter[rowNum + i + 1] ?: 1
                scratchCardCounter[rowNum + i + 1] = cardCounter + (1 * currentCardCount)
            }

        }

        return scratchCardCounter.values.sum()
    }

    val testInput = readInput("day04/Day04_test")
    val testInput2 = readInput("day04/Day04_test2")
    val input = readInput("day04/Day04")

    println("First part for test input:  ${part1(testInput)}")
    println("First part for input:  ${part1(input)}")

    println("Second part for test input:  ${part2(testInput2)}")
    println("Second part for input:  ${part2(input)}")
}
