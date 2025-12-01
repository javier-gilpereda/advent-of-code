package com.gilpereda.aoc2025.day01

fun firstTask(input: Sequence<String>): String =
    input
        .map { Operation.from(it) }
        .fold(Pair(50, 0)) { acc, next ->
            val result = next.apply(acc.first)
            val nextCount = if (result == 0) acc.second + 1 else acc.second
            Pair(result, nextCount)
        }.second
        .toString()

fun secondTask(input: Sequence<String>): String =
    input
        .map { Operation.from(it) }
        .flatMap { it.flatten() }
        .fold(Pair(50, 0)) { acc, next ->
            val result = next.apply(acc.first)
            val nextCount = if (result == 0) acc.second + 1 else acc.second
            Pair(result, nextCount)
        }.second
        .toString()

private const val DIAL_COUNT = 100

sealed interface Operation {
    fun apply(value: Int): Int

    fun flatten(): List<Operation>

    data class Left(
        val count: Int,
    ) : Operation {
        override fun apply(value: Int): Int {
            val result = value - (count % DIAL_COUNT)
            return if (result < 0) {
                DIAL_COUNT + result
            } else {
                result
            }
        }

        override fun flatten(): List<Operation> = List(count) { Left(1) }
    }

    data class Right(
        val count: Int,
    ) : Operation {
        override fun apply(value: Int): Int = (value + count) % DIAL_COUNT

        override fun flatten(): List<Operation> = List(count) { Right(1) }
    }

    companion object {
        fun from(line: String): Operation =
            when (line[0]) {
                'L' -> Left(line.substring(1).toInt())
                'R' -> Right(line.substring(1).toInt())
                else -> throw IllegalArgumentException()
            }
    }
}
