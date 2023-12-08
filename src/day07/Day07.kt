package day07

import readInput

fun main() {

    val part1CardScores = listOf(
            Card('A', 14),
            Card('K', 13),
            Card('Q', 12),
            Card('J', 11),
            Card('T', 10),
            Card('9', 9),
            Card('8', 8),
            Card('7', 7),
            Card('6', 6),
            Card('5', 5),
            Card('4', 4),
            Card('3', 3),
            Card('2', 2),
    )

    val part2CardScores = listOf(
            Card('A', 14),
            Card('K', 13),
            Card('Q', 12),
            Card('J', 1),
            Card('T', 10),
            Card('9', 9),
            Card('8', 8),
            Card('7', 7),
            Card('6', 6),
            Card('5', 5),
            Card('4', 4),
            Card('3', 3),
            Card('2', 2),
    )


    fun part1(input: List<String>): Int {
        return input.map {
                val cards = it.split(" ").first().toCharArray().map { char -> part1CardScores.first { it.symbol == char } }.toList()
                val bidValue = it.split(" ").last().trim().toInt()
                return@map Bid(cards, bidValue)
            }
            .sortedWith(Bid::compareTo)
//            .onEach { (it.cards.map { it.symbol } to it.getMaxJockerCardsScore()).println() }
            .mapIndexed { index, bid -> (index + 1) * bid.bidValue }
            .sum()
    }


    fun part2(input: List<String>): Int {
        return input.map {
            val cards = it.split(" ").first().toCharArray().map { char -> part2CardScores.first { it.symbol == char } }.toList()
            val bidValue = it.split(" ").last().trim().toInt()
            return@map Bid(cards, bidValue)
        }
                .sortedWith(Bid::compareToAllJockerCombinations)
                .onEachIndexed{ index, i -> println(i.cards.map { it.symbol }.joinToString("") + " ${index +1}" ) }
                .mapIndexed { index, bid -> (index + 1) * bid.bidValue }
                .sum()
    }

    val testInput = readInput("day07/Day07_test")
    val input = readInput("day07/Day07_G")

    println("First part for test input:  ${part1(testInput)}")
    println("First part for input:  ${part1(input)}")

    println("Second part for test input:  ${part2(testInput)}")
    println("Second part for input:  ${part2(input)}")
}

enum class TypeScore {
    HIGH_CARD,
    ONE_PAIR,
    TWO_PAIR,
    TRIS,
    FULL_HOUSE,
    FOUR,
    FIVE
}

data class Card(
        val symbol: Char,
        val value: Int
)

data class Bid(
        val cards: List<Card>,
        val bidValue: Int
) {
    fun getCardsCount(): Map<Char, List<Card>> {
        return cards
                .groupBy { it.symbol }
                .filter { it.value.size > 1 }
    }

    fun getJockerCount(): Int {
        return cards.filter { it.symbol == 'J' }.count()
    }

    fun getMaxJockerCardsScore(): TypeScore {
        return cards.filter { it.symbol != 'J' }
                .map {
                    cards.map { card ->
                        if (it.symbol == 'J') {
                            return@map null
                        } else {
                            Bid(cards.map { if (it.symbol == 'J') card else it }, this.bidValue)
                        }
                    }
                }.flatten()
                .union(listOf(this))
                .maxOf { it!!.getCardScore() }
    }

    fun getCardScore(): TypeScore {
        val cardsCountMap = getCardsCount()

        if (cardsCountMap.values.any { it.size >= 5 }) {
            return TypeScore.FIVE
        } else if (cardsCountMap.values.any { it.size == 4 }) {
            return TypeScore.FOUR
        }

        if (cardsCountMap.size > 1 && cardsCountMap.values.any { it.size == 3 }) {
            return TypeScore.FULL_HOUSE
        }

        if (cardsCountMap.values.any { it.size == 3 }) {
            return TypeScore.TRIS
        }

        return if (cardsCountMap.size > 1 && cardsCountMap.values.any { it.size == 2 }) {
            TypeScore.TWO_PAIR
        } else if (cardsCountMap.values.any { it.size == 2 }) {
            TypeScore.ONE_PAIR
        } else {
            TypeScore.HIGH_CARD
        }
    }

    fun compareTo(other: Bid): Int {
        val thisCardScore = this.getCardScore()

        val otherCardScore = other.getCardScore()
        return if (thisCardScore != otherCardScore) {
            thisCardScore.ordinal - otherCardScore.ordinal
        } else {
            this.cards
                    .mapIndexed { index, card -> card.value - other.cards[index].value }
                    .first { it != 0 }
        }
    }

    fun compareToAllJockerCombinations(other: Bid): Int {
        val thisCardScore = this.getMaxJockerCardsScore()

        val otherCardScore = other.getMaxJockerCardsScore()
        return if (thisCardScore != otherCardScore) {
            thisCardScore.ordinal - otherCardScore.ordinal
        } else {
            this.cards
                    .mapIndexed { index, card -> card.value - other.cards[index].value }
                    .first { it != 0 }
        }
    }
}


// 250958588 too high
// 250019693 not right
// 249985106 not right
// 249991732 not right