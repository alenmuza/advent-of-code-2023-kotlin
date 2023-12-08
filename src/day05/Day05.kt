package day05

import readInput
import java.lang.RuntimeException
import kotlin.math.max
import kotlin.math.min

fun main() {

    fun extractInput(input: List<String>, inputStartLine: String) = RecordsList(input
            .asSequence()
            .dropWhile { !it.contains(inputStartLine) }
            .drop(1)
            .takeWhile { it.isNotBlank() }
            .map {
                val record = it.trim().split(" ")
                return@map Record(record.first().toLong(), record[1].toLong(), record.last().toLong())
            }.sortedBy { it.fromIndex }.toList())

    fun part1(input: List<String>): Long {
        val seeds = input.first().split(": ").last().trim().split(" ").map { it.toLong() }

        val seedToSoil = extractInput(input, "seed-to-soil map:")
        val soilToFertilizer = extractInput(input, "soil-to-fertilizer map:")
        val fertilizerToWater = extractInput(input, "fertilizer-to-water map:")
        val waterToLight = extractInput(input, "water-to-light map:")
        val lightToTemperatire = extractInput(input, "light-to-temperature map:")
        val temperatureToHumidity = extractInput(input, "temperature-to-humidity map:")
        val humidityToLocation = extractInput(input, "humidity-to-location map:")

        return seeds.asSequence()
                .map { seedToSoil.getOrDefaultSame(it) }
                .map { soilToFertilizer.getOrDefaultSame(it) }
                .map { fertilizerToWater.getOrDefaultSame(it) }
                .map { waterToLight.getOrDefaultSame(it) }
                .map { lightToTemperatire.getOrDefaultSame(it) }
                .map { temperatureToHumidity.getOrDefaultSame(it) }
                .map { humidityToLocation.getOrDefaultSame(it) }
                .min()
    }

    fun part2(input: List<String>): Long {
        val seeds = input.first().split(": ").last().trim().split(" ")
                .map { it.toLong() }.chunked(2)
                .map { SeedRange(it.first(), it.last()) }

        val seedToSoil = extractInput(input, "seed-to-soil map:")
        val soilToFertilizer = extractInput(input, "soil-to-fertilizer map:")
        val fertilizerToWater = extractInput(input, "fertilizer-to-water map:")
        val waterToLight = extractInput(input, "water-to-light map:")
        val lightToTemperatire = extractInput(input, "light-to-temperature map:")
        val temperatureToHumidity = extractInput(input, "temperature-to-humidity map:")
        val humidityToLocation = extractInput(input, "humidity-to-location map:")

        return seeds.parallelStream().map {
            var min = -1L
            for (i in it.seedStart..it.seedEnd) {
                val current = humidityToLocation.getOrDefaultSame(
                        temperatureToHumidity.getOrDefaultSame(
                                lightToTemperatire.getOrDefaultSame(
                                        waterToLight.getOrDefaultSame(
                                                fertilizerToWater.getOrDefaultSame(
                                                        soilToFertilizer.getOrDefaultSame(
                                                                    seedToSoil.getOrDefaultSame(i)))))))
                if (min == -1L || current < min) min = current

            }
            return@map min
        }.min(Long::compareTo).get()
    }

    fun part2NotWorking(input: List<String>): Long {
        val seeds = input.first().split(": ").last().trim().split(" ")
                .map { it.toLong() }.chunked(2)
                .map { SeedRange(it.first(), it.last()) }

        val seedToSoil = extractInput(input, "seed-to-soil map:")
        val soilToFertilizer = extractInput(input, "soil-to-fertilizer map:")
        val fertilizerToWater = extractInput(input, "fertilizer-to-water map:")
        val waterToLight = extractInput(input, "water-to-light map:")
        val lightToTemperatire = extractInput(input, "light-to-temperature map:")
        val temperatureToHumidity = extractInput(input, "temperature-to-humidity map:")
        val humidityToLocation = extractInput(input, "humidity-to-location map:")

        val resultRanges = seeds
                .asSequence()
                .map { seedToSoil.splitToResultingRangesOnRecords(it) }.flatten()
                .map { soilToFertilizer.splitToResultingRangesOnRecords(it) }.flatten()
                .map { fertilizerToWater.splitToResultingRangesOnRecords(it) }.flatten()
                .map { waterToLight.splitToResultingRangesOnRecords(it) }.flatten()
                .map { lightToTemperatire.splitToResultingRangesOnRecords(it) }.flatten()
                .map { temperatureToHumidity.splitToResultingRangesOnRecords(it) }.flatten()
                .map { humidityToLocation.splitToResultingRangesOnRecords(it) }.flatten()
                .toList()

        return resultRanges
                .minOf { it.seedStart }
    }

    val testInput = readInput("day05/Day05_test")
    val input = readInput("day05/Day05")

    println("First part for test input:  ${part1(testInput)}")
    println("First part for input:  ${part1(input)}")

    println("Second part for test input:  ${part2NotWorking(testInput)}")
    println("Second part for input (notWorking):  ${part2NotWorking(input)}")
    println("Second part for input:  ${part2(input)}")
}

