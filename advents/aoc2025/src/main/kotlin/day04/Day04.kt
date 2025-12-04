package com.gilpereda.aoc2025.day04

import com.gilpereda.adventofcode.commons.geometry.Point
import com.gilpereda.adventofcode.commons.map.TypedTwoDimensionalMap
import com.gilpereda.adventofcode.commons.map.parseToMap

fun firstTask(input: Sequence<String>): String {
    val map =
        input.parseToMap { char ->
            when (char) {
                '.' -> Cell.Empty
                '@' -> Cell.Roll
                else -> throw IllegalArgumentException("Invalid char: $char")
            }
        }
    return map
        .mapIndexed { point, cell ->
            cell == Cell.Roll &&
                point.allNeighbours().count { map.getNullable(it) == Cell.Roll } < 4
        }.count { it }
        .toString()
}

fun secondTask(input: Sequence<String>): String {
    tailrec fun go(
        acc: Int,
        incr: Int,
        map: TypedTwoDimensionalMap<Cell>,
    ): Int =
        if (incr == 0) {
            acc
        } else {
            var count = 0
            val newMap =
                map.mapIndexed { point, cell ->
                    if (cell == Cell.Roll && point.allNeighbours().count { map.getNullable(it) == Cell.Roll } < 4) {
                        count++
                        Cell.Empty
                    } else {
                        cell
                    }
                }
            go(acc + count, count, newMap)
        }
    return go(
        0,
        Int.MAX_VALUE,
        input.parseToMap { char ->
            when (char) {
                '.' -> Cell.Empty
                '@' -> Cell.Roll
                else -> throw IllegalArgumentException("Invalid char: $char")
            }
        },
    ).toString()
}

enum class Cell {
    Empty,
    Roll,
}

fun Point.allNeighbours(): List<Point> =
    listOf(
        Point.from(x = x - 1, y = y - 1),
        Point.from(x = x, y = y - 1),
        Point.from(x = x + 1, y = y - 1),
        Point.from(x = x + 1, y = y),
        Point.from(x = x + 1, y = y + 1),
        Point.from(x = x, y = y + 1),
        Point.from(x = x - 1, y = y + 1),
        Point.from(x = x - 1, y = y),
    )
