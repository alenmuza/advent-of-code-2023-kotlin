package day10

import println
import readInput
import java.lang.RuntimeException

fun main() {

    fun part1(input: List<String>): Int {
        val inputArray = input.map {
            it.toCharArray()
        }.toTypedArray()

        val startingPoint = input
                .mapIndexed { rowId, row -> if (row.contains("S")) rowId to row else null }
                .filter { it != null }
                .mapNotNull { Point(it!!.first,  it.second.indexOf("S"), 'S') }
                .first()

        startingPoint.println()

        inputArray[startingPoint.rowId][startingPoint.columnId]

        var nextPoint = startingPoint.nextPoint(inputArray, Direction.UP)
        var steps = 1

        while (nextPoint.first != startingPoint) {
            nextPoint = nextPoint.first.nextPoint(inputArray, nextPoint.second)
            steps++
        }

        return steps / 2
    }


    fun part2(input: List<String>): Int {
        val inputArray = input.map {
            it.toCharArray()
        }.toTypedArray()

        val startingPoint = input
                .mapIndexed { rowId, row -> if (row.contains("S")) rowId to row else null }
                .filter { it != null }
                .mapNotNull { Point(it!!.first,  it.second.indexOf("S"), 'S') }
                .first()

        startingPoint.println()

        inputArray[startingPoint.rowId][startingPoint.columnId]


        var nextPoint = startingPoint.nextPoint(inputArray, Direction.UP)
        val loopPoints = mutableListOf(startingPoint)
        var steps = 1

        while (nextPoint.first != startingPoint) {
            loopPoints.add(nextPoint.first)
            nextPoint = nextPoint.first.nextPoint(inputArray, nextPoint.second)
            steps++
        }



        return inputArray
                .mapIndexed { rowId, row ->
                    row.mapIndexed { columnId, char ->
                        Point(rowId, columnId, char)
                    }
                }.flatten()
                .filter { !loopPoints.contains(it) && it.isInside(loopPoints) }
                .count()
    }

    val testInput = readInput("day10/Day10_test")
    val testInput2 = readInput("day10/Day10_test2")
    val testInput3 = readInput("day10/Day10_test3")
    val input = readInput("day10/Day10")

    println("First part for test input:  ${part1(testInput)}")
    println("First part for test input 2:  ${part1(testInput2)}")
    println("First part for input:  ${part1(input)}")

    println("Second part for test input:  ${part2(testInput3)}")
    println("Second part for input:  ${part2(input)}")
}

data class Point(
        val rowId: Int,
        val columnId: Int,
        val char: Char
) {
    fun nextPoint(inputArray: Array<CharArray>, incomingDirectionToCurrentPoint: Direction): Pair<Point, Direction> {
        if (this.char == 'S' || this.char == 'F') {
            if (incomingDirectionToCurrentPoint == Direction.UP) {
                return Point(rowId, columnId + 1, inputArray[rowId][columnId + 1]) to Direction.RIGHT
            } else if (incomingDirectionToCurrentPoint == Direction.LEFT) {
                return Point(rowId + 1, columnId, inputArray[rowId + 1][columnId]) to Direction.DOWN
            }
        } else if (this.char == '7') {
            if (incomingDirectionToCurrentPoint == Direction.RIGHT) {
                return Point(rowId + 1, columnId, inputArray[rowId + 1][columnId]) to Direction.DOWN
            } else if (incomingDirectionToCurrentPoint == Direction.UP) {
                return Point(rowId, columnId - 1, inputArray[rowId][columnId - 1]) to Direction.LEFT
            }
        } else if (this.char == 'J') {
            if (incomingDirectionToCurrentPoint == Direction.RIGHT) {
                return Point(rowId - 1, columnId, inputArray[rowId - 1][columnId]) to Direction.UP
            } else if (incomingDirectionToCurrentPoint == Direction.DOWN) {
                return Point(rowId, columnId - 1, inputArray[rowId][columnId - 1]) to Direction.LEFT
            }
        } else if (this.char == 'L') {
            if (incomingDirectionToCurrentPoint == Direction.DOWN) {
                return Point(rowId, columnId + 1, inputArray[rowId][columnId + 1]) to Direction.RIGHT
            } else if (incomingDirectionToCurrentPoint == Direction.LEFT) {
                return Point(rowId - 1, columnId, inputArray[rowId - 1][columnId]) to Direction.UP
            }
        } else if (this.char == '|') {
            if (incomingDirectionToCurrentPoint == Direction.DOWN) {
                return Point(rowId + 1, columnId, inputArray[rowId + 1][columnId]) to Direction.DOWN
            } else if (incomingDirectionToCurrentPoint == Direction.UP) {
                return Point(rowId - 1, columnId, inputArray[rowId - 1][columnId]) to Direction.UP
            }
        } else if (this.char == '-') {
            if (incomingDirectionToCurrentPoint == Direction.RIGHT) {
                return Point(rowId, columnId + 1, inputArray[rowId][columnId + 1]) to Direction.RIGHT
            } else if (incomingDirectionToCurrentPoint == Direction.LEFT) {
                return Point(rowId, columnId - 1, inputArray[rowId][columnId - 1]) to Direction.LEFT
            }
        }
        throw RuntimeException("Uncovered case $this, incoming direction $incomingDirectionToCurrentPoint")
    }

    fun isInside(loopPoints: List<Point>): Boolean {
        val verticalParts = loopPoints.filter {
            (it.rowId == this.rowId && it.columnId < this.columnId)
                    && (it.char == '|' || it.char == 'F' || it.char == '7' || it.char == 'J' || it.char == 'L' || it.char == 'S')
        }.sortedBy { it.columnId }

        val pipes = verticalParts.filter { it.char == '|' }

        val corneredVerticalParts = verticalParts
                .filter { it.char == 'F' || it.char == '7' || it.char == 'J' || it.char == 'L' || it.char == 'S' }
                .chunked(2)
                .map {
                    if (it.first().char == 'F' || it.first().char == 'S') {
                        if (it.last().char == '7') {
                            return@map 0
                        } else if (it.last().char == 'J') {
                            return@map 1
                        }
                    } else if (it.first().char == 'L') {
                        if (it.last().char == '7') {
                            return@map 1
                        } else if (it.last().char == 'J') {
                            return@map 0
                        }
                    }
                    throw RuntimeException("Uncovered vertical case $it")
                }.sum()
        return (pipes.size + corneredVerticalParts) % 2 == 1

    }
}

enum class Direction{
    UP, DOWN, LEFT, RIGHT
}