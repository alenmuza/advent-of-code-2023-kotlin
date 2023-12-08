package day06

import readInput

fun main() {

    fun part1(input: List<String>): Int {
        val times = input.first().split(":").last().trim().split(" ").filter { it.isNotBlank() }.map { it.trim().toInt() }
        val distances = input.last().split(":").last().trim().split(" ").filter { it.isNotBlank() }.map { it.trim().toInt() }

        return times.mapIndexed { index, time ->
            val distance = distances[index]
            (0..time).asSequence().filter {
                val speed = it
                val reach = speed * (time - it)
                return@filter reach > distance
            }.count()
        }.reduce { acc, i -> acc * i }
    }


    fun part2(input: List<String>): Int {
        val time = input.first().split(":").last().replace(" ", "").toLong()
        val distance = input.last().split(":").last().replace(" ", "").toLong()

        return (0..time).asSequence().filter {
                val speed = it
                val reach = speed * (time - it)
                return@filter reach > distance
            }.count()
    }

    val testInput = readInput("day06/Day06_test")
    val input = readInput("day06/Day06")

    println("First part for test input:  ${part1(testInput)}")
    println("First part for input:  ${part1(input)}")

    println("Second part for test input:  ${part2(testInput)}")
    println("Second part for input:  ${part2(input)}")
}