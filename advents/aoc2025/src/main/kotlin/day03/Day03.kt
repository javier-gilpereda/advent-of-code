package com.gilpereda.aoc2025.day03

fun firstTask(input: Sequence<String>): String = input.map { PowerBank.from(it).maxPower2(2) }.sum().toString()

fun secondTask(input: Sequence<String>): String = input.map { PowerBank.from(it).maxPower2(12) }.sum().toString()

class PowerBank(
    val batteries: List<Int>,
) {
    fun maxPower2(digits: Int): Long {
        fun go(
            acc: Long,
            d: Int,
            rest: List<Int>,
        ): Long =
            if (d == 0) {
                acc
            } else {
                val nextDigit = rest.dropLast(d - 1).max()
                val indexOfNextDigit = rest.indexOf(nextDigit)
                val newRest = rest.drop(indexOfNextDigit + 1)
                go(acc * 10 + nextDigit, d - 1, newRest)
            }
        return go(0, digits, batteries)
    }

    companion object {
        fun from(line: String) = PowerBank(line.map { "$it".toInt() })
    }
}
