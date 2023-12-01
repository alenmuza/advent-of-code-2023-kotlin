package day1

import readInput

fun main() {
    fun part1(input: List<String>): Int {
        return input.map {
            val digits = it.toCharArray()
                    .asList()
                    .filter { it.isDigit() }
            val firstDigit = digits.take(1).first()
            val lastDigit = digits.takeLast(1).first()
            return@map firstDigit.toString() + lastDigit.toString()
        }.map { it.toInt() }.sum()
    }

    val numMap = mapOf("one" to "1",
            "two" to "2",
            "three" to "3",
            "four" to "4",
            "five" to "5",
            "six" to "6",
            "seven" to "7",
            "eight" to "8",
            "nine" to "9")

    fun part2(input: List<String>): Int {
        return input.map { line ->
            val strLoc = numMap
                    .map { Triple(it.value, line.indexOf(it.key), line.lastIndexOf(it.key)) }
                    .filter { it.second > -1 }
                    .toMutableList()
            val numLoc = numMap.map { Triple(it.value, line.indexOf(it.value), line.lastIndexOf(it.value)) }.filter { it.second > -1 }
            strLoc.addAll(numLoc)
            return@map line to strLoc
        }.map {
            val res = it.second.minBy { it.second }.first + it.second.maxBy { it.third }.first
//            println("${it.first} --> ${res}")
            res
        }.sumOf { it.toInt() }
    }

    val testInput = readInput("day1/Day01_test")
    val testInput2 = readInput("day1/Day01_test2")
    val input = readInput("day1/Day01")
    println("First part for test input:  ${part1(testInput)}")

    println("First part for input:  ${part1(input)}")

    println("Second part for test input:  ${part2(testInput2)}")

    println("Second part for input:  ${part2(input)}")

}
