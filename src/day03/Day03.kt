package day03

import readInput

fun main() {
    fun extractEngineParts(input: List<String>) = input.mapIndexed { rowNo, row ->
        row.mapIndexed { index, c -> index to c }
                .filter { it.second.isDigit() }
                .fold(mutableListOf<MutableList<Pair<Int, Char>>>()) { groups, c ->
                    if (groups.isEmpty() || groups.last().last().first != c.first - 1) {
                        groups.add(mutableListOf(c))
                    } else {
                        groups.last().add(c)
                    }
                    groups
                }.map {
                    EnginePart(
                            it.map { it.second }.joinToString("").toInt(),
                            rowNo,
                            it.minOf { it.first },
                            it.maxOf { it.first }
                    )
                }
    }.flatten()

    fun exportSpecialChars(input: List<String>, filterFunction: (Pair<Int, Char>) -> Boolean) = input.mapIndexed { rowNo, row ->
        row.mapIndexed { index, c ->
            index to c
        }
                .filter(filterFunction)
                .map {
                    Triple(
                            it.second,
                            rowNo,
                            it.first
                    )
                }
    }.flatten()

    fun part1(input: List<String>): Int {
        val engineParts = extractEngineParts(input)

        val specialChars = exportSpecialChars(input) { !it.second.isDigit() && it.second != '.' }

        return engineParts.filter { ep ->
            specialChars.any { ep.isAdjacent(it) }
        }.sumOf { it.number }
    }


    fun part2(input: List<String>): Int {
        val engineParts = extractEngineParts(input)

        val gearChars = exportSpecialChars(input) { it.second == '*' }

        return gearChars.map { gear ->
            val gearParts = engineParts.filter { it.isAdjacent(gear) }.take(2)
            if (gearParts.size == 2) {
                return@map gearParts.map { it.number }.reduce { acc, i -> acc * i }
            } else return@map 0
        }.sum()
    }

    val testInput = readInput("day03/Day03_test")
    val testInput2 = readInput("day03/Day03_test2")
    val input = readInput("day03/Day03")

    println("First part for test input:  ${part1(testInput)}")
    println("First part for input:  ${part1(input)}")

    println("Second part for test input:  ${part2(testInput2)}")
    println("Second part for input:  ${part2(input)}")
}

data class EnginePart(
        val number: Int,
        val row: Int,
        val indexFrom: Int,
        val indexTo: Int
) {
    fun isAdjacent(specialChar: Triple<Char, Int, Int>): Boolean {
        if (specialChar.second in (row - 1)..(row + 1))
            if (specialChar.third in (indexFrom - 1)..(indexTo + 1)) {
                return true
            }
        return false
    }
}