package day09

import readInput

fun main() {

    fun part1(input: List<String>): Int {
        return input.map {
            it.split(" ").filter { it.isNotBlank() }.map { it.trim().toInt() }
        }.map { inputListOfInts ->
            val result = mutableListOf(inputListOfInts.toMutableList())
            do  {
                result.add(result.last().zipWithNext().map { it.second - it.first }.toMutableList())
            } while (result.last().any { element -> element != 0 })
            return@map result
        }.map {
            it.reversed().zipWithNext()
                    .forEach { it.second.add(it.second.last() + it.first.last()) }
            it
        }.sumOf {
            it.first().last()
        }
    }


    fun part2(input: List<String>): Int {
        return input.map {
            it.split(" ").filter { it.isNotBlank() }.map { it.trim().toInt() }
        }.map { inputListOfInts ->
            val result = mutableListOf(inputListOfInts.toMutableList())
            do  {
                result.add(result.last().zipWithNext().map { it.second - it.first }.toMutableList())
            } while (result.last().any { element -> element != 0 })
            return@map result
        }.map {
            it.reversed().zipWithNext()
                    .forEach { it.second.add(0, it.second.first() - it.first.first()) }
            it
        }.sumOf {
            it.first().first()
        }
    }

    val testInput = readInput("day09/Day09_test")
    val input = readInput("day09/Day09")

    println("First part for test input:  ${part1(testInput)}")
    println("First part for input:  ${part1(input)}")

    println("Second part for test input:  ${part2(testInput)}")
    println("Second part for input:  ${part2(input)}")
}
