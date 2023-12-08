package day08

import println
import readInput

fun main() {

    fun findLCM(a: Long, b: Long): Long {
        val larger = if (a > b) a else b
        val maxLcm = a * b
        var lcm = larger
        while (lcm <= maxLcm) {
            if (lcm % a == 0L && lcm % b == 0L) {
                return lcm
            }
            lcm += larger
        }
        return maxLcm
    }

    fun findLCMOfListOfNumbers(numbers: List<Long>): Long {
        var result = numbers[0]
        for (i in 1 until numbers.size) {
            result = findLCM(result, numbers[i])
        }
        return result
    }

    fun part1(input: List<String>): Int {
        val instructions = input.first().toCharArray()

        val nodes = input.drop(2).map {
            val id = it.split(" = ").first().trim()
            val leftAndRight = it.split(" = ").last().replace(")", "").replace("(", "").split(", ")
            Node(id, leftAndRight.first(), leftAndRight.last())
        }

        val repeatedInstructions = Array(200) { instructions }.flatMap { it.toList() }

        var currentNode = nodes.first { it.id == "AAA" }
        var steps = 0

        return repeatedInstructions.map { instruction ->
            currentNode.println()
            if (currentNode.id == "ZZZ") return@map steps
            currentNode = if (instruction == 'R') {
                nodes.first { it.id == currentNode.rightId }
            } else {
                nodes.first { it.id == currentNode.leftId }
            }
            steps++
            return@map 0
        }.first { it > 0 }
    }


    fun part2(input: List<String>): Long {
        val instructions = input.first().toCharArray()

        val nodes = input.drop(2).map {
            val id = it.split(" = ").first().trim()
            val leftAndRight = it.split(" = ").last().replace(")", "").replace("(", "").split(", ")
            Node(id, leftAndRight.first(), leftAndRight.last())
        }

        val repeatedInstructions = Array(200) { instructions }.flatMap { it.toList() }

        var currentNodes = nodes.filter { it.id.endsWith("A") }
        var steps = 0

        var endNodes = nodes.filter { it.id.endsWith("Z") }

        val stepCounters = currentNodes.mapIndexed { _, it ->
            var newSteps = 0
            var newCurrentNode = it
            return@mapIndexed repeatedInstructions.map { instruction ->
                newCurrentNode = if (instruction == 'R') {
                    nodes.first { it.id == newCurrentNode.rightId }
                } else {
                    nodes.first { it.id == newCurrentNode.leftId }
                }
                newSteps++
                if (newCurrentNode.id.endsWith("Z")) return@map newCurrentNode to newSteps
                return@map newCurrentNode to 0
            }.filter {it.second > 0}.take(1)
        }

        return findLCMOfListOfNumbers(stepCounters.flatten().map { it.second.toLong() }.toList())
    }

    val testInput = readInput("day08/Day08_test")
    val input = readInput("day08/Day08")

    println("First part for test input:  ${part1(testInput)}")
    println("First part for input:  ${part1(input)}")

    println("Second part for test input:  ${part2(testInput)}")
    println("Second part for input:  ${part2(input)}")
}

data class Node(
        val id: String,
        val leftId: String,
        val rightId: String
)