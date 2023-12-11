package day11

import readInput
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main() {

    fun parseInput(input: List<String>): Triple<List<Point>, List<Int>, List<Int>> {
        val points = input.mapIndexed { rowId, row ->
            row.mapIndexed { columnId, char ->
                Point(rowId, columnId, char)
            }
        }

        val galaxies = points.flatten().filter { it.char == '#' }

        val doubleRowIndices = input.mapIndexed { rowId, row ->
            if (row.all { it == '.' }) rowId else null
        }.filterNotNull()

        val doubleColumnIndices = points.flatten()
                .groupBy { it.columnId }
                .filter {
                    it.value.all { it.char == '.' }
                }.map { it.key }
        return Triple(galaxies, doubleRowIndices, doubleColumnIndices)
    }

    fun part1(input: List<String>): Int {
        val (galaxies, doubleRowIndices, doubleColumnIndices) = parseInput(input)

        return galaxies.map {galaxy1 ->
            galaxies.map {galaxy2 ->
                val yDiff = abs(galaxy1.rowId - galaxy2.rowId) + doubleRowIndices.count { it > min(galaxy1.rowId, galaxy2.rowId) && it < max(galaxy1.rowId, galaxy2.rowId) }
                val xDiff = abs(galaxy1.columnId - galaxy2.columnId) + doubleColumnIndices.count { it > min(galaxy1.columnId, galaxy2.columnId) && it < max(galaxy1.columnId, galaxy2.columnId) }
                yDiff + xDiff
            }
        }.flatten().sum() / 2

    }


    fun part2(input: List<String>): Long {
        val (galaxies, doubleRowIndices, doubleColumnIndices) = parseInput(input)

        return galaxies.map {galaxy1 ->
            galaxies.map {galaxy2 ->
                val yDiff = abs(galaxy1.rowId - galaxy2.rowId) + 999999L * doubleRowIndices.count { it > min(galaxy1.rowId, galaxy2.rowId) && it < max(galaxy1.rowId, galaxy2.rowId) }
                val xDiff = abs(galaxy1.columnId - galaxy2.columnId) + 999999L * doubleColumnIndices.count { it > min(galaxy1.columnId, galaxy2.columnId) && it < max(galaxy1.columnId, galaxy2.columnId) }
                yDiff + xDiff
            }
        }.flatten().sum() / 2L
    }

    val testInput = readInput("day11/Day11_test")
    val input = readInput("day11/Day11")

    println("First part for test input:  ${part1(testInput)}")
    println("First part for input:  ${part1(input)}")

    println("Second part for test input:  ${part2(testInput)}")
    println("Second part for input:  ${part2(input)}")
}

data class Point(
        val rowId: Int,
        val columnId: Int,
        val char: Char
)
