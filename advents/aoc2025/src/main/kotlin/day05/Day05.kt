package com.gilpereda.aoc2025.day05

fun firstTask(input: Sequence<String>): String {
    val (ranges, ingredientList) =
        input.fold(Pair(mutableListOf<LongRange>(), mutableListOf<Long>())) { acc, line ->
            if (line.isBlank()) {
                acc
            } else {
                val ingredients = line.split("-")
                if (ingredients.size == 2) {
                    acc.first.add(ingredients[0].toLong()..ingredients[1].toLong())
                    acc
                } else {
                    acc.second.add(ingredients[0].toLong())
                    acc
                }
            }
        }

    return ingredientList
        .count { ingredient ->
            ranges.any { range -> ingredient in range }
        }.toString()
}

fun secondTask(input: Sequence<String>): String =
    input
        .takeWhile { it.isNotBlank() }
        .fold(mutableListOf<LongRange>()) { acc, line ->
            val newRange = line.split("-").let { it[0].toLong()..it[1].toLong() }

            val intersectBefore = acc.firstOrNull { range -> newRange.first in range }
            val intersectAfter = acc.firstOrNull { range -> newRange.last in range }
            acc.firstOrNull { range -> range.first in newRange && range.last in newRange }?.let { acc.remove(it) }

            when {
                intersectBefore == null && intersectAfter == null -> {
                    acc.add(newRange)
                }

                intersectBefore != null && intersectAfter != null -> {
                    acc.remove(intersectBefore)
                    acc.remove(intersectAfter)
                    acc.add(intersectBefore.first..intersectAfter.last)
                }

                intersectBefore != null -> {
                    acc.remove(intersectBefore)
                    acc.add(intersectBefore.first..newRange.last)
                }

                intersectAfter != null -> {
                    acc.remove(intersectAfter)
                    acc.add(newRange.first..intersectAfter.last)
                }
            }
            acc
        }.sumOf { it.last - it.first + 1 }
        .toString()