data class Record(
        val baseResult: Long,
        val fromIndex: Long,
        val range: Long
) {
    val toIndex: Long
        get() = fromIndex + range - 1

    val diff: Long
        get() = baseResult - fromIndex

    fun isMatch(other: Long) = fromIndex <= other && toIndex > other
    fun getMapping(key: Long) = diff + key

    fun isSeedRangeMatch(seedRange: SeedRange) = (fromIndex <= seedRange.seedEnd && toIndex >= seedRange.seedEnd)
            || (fromIndex <= seedRange.seedStart && toIndex >= seedRange.seedStart)

}

data class RecordsList(
        val records: List<Record>
) {
    fun getOrDefaultSame(key: Long) = records
            .filter { it.isMatch(key) }
            .map { it.getMapping(key) }
            .firstOrNull() ?: key

    fun splitToResultingRangesOnRecords(seedRange: SeedRange): MutableList<SeedRange> {
        val resultRanges = mutableListOf<SeedRange>()
        val affectedRecords = records
                .filter { it.isSeedRangeMatch(seedRange) }

        if (affectedRecords.size == 1) {
            val affectedRecord = affectedRecords.first()
            if (seedRange.seedStart < affectedRecord.fromIndex && seedRange.seedEnd > affectedRecord.toIndex) {
                resultRanges.add(SeedRange(seedRange.seedStart, affectedRecord.fromIndex - seedRange.seedStart))
                resultRanges.add(SeedRange(affectedRecord.fromIndex, affectedRecord.toIndex - affectedRecord.fromIndex + 1).plusDiff(affectedRecord.diff))
                resultRanges.add(SeedRange(affectedRecord.toIndex + 1, seedRange.seedEnd - affectedRecord.toIndex))
            } else if (seedRange.seedStart < affectedRecord.fromIndex && seedRange.seedEnd <= affectedRecord.toIndex) {
                resultRanges.add(SeedRange(seedRange.seedStart, affectedRecord.fromIndex - seedRange.seedStart))
                resultRanges.add(SeedRange(affectedRecord.fromIndex, seedRange.seedEnd - affectedRecord.fromIndex + 1).plusDiff(affectedRecord.diff))
            } else if (seedRange.seedStart >= affectedRecord.fromIndex && seedRange.seedEnd <= affectedRecord.toIndex) {
                resultRanges.add(SeedRange(seedRange.seedStart, seedRange.seedEnd - seedRange.seedStart + 1).plusDiff(affectedRecord.diff))
            } else if (seedRange.seedStart >= affectedRecord.fromIndex && seedRange.seedEnd > affectedRecord.toIndex) {
                resultRanges.add(SeedRange(seedRange.seedStart, affectedRecord.toIndex - seedRange.seedStart + 1).plusDiff(affectedRecord.diff))
                resultRanges.add(SeedRange(affectedRecord.toIndex + 1, seedRange.seedEnd - affectedRecord.toIndex))
            } else {
                throw RuntimeException("Uncovered case $seedRange, $affectedRecord")
            }
        } else if (affectedRecords.isEmpty()) {
            resultRanges.add(seedRange)
        } else {
            val firstRecord = affectedRecords.first()
            if (seedRange.seedStart < firstRecord.fromIndex) {
                resultRanges.add(SeedRange(seedRange.seedStart, firstRecord.fromIndex - seedRange.seedStart + 1))
            }

            val lastRecord = affectedRecords.last()
            if (seedRange.seedEnd > lastRecord.toIndex) {
                resultRanges.add(SeedRange(lastRecord.toIndex, seedRange.seedEnd - lastRecord.toIndex + 1))
            }

            affectedRecords.zipWithNext().map {
                // dio u prvom segmentu
                val firstSegmentStart = max(it.first.fromIndex, seedRange.seedStart)
                resultRanges.add(SeedRange(firstSegmentStart, it.first.toIndex - firstSegmentStart + 1).plusDiff(it.first.diff))

                // medusegment

                if (it.first.toIndex + 1 < it.second.fromIndex) {
                    resultRanges.add(SeedRange(it.first.toIndex + 1, it.second.fromIndex - it.first.toIndex - 1))
                }

                // dio u drugom segmentu
                val lastSegmentEnd = min(it.second.toIndex, seedRange.seedEnd)
                resultRanges.add(SeedRange(it.second.fromIndex, lastSegmentEnd - it.second.fromIndex + 1).plusDiff(it.second.diff))
            }
        }
        return resultRanges


    }
}

data class SeedRange(
        val seedStart: Long,
        val range: Long
) {
    val seedEnd: Long
        get() = seedStart + range - 1

    fun plusDiff(diff: Long) = this.copy(seedStart + diff, range)

    fun splitToRanges(recordsList: RecordsList) {
        val intersectingRecrods = recordsList.records
                .filter { it.isSeedRangeMatch(this) }


    }
}